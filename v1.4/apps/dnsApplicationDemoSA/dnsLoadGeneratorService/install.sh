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

# This recipe uses an image with all the necessary bits preinstalled but not activated.
# See *-service.groovy file for notes on the template used.

# Kill httpd if running at this time.
ps -ef | grep -iE "httpd" | grep -vi grep
if [ $? -eq 0 ] ; then 
  ps -ef | grep -iE "httpd" | grep -vi grep | awk '{print $2}' | xargs sudo kill -9
fi  


