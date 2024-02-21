#!/bin/bash

# Create .env file with variable values
echo "DB_USERNAME=${MYSQL_USER}" | sudo tee /opt/webapp/.env
echo "DB_PASSWORD=${MYSQL_PASSWORD}" | sudo tee -a /opt/webapp/.env

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