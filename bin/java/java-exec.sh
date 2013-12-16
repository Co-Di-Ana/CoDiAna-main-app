#!/bin/bash

usage () {
	echo "Usage: $0 -c <path> -s <path> -d <path> [ <options> ]" 1>&2;
	echo "  -c <path>           classpath, where solution structure begins, source files ale located" 1>&2;
	echo "  -s <path>           startpath, main class path (start file with static main method, e.g. cz.tul.nti.Main)" 1>&2;

	echo "  -t <int>            maxtime in s, default 60s" 1>&2;
	echo "  -m <int>            maxmemory in MB, default 100MB" 1>&2;
	echo "  -i <path>           input file" 1>&2;
	echo "  -o <path>           output file" 1>&2;
	echo "  -e <path>           error file" 1>&2;
	echo "  -v                  verbose" 1>&2;
	echo "  -h                  help" 1>&2;
	exit 1;
}


# default max time
MAXTIME=60
MAXMEORY=100


while getopts ":c:s:d:t:m:i:o:e:hv" o; do
    case "${o}" in
        c)
			# classpath
            CLASSPATH=${OPTARG}
            ;;
        s)
			# startpath
            STARTPATH=${OPTARG}
            ;;
        t)
			# maximum time
            MAXTIME=${OPTARG}
			;;
        m)
			# maximum time
            MAXMEORY=${OPTARG}
            ;;
        i)
			# destination
            INPUT=${OPTARG}
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
	echo "MAXTIME     = ${MAXTIME}"
	echo "MAXMEORY    = ${MAXMEORY}"
	echo "INPUT       = ${INPUT}"
	echo "OUTPUT      = ${OUTPUT}"
	echo "ERROR       = ${ERROR}"
fi

if [ -z "${CLASSPATH}" ] || [ -z "${STARTPATH}" ]; then
	echo "missing required argument" 1>&2;
	usage
fi


STARTTIME=$(date +%s.%N)
if [ -z "${INPUT}" ]; then
	if [ -z "${OUTPUT}" ] && [ -z "${ERROR}" ]; then
		timeout ${MAXTIME} java -classpath ${CLASSPATH} ${STARTPATH}
	elif [ -z "${OUTPUT}" ]; then
		timeout ${MAXTIME} java -classpath ${CLASSPATH} ${STARTPATH} 2> "${ERROR}"
	elif [ -z "${ERROR}" ]; then
		timeout ${MAXTIME} java -classpath ${CLASSPATH} ${STARTPATH} > "${OUTPUT}"
	else
		timeout ${MAXTIME} java -classpath ${CLASSPATH} ${STARTPATH} > "${OUTPUT}" 2> "${ERROR}"
	fi
else
	if [ -z "${OUTPUT}" ] && [ -z "${ERROR}" ]; then
		timeout ${MAXTIME} java -classpath ${CLASSPATH} ${STARTPATH} < "${INPUT}"
	elif [ -z "${OUTPUT}" ]; then
		timeout ${MAXTIME} java -classpath ${CLASSPATH} ${STARTPATH} 2> "${ERROR}" < "${INPUT}"
	elif [ -z "${ERROR}" ]; then
		timeout ${MAXTIME} java -classpath ${CLASSPATH} ${STARTPATH} > "${OUTPUT}" < "${INPUT}"
	else
		timeout ${MAXTIME} java -classpath ${CLASSPATH} ${STARTPATH} > "${OUTPUT}" 2> "${ERROR}" < "${INPUT}"
	fi
fi
PID=$!
EXITVALUE=$?
ENDTIME=$(date +%s.%N)


DIFF=$(echo "${ENDTIME} - ${STARTTIME}" | bc)
echo "time=${DIFF}"
echo "memory=${MEMORY}"
echo "exit=${EXITVALUE}";
exit ${EXITVALUE};

#./java-compile.sh -c /home/hans/NetBeansProjects/DPTest02/src/ -s /home/hans/NetBeansProjects/DPTest02/src/cz/edu/x3m/test/Main.java -d /home/hans/NetBeansProjects/d/ -m 5 -o /home/hans/NetBeansProjects/d/o.txt -e /home/hans/NetBeansProjects/d/e.txt


