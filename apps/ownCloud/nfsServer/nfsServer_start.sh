#!/bin/sh

# start up the nfs services
service rpcbind start
service nfslock start
service nfs start

