package graph;

import java.awt.Color;

import uchicago.src.sim.network.DefaultEdge;
import uchicago.src.sim.gui.DrawableEdge;
import uchicago.src.sim.gui.SimGraphics;
import uchicago.src.sim.network.Node;

public class MyEdge extends DefaultEdge implements DrawableEdge {
	
	private Color color;
	
	public MyEdge() { }

    public MyEdge(Node from, Node to) {
        super(from, to, "");
    }
    
    public MyEdge(Node from, Node to, Color color) {
        super(from, to, "");
        this.color=color;
    }
	
    public void setColor(Color c) {
        color = c;
    }

    public Color getColor() {
        return color;
    }
    
	public void draw(SimGraphics g, int fromX, int toX, int fromY, int toY) {
		g.drawDirectedLink(color, fromX, toX, fromY, toY);
	}
	 
	 
	 
}