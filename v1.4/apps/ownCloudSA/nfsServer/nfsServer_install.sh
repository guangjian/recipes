#!/bin/sh

# Using an image with all the necessary components already installed.
# yum install -y nfs-utils httpd

# configure services
chkconfig rpcbind on
chkconfig nfslock on
chkconfig nfs on

# make a directory and make it available as an NFS directory
mkdir /opt/owncloudshare
chown -R daemon:daemon /opt/owncloudshare
chmod 0770 /opt/owncloudshare
echo '/opt/owncloudshare *(rw,no_root_squash,sync)' >> /etc/exports
