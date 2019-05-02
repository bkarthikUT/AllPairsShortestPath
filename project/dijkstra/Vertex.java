package project.dijkstra;




public class Vertex  { 
	public enum Color  {WHITE, BLACK, GREY};
	public int vertexID; //not source
	public int weightFromSource = Integer.MAX_VALUE; //weight from source
	public int originalEdgeWeight;
	public Integer parent = null;
	public Color color = Color.WHITE;
	public int queueIndex = Integer.MAX_VALUE;

	public Vertex(int node) 
	{ 
		this.vertexID = node; 
	} 

	public Vertex(int node, int cost) 
	{ 
		this.vertexID = node; 
		this.originalEdgeWeight = cost; 
	} 

}
