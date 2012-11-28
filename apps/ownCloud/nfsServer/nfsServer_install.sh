#!/bin/sh

# install nfs bits and make things so they start at boot
# also install httpd so that we have an apache user in place.
yum install -y nfs-utils httpd
chkconfig rpcbind on
chkconfig nfslock on
chkconfig nfs on

# make a directory and make it available as an NFS directory
mkdir /opt/owncloudshare
chown -R apache:apache /opt/owncloudshare
chmod 0770 /opt/owncloudshare
echo "/opt/owncloudshare 135.0.0.0/8(rw,no_root_squash,sync)" >> /etc/exports
