package graph;

import java.util.ArrayList;

public class GraphNode {
	private int data;
	private ArrayList<GraphNode> adjacent;
	
	public GraphNode(int data) {
		this.data = data;
		adjacent = new ArrayList<>();
	}
	
	public void addAdjacent(GraphNode node) {
		adjacent.add(node);
	}
}