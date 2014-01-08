#!/bin/bash




PID=$1
LIMIT=$2

echo "PID=$PID"
echo "LIM=$LIMIT"

(sleep $LIMIT && kill $PID) 2>/dev/null &
WATCH=$!


if wait $PID 2>/dev/null; then
	EXITVALUE=$?
else
	EXITVALUE=124
fi


kill $WATCH 2>/dev/null


echo "exit=$EXITVALUE"
exit $EXITVALUE
