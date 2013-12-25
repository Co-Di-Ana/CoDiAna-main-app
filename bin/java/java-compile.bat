@echo off
shift


javac -sourcepath %1 -d %5 %3 > %7 2> %9