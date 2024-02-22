build {
  sources = [
    "source.googlecompute.centos_stream"
  ]

  provisioner "shell" {
    script = "./scripts/prepare_opt.sh"
  }
  // Copy application JAR from local path to the image
  provisioner "file" {
    source      = var.artifact_path
    destination = "/opt/webapp/assignment1-1.0.0.jar"
  }

  provisioner "shell" {
    script = "./scripts/install_dependencies.sh"
    environment_vars = [
      "MYSQL_USER=${var.mysql_user}",
      "MYSQL_PASSWORD=${var.mysql_password}",
    ]
  }

  provisioner "shell" {
    script = "./scripts/configure_application.sh"
    environment_vars = [
      "MYSQL_USER=${var.mysql_user}",
      "MYSQL_PASSWORD=${var.mysql_password}",
    ]
  }

  provisioner "shell" {
    script = "./scripts/create_user.sh"
  }
}
