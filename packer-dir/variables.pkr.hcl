variable "project_id" {
  type        = string
  description = "The GCP project ID"
}

variable "zone" {
  type        = string
  description = "The GCP zone where resources will be created"
}

variable "image_family" {
  type        = string
  description = "Image family to use as a base"
  default     = "centos-stream-8"
}

variable "machine_type" {
  type        = string
  default     = "n1-standard-1"
  description = "Machine type to use for the build instance"
}

variable "artifact_path" {
  type        = string
  description = "java artifact_path"
}
