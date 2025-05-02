#  O Cloud SQL precisa que o Service Networking esteja habilitado corretamente na VPC.

provider "google" {
  project = var.project_id
  region  = var.location
}

resource "google_project_service" "service_networking" {
  service = "servicenetworking.googleapis.com"
  project = var.project_id
}


data "google_compute_network" "default" {
  name = "default"
}

resource "google_compute_global_address" "private_ip_alloc" {
  name          = "sql-private-ip-range"
  purpose       = "VPC_PEERING"
  address_type  = "INTERNAL"
  prefix_length = 16
  network       = data.google_compute_network.default.id
}

resource "google_service_networking_connection" "private_vpc_connection" {
  network                 = data.google_compute_network.default.id
  service                 = "servicenetworking.googleapis.com"
  reserved_peering_ranges = [google_compute_global_address.private_ip_alloc.name]

  depends_on = [google_project_service.service_networking]
}

resource "google_sql_database_instance" "postgres-sage" {
  name             = "postgres-sage"
  region           = var.location
  database_version = "POSTGRES_15"
  root_password    = var.root_password

  settings {
    tier            = "db-f1-micro"
    disk_type       = "PD_HDD"
    disk_autoresize = false
    disk_size       = 10

    ip_configuration {
      ipv4_enabled    = false
      private_network = data.google_compute_network.default.id
    }

    backup_configuration {
      enabled = false
    }
    maintenance_window {
      day          = 7
      hour         = 3
      update_track = "canary"
    }

  }
  deletion_protection = false

  depends_on = [google_service_networking_connection.private_vpc_connection]
}

resource "google_sql_user" "postgres_user" {
  name     = var.username
  instance = google_sql_database_instance.postgres-sage.name
  password = var.user_password
}
resource "google_sql_database" "sage_db" {
  name     = "sage"
  instance = google_sql_database_instance.postgres-sage.name
}
