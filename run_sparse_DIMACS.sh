#!/bin/bash

javac -cp . AllPairsShortestPathInc.java
for i in SYN_5 NY_50 BAY_1000 NY_5000 COL_10000
do
	echo "running usecase $i";
	java -Xmx7g -Xms7g -cp . AllPairsShortestPathInc ./testcases/DensityGraphs/$i/graph ./testcases/DensityGraphs/$i/inc baseline > ./testcases/DensityGraphs/$i/baseline.out
	java -Xmx7g -Xms7g -cp . AllPairsShortestPathInc ./testcases/DensityGraphs/$i/graph ./testcases/DensityGraphs/$i/inc incremental > ./testcases/DensityGraphs/$i/inc.out
	diff ./testcases/DensityGraphs/$i/baseline.out ./testcases/DensityGraphs/$i/inc.out > ./testcases/DensityGraphs/$i/diff.out
	cat ./testcases/DensityGraphs/$i/diff.out
done

