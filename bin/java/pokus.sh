#!/bin/bash


./java-compile.sh \
	-c /home/hans/NetBeansProjects/DPTest02/src/ \
	-s /home/hans/NetBeansProjects/DPTest02/src/cz/edu/x3m/test/Main.java \
	-d /home/hans/NetBeansProjects/DP/bin/java/result/ \


./java-exec.sh \
	-c /home/hans/NetBeansProjects/DP/bin/java/result/ \
	-s cz.edu.x3m.test.Main \
	-i /home/hans/NetBeansProjects/DP/bin/java/result/input \
	-o /home/hans/NetBeansProjects/DP/bin/java/result/output \
	-e /home/hans/NetBeansProjects/DP/bin/java/result/error \

