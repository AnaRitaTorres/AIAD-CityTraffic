package graph;

import java.util.ArrayList;

public class GraphNode{
		
	int x;
	int y;
	private ArrayList<GraphNode> adj = new ArrayList<GraphNode>();
	
	public GraphNode() {}
	public GraphNode(int x, int y) {
		this.x=x;
		this.y=y;
		
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
	
	public void removeAdj(int x, int y) {
		if(this.adj!= null) {
			for(int i=0; i < this.adj.size(); i++) {
				if(this.adj.get(i).getX()==x && this.adj.get(i).getY()==y) {
					this.adj.remove(i);
				}
			}
		}
		
	}
	
}