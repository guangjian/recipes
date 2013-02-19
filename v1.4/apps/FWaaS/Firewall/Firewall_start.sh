#!/bin/sh

# This "start" script does nothing at this time other than prove it was executed on the vyatta server.
# This will help test whether or not recipe-ization of the vyatta server is even possible.

rightnow=`date`
echo "Firewall_start.sh: Wrote the date: ${rightnow}" >> /tmp/firewall_start_was_here.txt
echo "" >> /tmp/firewall_start_was_here.txt

