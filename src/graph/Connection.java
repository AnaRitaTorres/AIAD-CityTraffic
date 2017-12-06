package graph;

import java.awt.Color;
import java.util.ArrayList;

public class Connection {
	
	public Connection() {}
	
	public void connectVertical(ArrayList<MyNode> nodes) {
		for(int i=1; i < nodes.size(); i++) {
			MyNode n = nodes.get(i);
			MyNode n1 = nodes.get(i-1);
			if(n.getX() == n1.getX()) {
				n1.makeEdgeToFrom(n, 5, Color.magenta);
			}
		}
	}
	
	public void connectStreetY(ArrayList<MyNode> nodes, double y) {
		
		ArrayList<MyNode> aux = new ArrayList<MyNode>();
		
		for(int i=0; i < nodes.size(); i++) {
			MyNode n = nodes.get(i);
			if(n.getY()==y) {
				
				aux.add(n);
			}
		}
		
		for(int i=1; i < aux.size(); i++) {
			MyNode n1 = aux.get(i);
			MyNode n2 = aux.get(i-1);
			n2.makeEdgeToFrom(n1, 5, Color.magenta);
		}
	
	}
	
	public void connectToFrom(ArrayList<MyNode> nodes,double xTo, double xFrom, double y) {
		
		ArrayList<MyNode> aux = new ArrayList<MyNode>();
		ArrayList<MyNode> aux1 = new ArrayList<MyNode>();
		
		for(int i=0; i < nodes.size(); i++) {
			MyNode n = nodes.get(i);
			if(n.getY()==y) {
				aux.add(n);
			}
		}
		
		for(int i=0; i < aux.size(); i++) {
			MyNode n1 = aux.get(i);
			
			if(n1.getX() <= xTo && n1.getX() >= xFrom) {
				aux1.add(n1);
			}
		}
		
		for(int i=1; i < aux1.size(); i++) {
			MyNode n1 = aux1.get(i);
			MyNode n2 = aux1.get(i-1);
			n2.makeEdgeToFrom(n1, 5, Color.magenta);
		}
	
	}
	
	public void connect2Nodes(ArrayList<MyNode> nodes,double xTo, double xFrom,double yTo, double yFrom) {
			
		MyNode in = new MyNode();
		MyNode out = new MyNode();
		for(int i=0; i < nodes.size(); i++) {
			MyNode n = nodes.get(i);
					
			if(n.getX() == xFrom && n.getY() == yFrom){
				in = n;
			}
			
						
			if(n.getX() == xTo && n.getY() == yTo) {
				out = n;
			}
		}
		
		in.makeEdgeToFrom(out, 5, Color.magenta);
	}
}