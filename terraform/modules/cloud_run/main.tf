provider "google" {
  project = var.project_id
  region  = var.location
}

resource "google_cloud_run_service" "cloud_run_service" {
  name     = var.repository_id
  location = "us-east1"

  template {
    metadata {
      annotations = {
        "run.googleapis.com/protocol" = var.http2 ? "h2c" : "http"
      }
    }
    spec {
      containers {
        image = "us-east1-docker.pkg.dev/${var.project_id}/${var.repository_id}/${var.repository_id}:latest"
        ports {
          container_port = 8080
          name           = var.http2 ? "h2c" : "http1"
        }

        env {
          name = "VERSION"
          value = "1.0.0"
        }

        env {
          name  = "JPA_SHOW_SQL"
          value = "false"
        }

        env {
          name = "DB_URL"
          value = var.DB_URL
        }

         env {
          name  = "SQL_USERNAME"
          value = var.SQL_USERNAME
        }

        env {
          name  = "SQL_PASSWORD"
          value = var.SQL_PASSWORD
        }

      }
    }
  }
  traffic {
    percent         = 100
    latest_revision = true
  }
}

terraform {
  backend "gcs" {
    bucket = "terraform-sage"
  }
}
