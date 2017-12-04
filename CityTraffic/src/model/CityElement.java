package model;

import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.engine.Stepable;
import uchicago.src.sim.space.Object2DGrid;
import uchicago.src.sim.gui.SimGraphics;

public class CityElement implements Drawable,Stepable{
	
	public int x, y;
	protected static Object2DGrid space;
	
	public CityElement(int x, int y, Object2DGrid s) {
		space = s;
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void draw(SimGraphics g) {}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}
		
	public void step() {
	
	}
}
