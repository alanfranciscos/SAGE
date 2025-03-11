provider "google" {
  project = var.project_id
  region  = var.location
}

resource "google_cloud_run_service" "cloud_run_service" {
  name     = var.repository_id
  location = "us-east1"

  template {
    spec {
      containers {
        image = "gcr.io/${var.project_id}/${var.repository_id}/${var.repository_id}:latest"
        ports {
          container_port = 8080
        }
      }
    }
  }
}

terraform {
  backend "gcs" {
    bucket = "terraform-faitec-simac"
    prefix = "state/cloud_run/" + var.repository_id
  }
}
