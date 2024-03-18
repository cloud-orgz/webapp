#!/bin/bash
sudo mkdir -p /opt
sudo chown packer:packer /opt

sudo mkdir -p /opt/webapp
sudo mkdir -p /var/logs/webapp
sudo chown packer:packer /opt/webapp
sudo chown packer:packer /var/logs/webapp