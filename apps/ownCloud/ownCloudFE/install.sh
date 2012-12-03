#!/bin/sh
echo "Using yum to install pre-reqs: php, httpd, curl, wget"
yum install -y php* httpd *curl* wget nfs-utils

echo "Using wget to retrieve ownCloud package"
wget http://mirrors.owncloud.org/releases/owncloud-4.5.2.tar.bz2

echo "Extracting ownCloud package"
tar -xjf owncloud-4.5.2.tar.bz2

echo "Moving ownCloud to webserver"
cp -r owncloud /var/www/html

echo "Setting appropriate directory permissions"
chown -R apache:apache /var/www

echo "Restarting webserver service"
service httpd restart

