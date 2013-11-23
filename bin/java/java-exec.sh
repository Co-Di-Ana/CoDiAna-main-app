#!/bin/bash

usage() { echo "Usage: $0 [-m <int>] [-i <file>] [-o <file>] [-e <file>] [-v <file>]" 1>&2; exit 1; }

while getopts ":m:i:o:e:v:" o; do
    case "${o}" in
	# maximum time
        m)
            m=${OPTARG}
            ;;

	# input file
        i)
            i=${OPTARG}
            ;;

	# output file
        o)
            o=${OPTARG}
            ;;

	# error file
        e)
            e=${OPTARG}
            ;;


	# verbose file
        v)
            v=${OPTARG}
            ;;

        *)
            usage
            ;;
    esac
done
# shift $((OPTIND-1))

if [ -z "${m}" ]; then
	m=60000
fi


"javac -classpath ~/NetBeansProjects/DPTest02/src/ -verbose  -d ~/NetBeansProjects/d/ -s ~/NetBeansProjects/s/ ~/NetBeansProjects/DPTest02/src/cz/edu/x3m/test/Main.java"

if [ -z "${OUTPUT}" ] && [ -z "${ERROR}" ]; then
	timeout -k ${MAXTIME} javac -verbose -classpath ${CLASSPATH} -d ${DESTINATION} ${STARTPATH} &
elif [ -z "${OUTPUT}" ]; then
	timeout -k ${MAXTIME} javac -verbose -classpath ${CLASSPATH} -d ${DESTINATION} ${STARTPATH} 2> ${ERROR} &
elif [ -z "${ERROR}" ]; then
	timeout -k ${MAXTIME} javac -verbose -classpath ${CLASSPATH} -d ${DESTINATION} ${STARTPATH} > ${OUTPUT} &
else
	timeout -k ${MAXTIME} javac -verbose -classpath ${CLASSPATH} -d ${DESTINATION} ${STARTPATH} > ${OUTPUT} 2> ${ERROR} &
fi
