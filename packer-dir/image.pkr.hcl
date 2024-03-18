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
  }

  provisioner "shell" {
    script = "./scripts/configure_application.sh"
  }

  provisioner "shell" {
    script = "./scripts/create_user.sh"
  }

  provisioner "file" {
    source      = var.artifact_path
    destination = "/opt/webapp/assignment1-1.0.0.jar"
  }

  provisioner "file" {
    source      = "./files/ops-agent-config.yaml"
    destination = "/tmp/ops-agent-config.yaml"
  }

  provisioner "shell" {
    script = "./scripts/install-ops-agent.sh"
  }
}


