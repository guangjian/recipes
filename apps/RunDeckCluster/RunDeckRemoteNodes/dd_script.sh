#!/bin/sh

blocksize_list="4 8 32 128 256 512"
outfile="/tmp/ofile"


usage ()
{
        echo "USAGE: $0 [-o <outfile>] [-b <blocksize>]"
        echo "-o: The outfile to which bytes should be written."
        echo "    File is deleted when done."
        echo "    If omitted, defaults to /tmp/ofile."
        echo "-b: The blocksize in KB to use."
        echo "     If omitted the script will run the dd for the blocksizes:"
        echo "     [${blocksize_list}]"
}

while getopts o:b: param
do
        case "${param}" in
                b)      blocksize_list="${OPTARG}"
                        ;;
                o)      outfile="${OPTARG}"
                        ;;
                *)      usage
                        exit 1
                        ;;
        esac
done


# Get total system memory.  MULT is how many times total memory we want the files to be (should be > 1)
# The icky command says to get the Mem line from free, cut off the numbers, strip leading white space,
# and grab the first number on the remaining line (which is total system RAM)
#
MEM=$(free | grep Mem | cut -f 2 -d: | sed -e 's/^[[:space:]]*//' | cut -f1 -d" ")
MULT=2

# Run through different block sizes (in Kbytes) - change the list as needed.
# To figure count, assume we need MULT x physical RAM so count = MEM * MULT / bs.
#
for bs in ${blocksize_list}
do
	((count=MEM*MULT/bs))
	cmd="dd if=/dev/urandom of=${outfile} iflag=fullblock oflag=direct bs=${bs}K count=${count}"
	echo "cmd: ${cmd}"
	eval "${cmd}"
    rm ${outfile}
done
