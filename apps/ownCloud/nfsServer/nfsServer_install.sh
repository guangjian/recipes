#!/bin/sh

# install nfs bits and make things so they start at boot
yum install -y nfs-utils
chkconfig rpcbind on
chkconfig nfslock on
chkconfig nfs on

# make a directory and make it available as an NFS directory
mkdir /opt/owncloudshare
echo "/opt/owncloudshare 135.0.0.0/8(rw,no_root_squash,sync)" >> /etc/exports
