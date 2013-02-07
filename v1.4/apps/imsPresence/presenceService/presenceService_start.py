#!/usr/bin/python
#
# Takes 4  parameters:
# $1 - IP of vSphere server
# $2 - vSphere username
# $3 - vSphere password
# $4 - VM to unsuspend/start

import sys
import vSphereFunctions
vSphereFunctions.vm_start(sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4])
