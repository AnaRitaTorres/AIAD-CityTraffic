package graph;

import java.util.ArrayList;

public class Graph{
	
	private ArrayList<GraphNode> nodes;
	
	
	public Graph(ArrayList<GraphNode> nodes){
		
		this.nodes=nodes;
	}


	public ArrayList<GraphNode> getNodes() {
		return nodes;
	}
	
}