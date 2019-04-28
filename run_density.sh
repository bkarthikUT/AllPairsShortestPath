#!/bin/bash

javac -cp . AllPairsShortestPathInc.java
for i in SocialNetwork-419 WebPages-589 YeastProteinInteraction-985 PsgAirTraffic-1148 P2PKazaa-3403 P2PWeb-6049 InternetRouters-10900	
do
	echo "running usecase $i";
	java -Xmx7g -Xms7g -cp . AllPairsShortestPathInc ./testcases/DensityGraphs/$i/graph ./testcases/DensityGraphs/$i/inc FW > ./testcases/DensityGraphs/$i/FW.out
	java -Xmx7g -Xms7g -cp . AllPairsShortestPathInc ./testcases/DensityGraphs/$i/graph ./testcases/DensityGraphs/$i/inc incremental > ./testcases/DensityGraphs/$i/inc.out
	diff ./testcases/DensityGraphs/$i/FW.out ./testcases/DensityGraphs/$i/inc.out > ./testcases/DensityGraphs/$i/diff.out
	cat ./testcases/DensityGraphs/$i/diff.out
done

