#!/bin/bash

#javac -cp . AllPairsShortestPathInc.java
for i in d2 d4 d8 d16 d32 d64 d128 d256
do
	echo "running usecase $i";
	java -Xmx7g -Xms7g -cp . AllPairsShortestPathInc ./testcases/Density_Sweep/$i/graph ./testcases/Density_Sweep/$i/inc FW > ./testcases/Density_Sweep/$i/FW.out
	java -Xmx7g -Xms7g -cp . AllPairsShortestPathInc ./testcases/Density_Sweep/$i/graph ./testcases/Density_Sweep/$i/inc incremental > ./testcases/Density_Sweep/$i/inc.out
	diff ./testcases/Density_Sweep/$i/FW.out ./testcases/Density_Sweep/$i/inc.out > ./testcases/Density_Sweep/$i/diff.out
	cat ./testcases/Density_Sweep/$i/diff.out
done

