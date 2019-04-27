javac -cp . AllPairsShortestPathInc.java
java -Xmx1g -Xms1g -cp . AllPairsShortestPathInc ./testcases/NY_50/USA-road-d.NY.gr ./testcases/NY_50/USA-road-d.NY.inc.txt incremental > run_incremental.out
java -Xmx1g -Xms1g -cp . AllPairsShortestPathInc ./testcases/NY_50/USA-road-d.NY.gr ./testcases/NY_50/USA-road-d.NY.inc.txt baseline > run_baseline.out
diff run_baseline.out run_incremental.out
