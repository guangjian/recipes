mkdir /opt/ocStorage

echo "$1:/opt/owncloudshare /opt/ocStorage nfs rsize=8192,wsize=8192,timeo=14,rw" >> /etc/fstab

mount -a

echo "Setting appropriate directory permissions"
cd /var/www/html
chown -R apache:apache *

echo "Restarting webserver service"
service httpd restart