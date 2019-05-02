package project.dijkstra;

import java.util.ArrayList;

public class Test {

	public static void main(String args[]) {

		String[] inputPaths = new String[] { "./testcases/DensityGraphs/", "./testcases/SparseGraphs/", "./testcases/Density_Sweep/" };

		String[] densityGraphs = new String[] { "SocialNetwork-419", "WebPages-589", "YeastProteinInteraction-985",
				"PsgAirTraffic-1148", "P2PKazaa-3403", "P2PWeb-6049", "InternetRouters-10900" };

		String[] sparseGraphs = new String[] { "SYN_5", "NY_50", "BAY_1000", "NY_5000", "COL_10000" };
		
		String[] densitySweepGraphs = new String[] { "d2", "d4", "d8", " d16", " d32" , "d64", " d128", "d256"};

		String inputFile = "graph";
		String incFile = "inc";
		String outputFile = "DK.out";
		
		ArrayList<String> paths = new ArrayList<>();
		for (String inputPath : inputPaths) {
			if (inputPath.equals("./testcases/DensityGraphs/")) {
				for (String densityGraph : densityGraphs) {
					String destination = inputPath + densityGraph ;
					paths.add(destination);
				}
			}else if (inputPath.equals("./testcases/SparseGraphs/")) {
				for (String sparseGraph : sparseGraphs) {
					String destination = inputPath + sparseGraph ;
					paths.add(destination);
				}
			}else {
				for (String densitySweepGraph : densitySweepGraphs) {
					String destination = inputPath + densitySweepGraph ;
					paths.add(destination);
				}
			}
			
		}
		
		for(int i=0; i<paths.size(); i++) {
			System.out.println(paths.get(i));
		}
	}

}
