import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AllPairsShortestPathInc {

	final static int INF = Integer.MAX_VALUE;
	long finalStartTime, finalEndTime;
	static long finalTotalTime;

	int[][] floydWarshall(int dist[][], int V) 
	{ 
		finalStartTime = System.nanoTime();
		
		int i, j, k, sum; 

		/* Add all vertices one by one to the set of intermediate 
		vertices. 
		---> Before start of an iteration, we have shortest 
			distances between all pairs of vertices such that 
			the shortest distances consider only the vertices in 
			set {0, 1, 2, .. k-1} as intermediate vertices. 
		----> After the end of an iteration, vertex no. k is added 
				to the set of intermediate vertices and the set 
				becomes {0, 1, 2, .. k} */
		for (k = 0; k < V; k++) 
		{ 
			/*if(k % 100 == 0) {
				System.out.println("Time is: " + (System.nanoTime()-finalStartTime)/1000000000 + " seconds and Vertices completed: " + k);
			}*/
			// Pick all vertices as source one by one 
			for (i = 0; i < V; i++) 
			{ 
				// Pick all vertices as destination for the 
				// above picked source 
				for (j = 0; j < V; j++) 
				{ 
					// If vertex k is on the shortest path from 
					// i to j, then update the value of dist[i][j]
					if (dist[i][k] == INF || dist[k][j] == INF) {
						sum = INF;
					} else {
						sum = dist[i][k] + dist[k][j];
					}
						
					if (sum < dist[i][j]) {
						dist[i][j] = sum;
					}	 
				} 
			} 
		}
		
		finalEndTime = System.nanoTime();
		finalTotalTime = finalEndTime-finalStartTime;

		return dist;
	}
	
	int[][] incrementalAllPairsShortestPath(int newDist[][], int newEdges[][], int V) {
		
		finalStartTime = System.nanoTime();
		
		// Step 1 - Finding the shortest path distance between new node and existing nodes
		for (int j = 0; j < newEdges.length; j++)
		{
			if (newEdges[j][0] == 0) {
				newDist[V][newEdges[j][1]-1] = (newEdges[j][2] < newDist[V][newEdges[j][1]-1]) ? newEdges[j][2] : newDist[V][newEdges[j][1]-1];
				for (int i = 0; i < V+1; i++) {
					int altDist = (newDist[V][(newEdges[j][1])-1] == INF || newDist[(newEdges[j][1])-1][i] == INF) ? INF 
							: newDist[V][(newEdges[j][1])-1]+newDist[(newEdges[j][1])-1][i];
					newDist[V][i] = newDist[V][i] < altDist ? newDist[V][i] : altDist;	
				}
			} else if (newEdges[j][0] == 1) {
				newDist[(newEdges[j][1])-1][V] = (newEdges[j][2] < newDist[(newEdges[j][1])-1][V]) ? newEdges[j][2] : newDist[(newEdges[j][1])-1][V];
				for (int i = 0; i < V+1; i++) {
					int altDist = (newDist[i][(newEdges[j][1])-1] == INF || newDist[(newEdges[j][1])-1][V] == INF ) ? INF 
							: newDist[i][(newEdges[j][1])-1]+newDist[(newEdges[j][1])-1][V];
					newDist[i][V] = newDist[i][V] < altDist ? newDist[i][V] : altDist;
				}
			} else {
				System.out.println("Invalid Input!");
				break;
			}
		}
		
		//long step1Time = System.nanoTime();
		
		// Step 2 - Check for updates to shortest path distances between the existing nodes
		for (int i = 0; i < V; i++) {
			for (int j = 0; j < V; j++) {
				int altDist = (newDist[i][V] == INF || newDist[V][j] == INF) ? INF : newDist[i][V]+newDist[V][j];
				newDist[i][j] = newDist[i][j] < altDist ? newDist[i][j] : altDist;
			}
		}
		
		//long step2Time = System.nanoTime();
		
		finalEndTime = System.nanoTime();
		finalTotalTime = finalEndTime-finalStartTime;
		/*finalTotalTime = finalEndTime-finalStartTime-((finalEndTime - step2Time)*2);
		System.out.println("step1Time is " + (step1Time - finalStartTime));
		System.out.println("step2Time is " + (step2Time - step1Time));
		System.out.println("nanoTime is " + ((finalEndTime - step2Time)*2));*/
		
		return newDist;	
	}
	
	private int[][] populateGraph(String inputFile, String inputIncFile) {
		int[][] distance = null;
		int noOfVertices;
		FileReader fr = null;
		BufferedReader br = null;
		String[] filename = {inputFile, inputIncFile};
		
		try {
			for(int k = 0; k < filename.length; k++) {
				fr = new FileReader(filename[k]);
				br = new BufferedReader(fr);

				String sCurrentLine;

				while ((sCurrentLine = br.readLine()) != null) {
					String[] temp = sCurrentLine.split(" ");
					if ("p".equals(temp[0])) {
						noOfVertices = Integer.parseInt(temp[2])+1;
						distance = new int[noOfVertices][noOfVertices];
						
						//Initialize the distances with zero and infinity
						for (int i = 0; i < noOfVertices; i++) {
							for (int j = 0; j < noOfVertices; j++) {
								if (i == j) {
									distance[i][j] = 0;
								} else {
									distance[i][j] = Integer.MAX_VALUE;
								}
							}
						}
					}
					if ("a".equals(temp[0])) {
						int x = Integer.parseInt(temp[1]);
						int y = Integer.parseInt(temp[2]);
						int weight = Integer.parseInt(temp[3]);
						if(weight < distance[x - 1][y - 1]) {
							distance[x - 1][y - 1] = weight;	
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fr != null) {
					fr.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return distance;
	}

	void printMatrix(int dist[][], String label) 
	{ 
		int mSize = dist.length;
		System.out.println(label + " is:");
		
		for (int i = 0; i < mSize; ++i) 
		{ 
			for (int j = 0; j < mSize; ++j) 
			{ 
				if (dist[i][j]==INF) 
					System.out.print("INF "); 
				else
					System.out.print(dist[i][j]+" "); 
			} 
			System.out.println(); 
		} 
	}

	public static void main (String[] args) throws Exception 
	{
		/*
		 * @param - args[0] is the file with input graph as an adjacency list
		 * args[1] is the file with incremental input as an adjacency list
		 * args[2] is the mode to run ("FW" or "incremental")  
		 */
		String inputFile = args[0];
		String incAdjListFile = args[1];
		String mode = args[2];
		int n;
		int dist[][] = null;
		int incGraph[][] = null;
      	String str;
		String st[] = new String[3];
		ArrayList<Integer> input = new ArrayList<Integer>();
		
		AllPairsShortestPathInc a = new AllPairsShortestPathInc();
		
		//Read input and construct the graph as adjacency matrix
		incGraph = a.populateGraph(inputFile, incAdjListFile);
		
		//a.printMatrix(incGraph,"Incremental Graph");
		
		/*
		 *  Run FW algorithm if mode is "FW" or 
		 *  incremental algorithm if mode is "incremental"
		 */
		if(mode.equals("FW")) {
			dist = a.floydWarshall(incGraph, incGraph.length);
		} else {
	      	/* 
	      	 * Reading file with info on new edges and weights (from new node)
	      	 * and constructing the adjacency list
	      	 */
	      	File file = new File(incAdjListFile); 
			
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			while ((str = br.readLine()) != null) {
				st = str.split(" ");
				for(int s=1; s<st.length; s++) {
					input.add(Integer.parseInt(st[s]));
				}
			}
			
			n = input.get(0);
			
			/*
			 * Format of adjList: adjList[edge#][0] - incoming (1) or outgoing (0)
			 * adjList[edge#][1] - node that the edge connects the new node with
			 * adjList[edge#][2] - weight of this edge
			 */
			
			int adjList[][] = new int[n][3];

			for (int j = 1; j < 3*n; j+=3) {
				if(input.get(j) < input.get(j+1)) {
					adjList[(j-1)/3][0] = 1;
					adjList[(j-1)/3][1] = input.get(j);
				}else {
					adjList[(j-1)/3][0] = 0;
					adjList[(j-1)/3][1] = input.get(j+1);
				}
				adjList[(j-1)/3][2] = input.get(j+2);
			}
	      	
			br.close();
	
			/* 
			 * Run Floyd Warshall baseline algorithm and
			 * get the shortest path distances for old graph
			 */
			int tempDist[][] = a.floydWarshall(incGraph, incGraph.length-1);
					
			// Calling the incremental algorithm
			dist = a.incrementalAllPairsShortestPath(tempDist, adjList, tempDist.length-1);
		}
		
		// Print the final matrix with the shortest distances
		a.printMatrix(dist, "Shortest path solution");
		System.out.println("Total time taken for the " + mode + " algorithm is " + finalTotalTime);
	} 
}
