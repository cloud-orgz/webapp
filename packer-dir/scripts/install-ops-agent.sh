#!/bin/bash

# Exit immediately if a command exits with a non-zero status.
set -e

# Download and install the Google Cloud Ops Agent
echo "Downloading and installing Google Cloud Ops Agent..."
curl -sSO https://dl.google.com/cloudagents/add-google-cloud-ops-agent-repo.sh
sudo bash add-google-cloud-ops-agent-repo.sh --also-install

sudo mv /tmp/ops-agent-config.yaml /etc/google-cloud-ops-agent/config.yaml

sudo systemctl daemon-reload
sudo systemctl enable google-cloud-ops-agent

echo "Google Cloud Ops Agent installation and configuration complete."
