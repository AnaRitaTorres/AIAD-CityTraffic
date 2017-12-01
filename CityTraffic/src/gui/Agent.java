package gui;

import uchicago.src.sim.engine.Stepable;

public class Agent implements Stepable {
	private int id;

	public Agent(int id) {
		this.id = id;
	}

	public void step() {
		System.out.println(id + " Hello World!");
	}

}
