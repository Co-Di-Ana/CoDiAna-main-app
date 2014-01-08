#!/bin/bash
#

# $1 process PID

function memory {
	Info=$(pmap $1 -x 2> /dev/null | tail -n 1)
	if [ -n "$Info" ]; then
		InfoArray=($Info)
		echo ${InfoArray[3]}
	else
		echo -1
	fi
}


PID=$1

CURRENTMEMORY=1
TOTALMEMORY=0
TOTALTICKS=0
while [ "$CURRENTMEMORY" -ge "0" ]
do
	# get memory in bytes 
	CURRENTMEMORY=$(memory $PID)

	if [ "$CURRENTMEMORY" -ne "0" ]; then
		# increment total ticks
		TOTALTICKS=$(($TOTALTICKS+1))

		# add current memory value
		TOTALMEMORY=`echo "scale=9;($TOTALMEMORY+$CURRENTMEMORY/1024)" | bc`
	fi
	# echo $CURRENTMEMORY
	sleep 0.05
done

TOTALMEMORY=`echo "scale=0;($TOTALMEMORY*1024)/$TOTALTICKS" | bc`
echo $TOTALMEMORY

