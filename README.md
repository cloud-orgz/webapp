# webapp



## Running Spring Boot Application on CentOS

The steps required to run a Spring Boot application on a CentOS server. It includes SSH access, activating swap memory, installing MySQL, Java, Maven, and finally, deploying and running the Spring Boot application.

## Prerequisites

- CentOS server access
- SSH client installed on your machine
- Root privileges on the CentOS server

## Step 1: SSH into Your CentOS Server

Start by SSH'ing into your CentOS server with the following command. Replace the key location and server IP address as per your setup:

```
ssh -i /path/to/key root@{ipAddress}
```

## Step 2: Activate Swap Memory

Allocate and activate swap memory using the following commands:

```
sudo fallocate -l 1G /swapfile
sudo dd if=/dev/zero of=/swapfile bs=1M count=1024
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile
echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab
sudo swapon --show
free -h
```

## Step 3: Install MySQL

Install and start MySQL:

```
sudo dnf install @mysql
sudo systemctl enable --now mysqld
sudo systemctl status mysqld
```

Secure your MySQL installation:

```
sudo mysql_secure_installation
```

Create a new MySQL user:

```
mysql -u root -p
CREATE USER 'mukul'@'localhost' IDENTIFIED BY 'Mukul123@';
```

## Step 4: Install Java 17

```
sudo yum install java-17-openjdk
```

## Step 5: Install Maven

```
sudo dnf install maven
```

Configure Maven environment:

```
sudo vi /etc/profile.d/maven.sh
```

Add the following environment variables:

```
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-17.0.6.0.9-0.3.ea.el8.x86_64
export M2_HOME=/opt/maven
export MAVEN_HOME=/opt/maven
export PATH=${M2_HOME}/bin:${PATH}
```

Activate the new environment variables:

```
sudo chmod +x /etc/profile.d/maven.sh
source /etc/profile.d/maven.sh
```

Verify Maven installation:

```
mvn -version
```

## Step 6: Deploy Spring Boot Application

Transfer the Spring Boot application ZIP file to the CentOS server:

```
scp -i .\tester-centos .\Downloads\webapp-main.zip root@24.199.126.5:/tmp
```

Navigate to the temp directory, assign execution permissions, and copy the ZIP to the home directory:

```
cd /tmp
sudo chmod +x webapp-main.zip
cp webapp-main.zip ~
```

Install `unzip` if not already installed:

```
sudo yum install unzip
```

Unzip the application:

```
unzip webapp-main.zip
```

Navigate to the application directory:

```
cd webapp
```

Set the environment variables in `.env` file as needed:

```
vi .env
```

Build and run the application:

```
mvn clean install
mvn spring-boot:run
```

You have now successfully deployed and started your Spring Boot application on a CentOS server.

--- 

Adjust paths, IP addresses, and other configurations according to your actual environment and requirements.