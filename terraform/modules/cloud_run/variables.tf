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

variable "http2" {
  description = "HTTP/2"
  type        = bool
  default     = false
}

variable "DB_URL" {
  description = "Database URL"
  type        = string

  validation {
    condition     = length(var.DB_URL) > 0
    error_message = "The DB_URL variable must be set."
  }
}

variable "SQL_USERNAME" {
  description = "SQL username"
  type        = string

  validation {
    condition     = length(var.SQL_USERNAME) > 0
    error_message = "The SQL_USERNAME variable must be set."
  }
}

variable "SQL_PASSWORD" {
  description = "SQL password"
  type        = string

  validation {
    condition     = length(var.SQL_PASSWORD) > 0
    error_message = "The SQL_PASSWORD variable must be set."
  }
}
