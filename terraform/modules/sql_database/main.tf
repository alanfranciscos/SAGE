provider "google" {
  project = var.project_id
  region  = var.location
}

data "google_compute_network" "default" {
  name = "default"
}

resource "google_sql_database_instance" "postgres-sage" {
  name             = "postgres-sage"
  region           = var.location
  database_version = "POSTGRES_15"
  root_password    = var.root_password

  settings {
    tier    = "db-f1-micro"
    edition = "STANDARD"

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
