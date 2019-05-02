package project.dijkstra;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.dijkstra.Vertex.Color;


public class Dijkstra {

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
	long startTime = 0;
	long completeTime = 0;

	// public static void main(String args[]) {
	//
	//
	// String[] inputPaths = new String[] { "./testcases/DensityGraphs/",
	// "./testcases/SparseGraphs/" };
	// // String[] inputPaths = new String[] { "./testcases/SparseGraphs/" };
	// String[] densityGraphs = new String[] { "SocialNetwork-419", "WebPages-589",
	// "YeastProteinInteraction-985",
	// "PsgAirTraffic-1148", "P2PKazaa-3403", "P2PWeb-6049", "InternetRouters-10900"
	// };
	//
	// // String[] densityGraphs = new String[] { "SocialNetwork-419",
	// "WebPages-589",
	// // "YeastProteinInteraction-985",
	// // "PsgAirTraffic-1148", "P2PKazaa-3403", "P2PWeb-6049",
	// "InternetRouters-10900"
	// // };
	// // String[] sparseGraphs = new String[] { "SYN_5", "NY_50", "BAY_1000",
	// // "NY_5000", "COL_10000" };
	// String[] sparseGraphs = new String[] { "SYN_5" };
	// String inputFile = "graph";
	// String incFile = "inc";
	// String outputFile = "DK.out";
	// for (String inputPath : inputPaths) {
	// if (inputPath.equals("./testcases/DensityGraphs/")) {
	// for (String densityGraph : densityGraphs) {
	// String inputFileName = inputPath + densityGraph + "/" + inputFile;
	// String outputFileName = inputPath + densityGraph + "/" + outputFile;
	// String incFileName = inputPath + densityGraph + "/" + incFile;
	// System.out.println("Processing " + inputFileName);
	// // System.out.println(outputFileName);
	//
	// /**
	// * File file = new File(outputFileName); boolean deleted = file.delete();
	// * System.out.println("Deleted " + outputFileName + " >>>" + deleted);
	// **/
	// long startTime = System.nanoTime();
	// Dijkstra dijkstra = new Dijkstra();
	// dijkstra.doDikjstra(inputFileName, outputFileName);
	//
	// long incrementalStart = System.nanoTime();
	// dijkstra.doIncremental(inputFileName, incFileName);
	// long incrementalEnd = System.nanoTime();
	//
	// dijkstra.incrementalDijkstraTime = incrementalEnd - incrementalStart;
	// long completedTime = System.nanoTime();
	// System.out.println(" Running time of sequential dijkstra for: " +
	// inputFileName + " is "
	// + (completedTime - startTime));
	//
	// try {
	// dijkstra.writeShortestPath(outputFileName);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// break;
	// }
	// } else {
	// for (String sparseGraph : sparseGraphs) {
	// String inputFileName = inputPath + sparseGraph + "/" + inputFile;
	// String incFileName = inputPath + sparseGraph + "/" + incFile;
	// String outputFileName = inputPath + sparseGraph + "/" + outputFile;
	// System.out.println("Processing " + inputFileName);
	//
	// long startTime = System.nanoTime();
	// Dijkstra dijkstra = new Dijkstra();
	// long incrementalStart = System.nanoTime();
	// dijkstra.doIncremental(inputFileName, incFileName);
	// long incrementalEnd = System.nanoTime();
	//
	// dijkstra.incrementalDijkstraTime = incrementalEnd - incrementalStart;
	// long completedTime = System.nanoTime();
	// System.out.println(" Running time of sequential dijkstra for: " +
	// inputFileName + " is "
	// + (completedTime - startTime));
	//
	// try {
	// dijkstra.writeShortestPath(outputFileName);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	// }
	//
	// }

	public static void main(String args[]) {

		String inputFileName = args[0];
		String outputFileName = args[2];
		String incFileName = args[1];
		System.out.println("Processing " + inputFileName);
		// System.out.println(outputFileName);

		Dijkstra dijkstra = new Dijkstra();

		long incrementalStart = System.nanoTime();
		dijkstra.doIncremental(inputFileName, incFileName);
		long incrementalEnd = System.nanoTime();

		dijkstra.incrementalDijkstraTime = incrementalEnd - incrementalStart;
		System.out.println(" Running time of sequential dijkstra for: " + inputFileName + " is "
				+ (dijkstra.incrementalDijkstraTime));

		try {
			dijkstra.writeShortestPath(outputFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void doIncremental(String inputFileName, String incFileName) {
		createGraphForIncremental(inputFileName, incFileName);
		allPointShortestPaths();

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
		allPointShortestPaths();
	}

	private void allPointShortestPaths() {
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
		for (int i = 0; i < noOfVertices; i++) {
			sb = new StringBuilder();
			for (int j = 0; j < noOfVertices; j++) {
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
		sb.append("Total time taken for the Dijkstra algorithm is:" + incrementalDijkstraTime);
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

	private void createGraphForIncremental(String fileName, String incFileName) {
		// System.out.println(fileName);
		Input input = new Input(fileName);
		String[] rows = input.read();
		for (String row : rows) {
			if (row != null) {
				String[] temp = row.split(" ");
				if ("p".equals(temp[0])) {
					noOfVertices = Integer.parseInt(temp[2]) + 1;
					noOfArcs = Integer.parseInt(temp[3]);
					adjList = new ArrayList[noOfVertices];
					vertices = new Vertex[noOfVertices + 1];
					distances = new Vertex[noOfVertices][noOfVertices];
					adjTransposeList = new ArrayList[noOfVertices];
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

				}
			}

		}

		input = new Input(incFileName);
		rows = input.read();
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
	}
}
