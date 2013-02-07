#!/bin/sh

# This script sets things up for the dnsLoadGenerator to use the nova client which accesses the CloudBand northbound OpenStack API
# It is executed during the prestart.
#


# these environment variables are used by nova client when called
OS_USERNAME="${1}"
OS_PASSWORD="${2}"
OS_TENANT_NAME="${3}"
OS_AUTH_URL="${4}"

NOVA_WRAPPER="${5}"

# This recipe uses an image that has all the necessary bits installed.
# See the -service.groovy file for link to wiki describing those bits.


# build a nova wrapper script that has the credentials prebuilt in there
# Not used at this time, but leaving in place, just in case.
echo "nova --os_username '${OS_USERNAME}' --os_password '${OS_PASSWORD}' --os_tenant_name '${OS_TENANT_NAME}' --os_auth_url '${OS_AUTH_URL}' --insecure \$*" > ${NOVA_WRAPPER}

# make it executable
chmod +x ${NOVA_WRAPPER}
