package graph;

import java.awt.Color;

import uchicago.src.sim.network.DefaultDrawableNode;
import uchicago.src.sim.gui.NetworkDrawable;
import uchicago.src.sim.network.DefaultNode;
import uchicago.src.sim.network.Edge;

import graph.MyEdge;

public class MyNode extends DefaultDrawableNode {
	
	private int xSize,ySize;
	
	public MyNode(int xSize, int ySize,NetworkDrawable drawable) {
		super(drawable);
		this.xSize = xSize;
		this.ySize = ySize;
	}
	
	public void makeEdgeToFrom(DefaultNode node, int xSize, int ySize, Color color) {
		
		if(!hasEdgeTo(node)) {
			
			Edge edge = new MyEdge(this,node,color);
			addOutEdge(edge);
			node.addInEdge(edge);
			Edge otherEdge = new MyEdge(node,this,color);
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
}