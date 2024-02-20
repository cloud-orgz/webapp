#!/bin/bash

# Create .env file
echo 'DB_USERNAME=csye6225' | sudo tee /opt/webapp/.env
echo 'DB_PASSWORD=password' | sudo tee -a /opt/webapp/.env

# Correctly format and create the systemd service file
sudo bash -c 'cat <<EOF > /etc/systemd/system/javaapp.service
[Unit]
Description=Java Application Service

[Service]
EnvironmentFile=/opt/webapp/.env
ExecStart=/usr/bin/java -jar /opt/webapp/assignment1-1.0.0.jar

[Install]
WantedBy=multi-user.target
EOF'

# Reload systemd to recognize the new service and enable it
sudo systemctl daemon-reload
sudo systemctl enable javaapp.service