#!/bin/bash

usage () {
	echo "Usage: $0 -c <path> -l <path> -d <path> [ <options> ]" 1>&2;
	echo "  -c <path>           classpath, where solution structure begins, source files ale located" 1>&2;
	echo "  -s <path>           startpath, main file path (start file with static main method)" 1>&2;
	echo "  -d <path>           destination, where to place generated class files" 1>&2;

	echo "  -m <int>            maxtime in ms" 1>&2;
	echo "  -o <path>           output file" 1>&2;
	echo "  -e <path>           error file" 1>&2;
	echo "  -h                  help" 1>&2;
	exit 1;
}


# default max time
MAXTIME=0.1


while getopts ":c:s:d:m:o:e:h" o; do
    case "${o}" in
        c)
			# classpath
            CLASSPATH=${OPTARG}
            ;;
        s)
			# startpath
            STARTPATH=${OPTARG}
            ;;
        d)
			# destination
            DESTINATION=${OPTARG}
            ;;
        m)
			# maximum time
            MAXTIME=${OPTARG}
            ;;
        o)
			# output file
            OUTPUT=${OPTARG}
            ;;
        e)
			# error file
            ERROR=${OPTARG}
			;;
        h)
			# help
            usage
            ;;
        *)
			# invalid option
            usage
            ;;
        :)
			# missing argument
            usage
            ;;
    esac
done
shift $((OPTIND-1))


echo "CLASSPATH   = ${CLASSPATH}"
echo "STARTPATH   = ${STARTPATH}"
echo "DESTINATION = ${DESTINATION}"
echo "MAXTIME     = ${MAXTIME}"
echo "OUTPUT      = ${OUTPUT}"
echo "ERROR       = ${ERROR}"

if [ -z "${CLASSPATH}" ] || [ -z "${STARTPATH}" ] || [ -z "${DESTINATION}" ]; then
	echo "missing required argument"
	usage
fi



sleep 1 &

PID=$!

while kill -s 0 ${PID} 2> /dev/null; do
	sleep 0.1s
	echo "still running"
done


#"javac -classpath ~/NetBeansProjects/DPTest02/src/ -verbose  -d ~/NetBeansProjects/d/ -s ~/NetBeansProjects/s/ ~/NetBeansProjects/DPTest02/src/cz/edu/x3m/test/Main.java"


