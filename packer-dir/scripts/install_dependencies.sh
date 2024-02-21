#!/bin/bash

# Update the system
sudo dnf update -y

# Install Java 17 JDK
sudo dnf install -y java-17-openjdk-devel

# Attempt to set Java 17 as the default version
JAVA_17_PATH=$(sudo update-alternatives --list java | grep 'java-17' | head -n 1)

if [[ ! -z "$JAVA_17_PATH" ]]; then
    echo "Setting Java 17 as the default Java version..."
    sudo update-alternatives --set java "$JAVA_17_PATH"
else
    echo "Java 17 is installed but not found in the alternatives list. Attempting to register it..."
    JAVA_17_BIN=$(dirname $(readlink -f $(which java)))
    if [[ "$JAVA_17_BIN" == *"/java-17"* ]]; then
        echo "Registering Java 17 with update-alternatives..."
        sudo update-alternatives --install /usr/bin/java java "$JAVA_17_BIN/java" 2
        sudo update-alternatives --set java "$JAVA_17_BIN/java"
    else
        echo "Unable to locate the Java 17 executable for registration."
    fi
fi

# Verification step
echo "Verification: Current default Java version:"
java -version


# Install Maven
sudo dnf install -y maven

# Install MySQL Server
sudo dnf install -y mysql-server

# Enable and start MySQL service
sudo systemctl enable mysqld
sudo systemctl start mysqld

# Secure the MySQL installation (optional, recommended for production)
# sudo mysql_secure_installation

# Create a MySQL user and grant privileges
# Create MySQL user
sudo mysql -e "CREATE USER '${MYSQL_USER}'@'localhost' IDENTIFIED BY '${MYSQL_PASSWORD}';"

# Grant privileges to the user
sudo mysql -e "GRANT ALL PRIVILEGES ON *.* TO '${MYSQL_USER}'@'localhost' WITH GRANT OPTION;"

# Apply changes
sudo mysql -e "FLUSH PRIVILEGES;"