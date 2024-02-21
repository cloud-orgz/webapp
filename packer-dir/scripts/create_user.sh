#!/bin/bash

sudo groupadd -f csye6225
sudo useradd -r -g csye6225 -s /usr/sbin/nologin csye6225 || true

# Now set the ownership of the /opt/application directory
sudo chown -R csye6225:csye6225 /opt/webapp

