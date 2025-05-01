variable "project_id" {
  description = "ID from your project"
  type        = string

  validation {
    condition     = length(var.project_id) > 0
    error_message = "The project_id variable must be set."
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

variable "root_password" {
  description = "Root password for the database"
  type        = string

  validation {
    condition     = length(var.root_password) > 0
    error_message = "The root_password variable must be set."
  }
}

variable "user_password" {
  description = "User password for the database"
  type        = string

  validation {
    condition     = length(var.user_password) > 0
    error_message = "The user_password variable must be set."
  }
}

variable "username" {
  description = "Username for the database"
  type        = string

  validation {
    condition     = length(var.username) > 0
    error_message = "The username variable must be set."
  }
}
