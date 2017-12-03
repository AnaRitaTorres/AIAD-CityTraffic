package gui;

import uchicago.src.sim.space.Object2DGrid;

public class GridSpace{
	
	private Object2DGrid streetSpace;
	private Object2DGrid agentSpace;
	
	public GridSpace(int xSize,int ySize) {
		
		streetSpace = new Object2DGrid(xSize,ySize);
		agentSpace = new Object2DGrid(xSize,ySize);
		
		for(int i=0; i < xSize; i++) {
			for(int j=0; j < ySize; j++) {
				streetSpace.putObjectAt(i, j, new Integer(0));
			}
		}
		
	}
	
	public Object2DGrid getCurrentStreetSpace() {
		return streetSpace;
	}
	
	public Object2DGrid getCurrentAgentSpace() {
		return agentSpace;
	}
	
	public void removeAgentAt(int x, int y) {
		agentSpace.putObjectAt(x, y, null);
	}
	
	public boolean isCellOccupied(int x, int y) {
		boolean occupied = false;
		
		if(agentSpace.getObjectAt(x,y)!= null) {
			occupied = true;
		}
		
		return occupied;
	}
}