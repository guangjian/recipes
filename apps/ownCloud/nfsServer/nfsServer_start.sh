#!/bin/sh

# start up the nfs services now
service rpcbind start
service nfslock start
service nfs start

