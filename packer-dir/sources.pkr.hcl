source "googlecompute" "centos_stream" {
  project_id          = var.project_id
  zone                = var.zone
  source_image_family = var.image_family
  ssh_username        = "packer"
  machine_type        = var.machine_type
  image_name          = "webapp-image-${formatdate("YYYYMMDDHHmmss", timestamp())}"
  image_family        = "webapp-image-family"
}
