#!/bin/bash

#javac -cp . AllPairsShortestPathInc.java
for i in SYN_5 NY_50 BAY_1000 NY_5000 COL_10000
do
	echo "running usecase $i";
	java -Xmx7g -Xms7g -cp . AllPairsShortestPathInc ./testcases/SparseGraphs/$i/graph ./testcases/SparseGraphs/$i/inc FW > ./testcases/SparseGraphs/$i/FW.out
	java -Xmx7g -Xms7g -cp . AllPairsShortestPathInc ./testcases/SparseGraphs/$i/graph ./testcases/SparseGraphs/$i/inc incremental > ./testcases/SparseGraphs/$i/inc.out
	diff ./testcases/SparseGraphs/$i/FW.out ./testcases/SparseGraphs/$i/inc.out > ./testcases/SparseGraphs/$i/diff.out
	cat ./testcases/SparseGraphs/$i/diff.out
done

