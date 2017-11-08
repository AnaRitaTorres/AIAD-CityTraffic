package agents;

import jade.core.Agent;

@SuppressWarnings("serial")
public class VehicleAgent extends Agent{
	
	protected void setup() {
		System.out.println("Hello! Vehicle-Agent "+getAID().getName()+ "is ready.");
		
	}
	
	protected void takeDown(){
		// Printout a dismissal	message
		System.out.println("Vehicle-Agent "+getAID().getName()+ "terminating.");
	}
}