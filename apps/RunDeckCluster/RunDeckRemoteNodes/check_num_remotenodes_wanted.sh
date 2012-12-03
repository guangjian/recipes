#!/bin/sh

# returns the contents of num_remotenodes_wanted.txt file
# this file is written by scaler.sh which can be found on the remote nodes in the /root/bin directory.
# users can use RunDeck to execute scaler.sh to increase or decrease the number of nodes wanted

cat /root/bin/num_remotenodes_wanted.txt