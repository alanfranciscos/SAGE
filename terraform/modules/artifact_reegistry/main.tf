resource "google_artifact_registry_repository" "artifact_registry" {
  location      = var.location
  repository_id = var.repository_id
  format        = "DOCKER"
}

provider "google" {
  project = var.project_id
  region  = var.location
}

terraform {
  backend "gcs" {
    bucket = "terraform-faitec-sage"
  }
}
