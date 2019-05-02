package project.dijkstra;


public class MinPriorityQueue {

	private int heapSize=0;
	Vertex[] minPriorityQueue = null;
	//private int defaultLength = 10;
	
	public MinPriorityQueue(int noOfVertices) {
		minPriorityQueue = new Vertex[noOfVertices];
	}
	
	public void insert(Vertex vertex, int newWeight) {

		vertex.queueIndex = heapSize;
		minPriorityQueue[heapSize] = vertex;
		decreaseKey(heapSize, newWeight);
		heapSize++;
	}

	public boolean isEmpty() {
		return heapSize == 0;
	}
	

	private void minHeapify(int index) {
		//System.out.println("Index:" + index + " ,value:" + vertices[index].weightFromSource);
		int l = left(index);
		int r = right(index);
		int smallest;
		if (l < heapSize && minPriorityQueue[l].weightFromSource < minPriorityQueue[index].weightFromSource) {
			smallest = l;
		} else {
			smallest = index;
		}
		if (r < heapSize && minPriorityQueue[r].weightFromSource < minPriorityQueue[smallest].weightFromSource) {
			smallest = r;
		}
		//System.out.println("smallest " + smallest + " ,index, " + index + " value:" + vertices[index].weightFromSource);
		if (smallest != index) {
			Vertex tempVertex = minPriorityQueue[index];
			Vertex smallestV = minPriorityQueue[smallest];
			smallestV.queueIndex = index;
			minPriorityQueue[index] = smallestV;
			tempVertex.queueIndex = smallest;
			minPriorityQueue[smallest] = tempVertex;
			minHeapify(smallest);
		}
	}

	private int left(int index) {
		return index * 2 + 1;
	}

	private int right(int index) {
		return index * 2 + 2;
	}
	
	private int parent(int index) {
		int parent = (index-1)/2;
		
		return parent;
	}

	public Vertex extractMin() {
		if (heapSize < 0) {
			throw new RuntimeException("Heap Underflow");
		}
		Vertex min = minPriorityQueue[0];
		Vertex max = minPriorityQueue[heapSize - 1];
		max.queueIndex = 0;
		minPriorityQueue[0] = max;
		heapSize = heapSize -1;
		minHeapify(0);

		return min;
	}

	public void decreaseKey(int index, int newWeight) {
		if (minPriorityQueue[index].weightFromSource < newWeight) {
			throw new RuntimeException("New value larger than old");
		}
		
		minPriorityQueue[index].weightFromSource = newWeight;
		int parent = parent(index);
		while(index >0 && minPriorityQueue[parent].weightFromSource > minPriorityQueue[index].weightFromSource) {
			Vertex temp = minPriorityQueue[index];
			minPriorityQueue[index] = minPriorityQueue[parent];
			minPriorityQueue[index] .queueIndex = index;
			minPriorityQueue[parent] = temp;
			temp.queueIndex = parent;
			index = parent;
			parent = parent(index);
		}
	}

	public static void main(String args[]) {
		int n = 5;
		Vertex[] vertices = new Vertex[n];
		int j = 1;
		for (int i = n; i > 0; i--) {
			Vertex vertex = new Vertex(j);
			vertex.weightFromSource = j*2;
			j++;
			vertices[i - 1] = vertex;
		}

		MinPriorityQueue pqueue = new MinPriorityQueue(n);
		// pqueue.addAll(vertices);
		// pqueue.decreaseKey(4, 5);
		//
		// System.out.println(pqueue.extractMin().weightFromSource);
		// System.out.println(pqueue.extractMin().weightFromSource);
		//		System.out.println(pqueue.extractMin().weightFromSource);
//		System.out.println(pqueue.extractMin().weightFromSource);
//		System.out.println(pqueue.extractMin().weightFromSource);
	}
}
