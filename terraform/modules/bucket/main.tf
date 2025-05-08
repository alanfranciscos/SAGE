provider "google" {
  project = var.project_id
  region  = var.location
}


resource "google_storage_bucket" "static" {
 name          = "terraform-sage"
 location      =  var.location
 storage_class = "STANDARD"

 uniform_bucket_level_access = true

 versioning {
    enabled = true
 }

 lifecycle_rule {
    action {
      type = "Delete"
    }
    condition {
      age = 30
    }
 }
}
