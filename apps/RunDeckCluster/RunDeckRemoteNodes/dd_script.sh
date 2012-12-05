#!/bin/sh

USAGE="usage: dd-test outfile\
where outfile is the file to which the bytes should be written"
# no error checking is done other than that outfile is specified.

if [ $# -ne 1 ]
then
      echo "need an output file" >&2
      echo "${USAGE}" >&2
      exit 1
fi

# Get total system memory.  MULT is how many times total memory we want the files to be (should be > 1)
# The icky command says to get the Mem line from free, cut off the numbers, strip leading white space,
# and grab the first number on the remaining line (which is total system RAM)
#
MEM=$(free | grep Mem | cut -f 2 -d: | sed -e 's/^[[:space:]]*//' | cut -f1 -d" ")
MULT=2

# Run through different block sizes (in Kbytes) - change the list as needed.
# To figure count, assume we need MULT x physical RAM so count = MEM * MULT / bs.
#
for bs in 4 8 32 128 256 512
do
	count=$(echo ${MEM}*${MULT}/${bs} | bc)
	cmd="dd if=/dev/urandom of=$1 iflag=fullblock oflag=direct bs=${bs}K count=${count}"
	echo "cmd: ${cmd}"
	eval "${cmd}"
    rm $1
done
