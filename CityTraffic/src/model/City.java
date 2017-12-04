package model;

import uchicago.src.sim.space.Object2DGrid;

public class City {
	
	private Object2DGrid city;
	
	public City(int xSize,int ySize) {
		city = new Object2DGrid(xSize,ySize);
		
		for(int i=0; i < xSize; i++) {
			for(int j=0; j < ySize; j++) {
				city.putObjectAt(i, j, 0);
			}
		}
	}
}
