package gui;

import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.engine.SimpleModel;

public class Model extends SimpleModel{
	
	private int numberOfAgents;
	
	public void MyModel() {
		
		name="My Model";
	}
	
	public void setup() {
		super.setup();
		numberOfAgents = 3;
		autoStep=true;
		shuffle=true;
		
	}
	
	public void buildModel() {
		for(int i=0; i<numberOfAgents; i++) {
			agentList.add(new MyModelAgent(i));
			}
	}
	
	protected void 	preStep() {
		System.out.println("Initiating step " + getTickCount());
	}
	
	protected void 	postStep() {
		System.out.println("Done step " + getTickCount());
	}
	

}