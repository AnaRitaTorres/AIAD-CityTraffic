package gui;

import uchicago.src.sim.engine.Stepable;

public class MyModelAgent implements Stepable {
	
	private int	id;
	
	public MyModelAgent(int id) {
		this.id = id;
	}
	
	public void step() {
		System.out.println(id + " Hello World!");
	}
	
}