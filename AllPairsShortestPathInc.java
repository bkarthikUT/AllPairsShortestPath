import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class AllPairsShortestPathInc {

	final static int INF = 999999;
	long finalStartTime, finalEndTime;
	static long finalTotalTime;

	int[][] floydWarshall(int graph[][]) 
	{ 
		finalStartTime = System.nanoTime();
		
		int V = graph.length;
		int dist[][] = new int[V][V]; 
		int i, j, k; 

		/* Initialize the solution matrix same as input graph matrix. 
		Or we can say the initial values of shortest distances 
		are based on shortest paths considering no intermediate 
		vertex. */
		for (i = 0; i < V; i++) 
			for (j = 0; j < V; j++) 
				dist[i][j] = graph[i][j]; 

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
			// Pick all vertices as source one by one 
			for (i = 0; i < V; i++) 
			{ 
				// Pick all vertices as destination for the 
				// above picked source 
				for (j = 0; j < V; j++) 
				{ 
					// If vertex k is on the shortest path from 
					// i to j, then update the value of dist[i][j] 
					if (dist[i][k] + dist[k][j] < dist[i][j]) 
						dist[i][j] = dist[i][k] + dist[k][j]; 
				} 
			} 
		}
		
		finalEndTime = System.nanoTime();
		finalTotalTime = finalEndTime-finalStartTime;

		return dist;
	}
	
	int[][] incrementalAllPairsShortestPath(int oldDist[][], int newEdges[][][]) {
		
		finalStartTime = System.nanoTime();
		
		int V = oldDist.length;
		int newDist[][] = new int[V+1][V+1];
		
		//long allocTime = System.nanoTime();
		
		// Copying existing shortest path distances to the new matrix
		for (int a = 0; a < V; a++) 
		{ 
			for (int b = 0; b < V; b++) 
			{ 
				newDist[a][b] = oldDist[a][b];
			}
		}
		
		//long copyTime = System.nanoTime();
		
		// Temporarily filling the distances as Infinity for the new node
		for (int k = 0; k < V; k++) 
		{ 
			newDist[V][k] = INF;
			newDist[k][V] = INF;
		}
		newDist[V][V] = 0;
		
		//long fillTime = System.nanoTime();
		
		// Step 1 - Finding the shortest path distance between new node and existing nodes
		for (int j = 0; j < newEdges.length; j++)
		{
			if (newEdges[0][j][0] == 0) {
				newDist[V][newEdges[0][j][1]-1] = newEdges[0][j][2];
				for (int i = 0; i < V+1; i++) {
					int altDist = newDist[V][newEdges[0][j][1]-1]+newDist[newEdges[0][j][1]-1][i];
					newDist[V][i] = newDist[V][i] < altDist ? newDist[V][i] : altDist;	
				}
			} else if (newEdges[0][j][0] == 1) {
				newDist[newEdges[0][j][1]-1][V] = newEdges[0][j][2];
				for (int i = 0; i < V+1; i++) {
					int altDist = newDist[i][newEdges[0][j][1]-1]+newDist[newEdges[0][j][1]-1][V];
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
				int altDist = newDist[i][V]+newDist[V][j];
				newDist[i][j] = newDist[i][j] < altDist ? newDist[i][j] : altDist;
			}
		}
		
		//long step2Time = System.nanoTime();
		
		finalEndTime = System.nanoTime();
		finalTotalTime = finalEndTime-finalStartTime;
		/*finalTotalTime = finalEndTime-finalStartTime-((finalEndTime - step2Time)*5);
		System.out.println("allocTime is " + (allocTime - finalStartTime));
		System.out.println("copyTime is " + (copyTime - allocTime));
		System.out.println("fillTime is " + (fillTime - copyTime));
		System.out.println("step1Time is " + (step1Time - fillTime));
		System.out.println("step2Time is " + (step2Time - step1Time));
		System.out.println("nanoTime is " + ((finalEndTime - step2Time)*5));*/
		
		return newDist;	
	}

	void printSolution(int dist[][], int mSize) 
	{ 
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
		 * @param - args[0] is the adjacency list file name
		 * args[1] is the mode to run ("baseline" or "incremental") 
		 */
		String incAdjListFile = args[0];
		String mode = args[1];
		int n, m = 1;
		int dist[][] = {};
      	String str;
		String st[] = new String[3];
		ArrayList<Integer> input = new ArrayList<Integer>();
		
		// TBD - Read input from file
      	int graph[][] = { {0, 3, INF, 7}, 
						{8, 0, 2, INF}, 
						{5, INF, 0, 1}, 
						{2, INF, INF, 0}
						};
		
		int graph2[][] = { {0, 3, INF, 7, INF}, 
							{8, 0, 2, INF, 4}, 
							{5, INF, 0, 1, INF}, 
							{2, INF, INF, 0, 1},
					        {INF, INF, 5, INF, 0}
						};
		
		AllPairsShortestPathInc a = new AllPairsShortestPathInc(); 
		
		/*
		 *  Run baseline algorithm if mode is "baseline" or 
		 *  incremental algorithm if mode is "incremental"
		 */
		if(mode.equals("baseline")) {
			dist = a.floydWarshall(graph2);
			System.out.println("Total time taken for baseline algorithm is " + finalTotalTime);
		} else {
	      	/* 
	      	 * Reading file with info on new edges and weights (from new node)
	      	 * and constructing the adjacency list
	      	 */
	      	File file = new File(incAdjListFile); 
			
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			while ((str = br.readLine()) != null) {
				st = str.split(" ");
				for(int s=0; s<st.length; s++)
					input.add(Integer.parseInt(st[s]));
			}
			
			n = input.get(0);
			
			int adjList[][][] = new int[n][n][n];
			
			//System.out.println("n: " + n);
			
			while (m < input.size()) {
				//System.out.println("m: " + m);
				//System.out.println("input size: " + input.size());
				//for (int i = 0; i < n-2; i++) {
					for (int j = 0; j < n; j++) {
						//System.out.println("incrementing j...");
						for (int k = 0; k < 3; k++) {
							adjList[0][j][k] = input.get(m);
							m++;
							//System.out.println("adjList[0]["+j+"]["+k+"] = " + adjList[0][j][k]);
						}
					}
				//}
			}
	      	
			br.close();
	
			/* 
			 * Run Floyd Warshall baseline algorithm and
			 * get the shortest path distances for old graph
			 */
			int tempDist[][] = a.floydWarshall(graph);
					
			// Calling the incremental algorithm
			dist = a.incrementalAllPairsShortestPath(tempDist, adjList);
			System.out.println("Total time taken for incremental algorithm is " + finalTotalTime);
		}
		
		// Print the final matrix with the shortest distances
		a.printSolution(dist, dist.length);
	} 
}
