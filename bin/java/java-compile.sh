#!/bin/bash

usage () {
	echo "Usage: $0 -c <path> -s <path> -d <path> [ <options> ]" 1>&2;
	echo "  -c <path>           classpath, where solution structure begins, source files ale located" 1>&2;
	echo "  -s <path>           startpath, main file path (start file with static main method, e.g. java file)" 1>&2;
	echo "  -d <path>           destination, where to place generated class files" 1>&2;

	echo "  -t <int>            maxtime in s" 1>&2;
	echo "  -m <int>            maxmemory in MB" 1>&2;
	echo "  -o <path>           output file" 1>&2;
	echo "  -e <path>           error file" 1>&2;
	echo "  -v                  verbose" 1>&2;
	echo "  -h                  help" 1>&2;
	exit 1;
}


# default max time
MAXTIME=60
MAXMEORY=100
VERBOSEARG=''


while getopts ":c:s:d:t:m:o:e:hv" o; do
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
        t)
			# maximum time
            MAXTIME=${OPTARG}
			;;
        m)
			# maximum time
            MAXMEORY=${OPTARG}
            ;;
        o)
			# output file
            OUTPUT=${OPTARG}
            ;;
        e)
			# error file
            ERROR=${OPTARG}
			;;
        v)
			# error file
            VERBOSE=1
			VERBOSEARG=-verbose
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

if [[ $VERBOSE -eq 1 ]]; then	
	echo "CLASSPATH   = ${CLASSPATH}"
	echo "STARTPATH   = ${STARTPATH}"
	echo "DESTINATION = ${DESTINATION}"
	echo "MAXTIME     = ${MAXTIME}"
	echo "MAXMEORY     = ${MAXMEORY}"
	echo "OUTPUT      = ${OUTPUT}"
	echo "ERROR       = ${ERROR}"
fi

if [ -z "${CLASSPATH}" ] || [ -z "${STARTPATH}" ] || [ -z "${DESTINATION}" ]; then
	echo "missing required argument" 1>&2;
	usage
fi


STARTTIME=$(date +%s.%N)
if [ -z "${OUTPUT}" ] && [ -z "${ERROR}" ]; then
	timeout ${MAXTIME} javac ${VERBOSEARG} -classpath ${CLASSPATH} -d ${DESTINATION} ${STARTPATH}
elif [ -z "${OUTPUT}" ]; then
	timeout ${MAXTIME} javac ${VERBOSEARG} -classpath ${CLASSPATH} -d ${DESTINATION} ${STARTPATH} 2> ${ERROR}
elif [ -z "${ERROR}" ]; then
	timeout ${MAXTIME} javac ${VERBOSEARG} -classpath ${CLASSPATH} -d ${DESTINATION} ${STARTPATH} > ${OUTPUT}
else
	timeout ${MAXTIME} javac ${VERBOSEARG} -classpath ${CLASSPATH} -d ${DESTINATION} ${STARTPATH} > ${OUTPUT} 2> ${ERROR}
fi
EXITVALUE=$?
ENDTIME=$(date +%s.%N)


DIFF=$(echo "${ENDTIME} - ${STARTTIME}" | bc)
echo "time=${DIFF}"
echo "memory=${MEMORY}"
echo "exit=${EXITVALUE}";
exit ${EXITVALUE};

#./java-compile.sh -c /home/hans/NetBeansProjects/DPTest02/src/ -s /home/hans/NetBeansProjects/DPTest02/src/cz/edu/x3m/test/Main.java -d /home/hans/NetBeansProjects/d/ -m 5 -o /home/hans/NetBeansProjects/d/o.txt -e /home/hans/NetBeansProjects/d/e.txt


