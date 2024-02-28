#!/bin/bash

# Correctly format and create the systemd service file
sudo bash -c 'cat <<'EOF' > /etc/systemd/system/javaapp.service
[Unit]
Description=Java Application Service
After=network.target

[Service]
Type=simple
ExecStartPre=/bin/bash -c "for i in {1..10}; do if [ -f /opt/webapp/.env ]; then break; fi; sleep 1; done"
User=csye6225
Group=csye6225
EnvironmentFile=/opt/webapp/.env
ExecStart=/usr/bin/java -jar /opt/webapp/assignment1-1.0.0.jar
Restart=always
RestartSec=3
StandardOutput=syslog
StandardError=syslog
SyslogIdentifier=csye6225

[Install]
WantedBy=multi-user.target
EOF'

# Reload systemd to recognize the new service and enable it
sudo systemctl daemon-reload
sudo systemctl enable javaapp.service