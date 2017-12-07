package graph;

import java.awt.Color;

import uchicago.src.sim.network.DefaultDrawableNode;
import uchicago.src.sim.gui.NetworkDrawable;
import uchicago.src.sim.network.DefaultNode;
import uchicago.src.sim.network.Edge;

import graph.MyEdge;

public class MyNode extends DefaultDrawableNode {
	
	private Color color;
	private int x;
	private int y;
	public MyNode() {}	
	public MyNode(NetworkDrawable drawable, int xSize, int ySize) {
		super(drawable);
		this.x = xSize;
		this.y = ySize;
	}
	

    public void setColor(Color c) {
        color = c;
    }

    public Color getColor() {
        return color;
    }
	
	public void makeEdgeToFrom(DefaultNode node, int maxDegree, Color color) {
	    if ((! hasEdgeTo(node)) && getOutDegree() < maxDegree &&
		node.getOutDegree() < maxDegree) {
	      
	      Edge edge = new MyEdge(this, node, color);
	      addOutEdge(edge);
	      node.addInEdge(edge);
	      Edge otherEdge = new MyEdge(node, this, color);
	      node.addOutEdge(otherEdge);
	      addInEdge(otherEdge);
	    }
	  }
	
	public void removeConnection(MyNode node) {
	    
	    if (node != null) {
	      removeEdgesTo(node);
	      node.removeEdgesFrom(this);
	      removeEdgesFrom(node);
	      node.removeEdgesTo(this);
	    }
	 }
	
	public boolean equals(MyNode n){
		if(n.x == this.x && n.y == this.y){
			return true;
		}
		else{
			return false;
		}
	}
	
}