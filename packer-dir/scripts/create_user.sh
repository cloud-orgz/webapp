#!/bin/bash

sudo groupadd -f identityhub
sudo useradd -r -g identityhub -s /usr/sbin/nologin identityhub || true

# Now set the ownership of the /opt/application directory
sudo chown -R identityhub:identityhub /opt/webapp

sudo chown -R identityhub:identityhub /var/logs/webapp