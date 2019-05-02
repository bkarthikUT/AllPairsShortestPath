package project.dijkstra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import project.dijkstra.Vertex.Color;

public class DijkstraIncremental {

	String outFileName = "DK";

	int noOfVertices;
	int noOfArcs;
	List<Vertex>[] adjList;
	List<Vertex>[] adjTransposeList;
	// List<Vertex>[] incList;
	Vertex[] vertices;
	Vertex[] finalSet = null;
	MinPriorityQueue queue = null;
	Vertex[][] distances;
	Vertex[] newSetOfVertices = null;
	// Vertex[][] incMatrix = null;

	long totalWritingTime = 0;
	long totalTime = 0;
	long incrementalDijkstraTime = 0;
	long startTime=0;
	long completeTime=0;

	public static void main(String args[]) {

		 String[] inputPaths = new String[] { "./testcases/DensityGraphs/",
		 "./testcases/SparseGraphs/" };
		//String[] inputPaths = new String[] { "./testcases/SparseGraphs/" };
		String[] densityGraphs = new String[] { "SocialNetwork-419", "WebPages-589", "YeastProteinInteraction-985",
				"PsgAirTraffic-1148", "P2PKazaa-3403", "P2PWeb-6049", "InternetRouters-10900" };

//		String[] densityGraphs = new String[] { "SocialNetwork-419", "WebPages-589", "YeastProteinInteraction-985",
//				"PsgAirTraffic-1148", "P2PKazaa-3403", "P2PWeb-6049", "InternetRouters-10900" };
//		 String[] sparseGraphs = new String[] { "SYN_5", "NY_50", "BAY_1000",
//		 "NY_5000", "COL_10000" };
		String[] sparseGraphs = new String[] { "SYN_5" };
		String inputFile = "graph";
		String incFile = "inc";
		String outputFile = "DK.out";
		for (String inputPath : inputPaths) {
			if (inputPath.equals("./testcases/DensityGraphs/")) {
				for (String densityGraph : densityGraphs) {
					String inputFileName = inputPath + densityGraph + "/" + inputFile;
					String outputFileName = inputPath + densityGraph + "/" + outputFile;
					String incFileName = inputPath + densityGraph + "/" + incFile;
					System.out.println("Processing " + inputFileName);
					// System.out.println(outputFileName);

					/**
					 * File file = new File(outputFileName); boolean deleted = file.delete();
					 * System.out.println("Deleted " + outputFileName + " >>>" + deleted);
					 **/
					long startTime = System.nanoTime();
					DijkstraIncremental dijkstra = new DijkstraIncremental();
					dijkstra.doDikjstra(inputFileName, outputFileName);
					
					long incrementalStart = System.nanoTime();
					dijkstra.doIncremental(incFileName);
					long incrementalEnd = System.nanoTime();
					
					dijkstra.incrementalDijkstraTime = incrementalEnd - incrementalStart;
					long completedTime = System.nanoTime();
					System.out.println(" Running time of sequential dijkstra for: " + inputFileName + " is "
							+ (completedTime - startTime ));

					try {
						dijkstra.writeShortestPath(outputFileName);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
			} else {
				for (String sparseGraph : sparseGraphs) {
					String inputFileName = inputPath + sparseGraph + "/" + inputFile;
					String incFileName = inputPath + sparseGraph + "/" + incFile;
					String outputFileName = inputPath + sparseGraph + "/" + outputFile;
					System.out.println("Processing " + inputFileName);
					// System.out.println(outputFileName);

					/**
					 * File file = new File(outputFileName); boolean deleted = file.delete();
					 * System.out.println("Deleted " + outputFileName + " >>>" + deleted);
					 **/
					long startTime = System.nanoTime();
					DijkstraIncremental dijkstra = new DijkstraIncremental();
					dijkstra.doDikjstra(inputFileName, outputFileName);
					long incrementalStart = System.nanoTime();
					dijkstra.doIncremental(incFileName);
					long incrementalEnd = System.nanoTime();
					
					dijkstra.incrementalDijkstraTime = incrementalEnd - incrementalStart;
					long completedTime = System.nanoTime();
					System.out.println(" Running time of sequential dijkstra for: " + inputFileName + " is "
							+ (completedTime - startTime ));

					try {
						dijkstra.writeShortestPath(outputFileName);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	private void doIncremental(String incFileName) {
		Input input = new Input(incFileName);
		String[] rows = input.read();
		HashMap<Integer, ArrayList<Vertex>> newAdjList = new HashMap<>();
		List<Vertex> transposeList = new ArrayList<>();
		Vertex[] affectedVertices = new Vertex[noOfVertices + 1];
		newSetOfVertices = new Vertex[noOfVertices + 1];
		// incMatrix = new Vertex[noOfVertices+1][noOfVertices+1];
		for (int i = 0; i < noOfVertices + 1; i++) {
			Vertex temp = new Vertex(i);
			newSetOfVertices[i] = temp;
		}
		Vertex newVertex = null;
		for (String row : rows) {
			if (row != null) {
				String[] temp = row.split(" ");
				if ("a".equals(temp[0])) {
					int x = Integer.parseInt(temp[1]);
					int y = Integer.parseInt(temp[2]);
					int weight = 1;
					if (temp.length == 4) {
						weight = Integer.parseInt(temp[3]);
					}
					if (x > noOfVertices) { // outgoing edges from new node will be its adjacency list
						newVertex = new Vertex(x - 1);
						newSetOfVertices[newVertex.vertexID] = newVertex;
						Vertex neighbor = new Vertex(y - 1, weight);
						ArrayList<Vertex> neighbors = newAdjList.get(x - 1);
						if (neighbors == null) {
							neighbors = new ArrayList<>();
						}
						neighbors.add(neighbor);
						newAdjList.put(new Integer(x - 1), neighbors);
						// if(adjTransposeList[neighbor.vertexID] == null) {
						// adjTransposeList[neighbor.vertexID] = new ArrayList<>();
						// }
						// adjTransposeList[neighbor.vertexID].add(newVertex);
					} else {
						Vertex neighbor = new Vertex(y - 1, weight);
						if (newSetOfVertices[y - 1] == null) {
							newSetOfVertices[y - 1] = new Vertex(y - 1);
						}

						ArrayList<Vertex> neighbors = newAdjList.get(x - 1);
						if (neighbors == null) {
							neighbors = new ArrayList<>();
						}
						neighbors.add(neighbor);
						newAdjList.put(new Integer(x - 1), neighbors);

						transposeList.add(new Vertex(x - 1, weight));

					}

				}
			}
		}

		// for each of the vertex that has an edge to z
		// start from z and navigate the transpose by doing dijkstra to find all
		// affected nodes
		// first add the nodes that has incoming links to z
		// extract min and get nodes that have incoming links to the extract min node

		for (int i = 0; i < noOfVertices + 1; i++) {
			Vertex temp = new Vertex(i);
			newSetOfVertices[i] = temp;
		}
		MinPriorityQueue minPriorityQueue = new MinPriorityQueue(1 + noOfVertices);
		Vertex source = newSetOfVertices[noOfVertices];
		minPriorityQueue.insert(source, 0);
		while (!minPriorityQueue.isEmpty()) {
			Vertex u = minPriorityQueue.extractMin();
			u.color = Color.BLACK;
			affectedVertices[u.vertexID] = u;
			if (transposeList == null) {
				transposeList = adjTransposeList[u.vertexID];
			}
			for (int k = 0; k < transposeList.size(); k++) {
				Vertex incomingToNew = transposeList.get(k);

				Vertex v = newSetOfVertices[incomingToNew.vertexID];
				if (v.color == Color.WHITE) {
					int newWeight = distances[incomingToNew.vertexID][u.vertexID] == null
							? incomingToNew.originalEdgeWeight
							: distances[incomingToNew.vertexID][u.vertexID].weightFromSource
									+ distances[u.vertexID][source.vertexID].weightFromSource;
					if (distances[v.vertexID][source.vertexID] == null
							|| newWeight < distances[v.vertexID][source.vertexID].weightFromSource) {
						Vertex distVertex = new Vertex(source.vertexID);
						distVertex.parent = v.vertexID;
						distVertex.weightFromSource = newWeight;
						distances[v.vertexID][source.vertexID] = distVertex;
						v.color = Color.GREY;
						minPriorityQueue.insert(v, newWeight);

					} else {
						System.out.println("Error");
					}

				} else if (vertices[incomingToNew.vertexID].color == Color.GREY) {

					int newWeight = distances[incomingToNew.vertexID][u.vertexID].weightFromSource
							+ distances[u.vertexID][source.vertexID].weightFromSource;
					if (newWeight < distances[v.vertexID][source.vertexID].weightFromSource) {
						Vertex distVertex = new Vertex(source.vertexID);
						distVertex.parent = v.vertexID;
						distVertex.weightFromSource = newWeight;
						distances[v.vertexID][source.vertexID] = distVertex;

						minPriorityQueue.decreaseKey(v.queueIndex, newWeight);
					}

				}
			}
			transposeList = null;
		}

//		for (int i = 0; i < affectedVertices.length; i++) {
//			if (affectedVertices[i] != null) {
//				System.out.println(affectedVertices[i].vertexID);
//			}
//
//		}

		distances[source.vertexID][source.vertexID] = new Vertex(source.vertexID);
		distances[source.vertexID][source.vertexID].weightFromSource = 0;

		List<Vertex> neighbors = newAdjList.get(new Integer(source.vertexID));
		for (Vertex x : neighbors) {
			// Vertex currentInfo = distances[source.vertexID][x.vertexID];
			// int newWeight =
			// if (currentInfo == null || currentInfo.weightFromSource > potentialValue) {
			Vertex tempVertex = new Vertex(x.vertexID);
			distances[source.vertexID][x.vertexID] = tempVertex;
			tempVertex.weightFromSource = x.originalEdgeWeight;
			tempVertex.parent = source.vertexID;
			// }
		}
		for (Vertex affectedVertex : affectedVertices) {
			minPriorityQueue = new MinPriorityQueue(noOfVertices);
			if (affectedVertex == null) {
				continue;
			}

			for (int i = 0; i < noOfVertices + 1; i++) {
				Vertex temp = new Vertex(i);
				newSetOfVertices[i] = temp;
			}
			for (Vertex x : newAdjList.get(new Integer(source.vertexID))) {
				minPriorityQueue.insert(newSetOfVertices[x.vertexID],
						distances[source.vertexID][x.vertexID].weightFromSource);
			}

			while (!minPriorityQueue.isEmpty()) {
				Vertex u1 = minPriorityQueue.extractMin();
				u1.color = Color.BLACK;
				if(distances[source.vertexID][u1.vertexID] == null) {
					continue;
				}
				neighbors = adjList[u1.vertexID];
				for (Vertex x : neighbors) {
					Vertex currentInfo = distances[affectedVertex.vertexID][x.vertexID];
					int potentialValue = distances[affectedVertex.vertexID][source.vertexID].weightFromSource
							+ distances[source.vertexID][u1.vertexID].weightFromSource + x.originalEdgeWeight;
					if (currentInfo == null || currentInfo.weightFromSource > potentialValue) {
						Vertex tempVertex = new Vertex(x.vertexID);
						distances[affectedVertex.vertexID][x.vertexID] = tempVertex;
						tempVertex.weightFromSource = potentialValue;
						tempVertex.parent = u1.vertexID;
						if (newSetOfVertices[x.vertexID].color == Color.WHITE) {
							newSetOfVertices[x.vertexID].color = Color.GREY;
							minPriorityQueue.insert(newSetOfVertices[x.vertexID], potentialValue);
						} else {
							minPriorityQueue.decreaseKey(newSetOfVertices[x.vertexID].queueIndex, potentialValue);
						}
					}
				}
				// }
			}

		}
		//printMatrix();

	}

	private void printMatrix() {
		for (int i = 0; i < noOfVertices + 1; i++) {
			for (int j = 0; j < noOfVertices + 1; j++) {
				if (distances[i][j] != null) {
					System.out.print(distances[i][j].weightFromSource + " ");
				} else {
					System.out.print("* ");
				}

			}
			System.out.print("\n");
		}

	}

	private void doDikjstra(String inputFileName, String outputFileName) {

		createGraph(inputFileName);
		// long inititializeComplete = System.nanoTime();;
		// System.out.println("Initialization time " + (inititializeComplete -
		// startTime));
		// dijkstra.printGraph();
		allPointShortestPaths(outputFileName);
	}

	private void allPointShortestPaths(String outputFile) {
		for (int i = 0; i < noOfVertices; i++) {
			// System.out.println("---------- Processing--------"+i);
			this.finalSet = new Vertex[noOfVertices];
			this.queue = new MinPriorityQueue(noOfVertices);
			for (int j = 0; j < noOfVertices; j++) {
				if (vertices[j] != null) {
					this.vertices[j].color = Color.WHITE;
					this.vertices[j].weightFromSource = Integer.MAX_VALUE;
				} else {
					this.vertices[j] = new Vertex(j);
					adjList[j] = new ArrayList<Vertex>();
				}
			}
			if (vertices[i] != null) {
				findShortestPath(i);
			}

		}
	}

	private void findShortestPath(int source) {
		Vertex sourceVertex = vertices[source];
		sourceVertex.color = Color.GREY;

		queue.insert(sourceVertex, 0);
		while (!queue.isEmpty()) {
			Vertex u = queue.extractMin();
			u.color = Color.BLACK;
			finalSet[u.vertexID] = u;
			Vertex tempVertex = new Vertex(u.vertexID);
			tempVertex.parent = u.parent;
			tempVertex.weightFromSource = u.weightFromSource;
			distances[source][u.vertexID] = tempVertex;
			List<Vertex> neighbors = adjList[u.vertexID];
			if (neighbors != null) {
				for (Vertex v : neighbors) {

					relaxVertex(u, v);
				}
			}
		}

	}

	private void writeShortestPath(String outputFile) throws IOException {

		long startTime = System.nanoTime();

		Output output = new Output(outputFile);
		StringBuilder sb = new StringBuilder();
		sb.append("Shortest path solution is:");
		sb.append("\n");
		output.write(sb.toString());
		for (int i = 0; i < noOfVertices+1; i++) {
			sb = new StringBuilder();
			for (int j = 0; j < noOfVertices+1; j++) {
				if (distances[i][j] == null) {
					sb.append("INF ");
				} else {
					sb.append(distances[i][j].weightFromSource + " ");
				}
			}
			sb.append("\n");
			output.write(sb.toString());

		}
		sb = new StringBuilder();
		sb.append("Total time taken for the incremental algorithm is:"+ incrementalDijkstraTime);
		output.write(sb.toString());

		output.close();

		long endTime = System.nanoTime();

		long writingTime = endTime - startTime;

		totalWritingTime = totalWritingTime + writingTime;

	}

	private void printShortestPath() {
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < noOfVertices; j++) {
			if (finalSet[j] == null) {
				sb.append(" *");
			} else {
				sb.append(" " + finalSet[j].weightFromSource);
			}
		}
		System.out.println(sb.toString());
	}

	private void relaxVertex(Vertex u, Vertex neighbor) {
		Vertex v = vertices[neighbor.vertexID];
		if (v.color == Color.WHITE) {
			int newWeight = u.weightFromSource + neighbor.originalEdgeWeight;
			if (newWeight < v.weightFromSource) {
				v.color = Color.GREY;
				v.parent = u.vertexID;
				queue.insert(v, newWeight);
			} else {
				System.out.println("Error");
			}

		} else if (vertices[neighbor.vertexID].color == Color.GREY) {

			int newWeight = u.weightFromSource + neighbor.originalEdgeWeight;
			if (newWeight < v.weightFromSource) {
				v.parent = u.vertexID;
				queue.decreaseKey(v.queueIndex, newWeight);
			}

		}

	}

	private void printGraph() {
		for (int i = 0; i < noOfVertices; i++) {
			if (adjList[i] != null) {
				System.out.println("Number of neighbors of " + (i + 1) + " is " + adjList[i].size());
			} else {
				System.out.println(" Vertex " + (i + 1) + " not in graph or has no outgoing edges.");
			}
		}

	}

	private void initializeGraph(String fileName) {
		System.out.println(fileName);
		Input input = new Input(fileName);
		String[] rows = input.read();
		String[] temp = rows[0].split(" ");
		noOfVertices = Integer.parseInt(temp[1]);
		noOfArcs = Integer.parseInt(temp[2]);
		adjList = new ArrayList[noOfVertices];
		vertices = new Vertex[noOfVertices];
		// path = new ArrayList<>();

		rows[0] = null;
		for (String row : rows) {
			if (row != null) {
				// System.out.println(row);
				temp = row.split(" ");
				int x = Integer.parseInt(temp[0]);
				int y = Integer.parseInt(temp[1]);
				int weight = 1;
				if (temp.length == 3) {
					weight = Integer.parseInt(temp[2]);
				}
				List<Vertex> neighbors = adjList[x - 1];
				if (neighbors == null) {
					neighbors = new ArrayList<>();
					Vertex newVertex = new Vertex(x - 1);
					vertices[x - 1] = newVertex;
				}
				Vertex neighbor = new Vertex(y - 1, weight);
				neighbors.add(neighbor);
				adjList[x - 1] = neighbors;
			}

		}

	}

	private void createGraph(String fileName) {
		// System.out.println(fileName);
		Input input = new Input(fileName);
		String[] rows = input.read();
		for (String row : rows) {
			if (row != null) {
				String[] temp = row.split(" ");
				if ("p".equals(temp[0])) {
					noOfVertices = Integer.parseInt(temp[2]);
					noOfArcs = Integer.parseInt(temp[3]);
					adjList = new ArrayList[noOfVertices];
					vertices = new Vertex[noOfVertices];
					distances = new Vertex[noOfVertices + 1][noOfVertices + 1];
					adjTransposeList = new ArrayList[noOfVertices + 1];
				}
				if ("a".equals(temp[0])) {
					int x = Integer.parseInt(temp[1]);
					int y = Integer.parseInt(temp[2]);
					int weight = 1;
					if (temp.length == 4) {
						weight = Integer.parseInt(temp[3]);
					}
					List<Vertex> neighbors = adjList[x - 1];
					if (neighbors == null) {
						neighbors = new ArrayList<>();
						Vertex newVertex = new Vertex(x - 1);
						vertices[x - 1] = newVertex;
					}
					Vertex neighbor = new Vertex(y - 1, weight);
					neighbors.add(neighbor);
					adjList[x - 1] = neighbors;

					List<Vertex> transpose = adjTransposeList[y - 1];
					if (transpose == null) {
						transpose = new ArrayList<>();
					}
					Vertex neighborTranspose = new Vertex(x - 1, weight);
					transpose.add(neighborTranspose);
					adjTransposeList[y - 1] = transpose;

				}
			}

		}

	}

}
