variable "project_id" {
  description = "ID from your project"
  type        = string

  validation {
    condition     = length(var.project_id) > 0
    error_message = "The project_id variable must be set."
  }
}

variable "repository_id" {
  description = "Repository ID"
  type        = string
  default     = "board-controller-api"

  validation {
    condition     = length(var.repository_id) > 0
    error_message = "The repository_id variable must be set."
  }
}

variable "location" {
  description = "Location of the repository"
  type        = string
  default     = "us-east1"

  validation {
    condition     = length(var.location) > 0
    error_message = "The location variable must be set."
  }
}
