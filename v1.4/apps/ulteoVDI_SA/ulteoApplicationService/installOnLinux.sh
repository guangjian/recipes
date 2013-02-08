#!/bin/bash

# args:
# $1 the error code of the last command (should be explicitly passed)
# $2 the message to print in case of an error
# 
# an error message is printed and the script exists with the provided error code
function error_exit {
	echo "$2 : error code: $1"
	exit ${1}
}

export PATH=$PATH:/usr/sbin:/sbin || error_exit $? "Failed on: export PATH=$PATH:/usr/sbin:/sbin"

echo "Config ovd-subsystem-config with SM = " $1
ovd-subsystem-config --chroot-uri file:///opt/ulteo_files/base.tar.gz --chroot-dir /opt/ulteo --sm-address $1  < args.dat || error_exit $? "Failed on: ovd-subsystem-config --chroot-uri http://10.45.4.64/gigaspaces-repo-test/base.tar.gz --chroot-dir /opt/ulteo --sm-address $1  < args.dat"

echo "Stoping ulteo-ovd-subsystem before Start lifecycle"
service ulteo-ovd-subsystem stop  || error_exit $? "Failed on: service ovd-subsystem-config stop"


