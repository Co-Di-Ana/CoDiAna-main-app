@echo off
shift

java -classpath %1 %3 > %7 2> %9 < %5

echo run-time=187
echo memory-peak=25