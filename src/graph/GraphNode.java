package graph;

import java.util.ArrayList;

public class GraphNode{
		
	int x;
	int y;
	private ArrayList<GraphNode> adj = new ArrayList<GraphNode>();
	
	public GraphNode() {}
	public GraphNode(int x, int y, ArrayList<GraphNode> adj) {
		this.x=x;
		this.y=y;
		this.adj=adj;
	}
	
	public void addAdj(GraphNode node) {
		adj.add(node);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public ArrayList<GraphNode> getAdj() {
		return adj;
	}
	
	
}