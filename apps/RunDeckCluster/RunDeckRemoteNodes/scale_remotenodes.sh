#!/bin/sh

# use this to set the number of remotenodes you want scaled up.

if [ $# -ne 1 ]
then
	echo "USAGE: $0 <NUMBER OF REMOTE NODES WANTED>"
	exit 1
fi

# overwrite whatever value was there
echo ${1} > /root/bin/num_remotenodes_wanted.txt