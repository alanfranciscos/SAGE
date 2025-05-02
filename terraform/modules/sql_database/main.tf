#Enable this:
#https://console.developers.google.com/apis/api/cloudresourcemanager.googleapis.com/overview?project=437196334396

provider "google" {
  project = var.project_id
  region  = var.location
}

resource "google_compute_global_address" "private_ip_address" {
  name          = "cloudsql-private-ip-range"
  purpose       = "VPC_PEERING"
  address_type  = "INTERNAL"
  prefix_length = 24
  network       = "projects/${var.project_id}/global/networks/default"
}

resource "google_service_networking_connection" "private_vpc_connection" {
  network                 = "projects/${var.project_id}/global/networks/default"
  service                 = "servicenetworking.googleapis.com"
  reserved_peering_ranges = [google_compute_global_address.private_ip_address.name]
}
resource "google_sql_database_instance" "postgres-sage" {
  name             = "postgres-sage"
  region           = var.location
  database_version = "POSTGRES_15"
  root_password    = var.root_password

  settings {
    tier = "db-f1-micro"

    disk_type       = "PD_HDD"
    disk_autoresize = false
    disk_size       = 10

    ip_configuration {
      ipv4_enabled    = true
      private_network = "projects/${var.project_id}/global/networks/default"
    }

    backup_configuration {
      enabled = false
    }
    maintenance_window {
      day          = 7
      hour         = 3
      update_track = "canary" # 
    }

  }
  deletion_protection = false
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
