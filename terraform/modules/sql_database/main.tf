provider "google" {
  project = var.project_id
  region  = var.location
}

resource "google_sql_database_instance" "postgres-sage" {
  name             = "postgres-sage"
  region           = var.location
  database_version = "POSTGRES_15"
  root_password    = var.root_password

  settings {
    tier    = "db-custom-1-3840"
    edition = "ENTERPRISE"
  }
  deletion_protection = true
}

resource "google_sql_user" "postgres_user" {
  name     = var.username
  instance = google_sql_database_instance.postgres-sage.name
  password = var.user_password
}




