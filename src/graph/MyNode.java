package graph;

import java.awt.Color;
import java.util.ArrayList;

import uchicago.src.sim.network.DefaultDrawableNode;
import uchicago.src.sim.gui.NetworkDrawable;
import uchicago.src.sim.network.DefaultNode;
import uchicago.src.sim.network.Edge;

import graph.MyEdge;
import javafx.scene.Node;

public class MyNode extends DefaultDrawableNode {
	
	private Color color;
	public MyNode() {}	
	public MyNode(NetworkDrawable drawable,int xSize, int ySize) {
		super(drawable);
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
	
	public void removeConnection(int x, int y,ArrayList<MyNode> nodes) {
	    
		for(int i=0; i < nodes.size(); i++) {
			if(nodes.get(i).getX()== x && nodes.get(i).getY()==y) {
				removeEdgesTo(nodes.get(i));
				nodes.get(i).removeEdgesFrom(this);
				removeEdgesFrom(nodes.get(i));
				nodes.get(i).removeEdgesTo(this);
			}
		}
	   
	 }
	
	
	
	  
}