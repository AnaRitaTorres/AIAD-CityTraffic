package graph;

import java.awt.Color;
import java.util.ArrayList;

public class Graph{
	
	private ArrayList<GraphNode> nodes;
	
	
	public Graph(ArrayList<GraphNode> nodes){
		
		this.nodes=nodes;
	}
	
	public void connectVertical(ArrayList<GraphNode> nodes) {
		for(int i=1; i < nodes.size(); i++) {
			GraphNode n = nodes.get(i);
			GraphNode n1 = nodes.get(i-1);
			if(n.getX() == n1.getX()) {
				n.addAdj(n1);
			}
		}
	}
	
	public void connectStreetY(ArrayList<GraphNode> nodes, double y) {
		
		ArrayList<GraphNode> aux = new ArrayList<GraphNode>();
		
		for(int i=0; i < nodes.size(); i++) {
			GraphNode n = nodes.get(i);
			if(n.getY()==y) {
				
				aux.add(n);
			}
		}
		
		for(int i=1; i < aux.size(); i++) {
			GraphNode n1 = aux.get(i);
			GraphNode n2 = aux.get(i-1);
			n2.addAdj(n1);
		}
	
	}

	public void connectToFrom(ArrayList<GraphNode> nodes,double xTo, double xFrom, double y) {
		
		ArrayList<GraphNode> aux = new ArrayList<GraphNode>();
		ArrayList<GraphNode> aux1 = new ArrayList<GraphNode>();
		
		for(int i=0; i < nodes.size(); i++) {
			GraphNode n = nodes.get(i);
			if(n.getY()==y) {
				aux.add(n);
			}
		}
		
		for(int i=0; i < aux.size(); i++) {
			GraphNode n1 = aux.get(i);
			
			if(n1.getX() <= xTo && n1.getX() >= xFrom) {
				aux1.add(n1);
			}
		}
		
		for(int i=1; i < aux1.size(); i++) {
			GraphNode n1 = aux1.get(i);
			GraphNode n2 = aux1.get(i-1);
			n2.addAdj(n1);
		}
	
	}
	
	public void connect2Nodes(ArrayList<GraphNode> nodes,double xTo, double xFrom,double yTo, double yFrom) {
			
		GraphNode in = new GraphNode();
		GraphNode out = new GraphNode();
		for(int i=0; i < nodes.size(); i++) {
			GraphNode n = nodes.get(i);
					
			if(n.getX() == xFrom && n.getY() == yFrom){
				in = n;
			}
			
						
			if(n.getX() == xTo && n.getY() == yTo) {
				out = n;
			}
		}
		
		in.addAdj(out);
	}
}