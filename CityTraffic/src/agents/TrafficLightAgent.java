package agents;

import jade.core.Agent;

@SuppressWarnings("serial")
public class TrafficLightAgent extends Agent{
	
	protected void setup() {
		System.out.println("Hello! Traffic-Agent "+getAID().getName()+" is ready.");
	}
	
	protected void takeDown(){
		// Printout a dismissal	message
		System.out.println("Traffic-Agent "+getAID().getName()+ "terminating.");
	}
}