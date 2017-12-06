package graph;

import java.awt.Color;

import uchicago.src.sim.network.DefaultDrawableNode;
import uchicago.src.sim.gui.NetworkDrawable;
import uchicago.src.sim.network.DefaultNode;
import uchicago.src.sim.network.Edge;

import graph.MyEdge;

public class MyNode extends DefaultDrawableNode {
	
	public MyNode() {}	
	public MyNode(int xSize, int ySize,NetworkDrawable drawable, Color color) {
		super(drawable);
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
	
	
	
	  
}