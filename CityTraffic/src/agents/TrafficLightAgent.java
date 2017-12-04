package agents;


import java.util.ArrayList;

import sajas.core.Agent;
import sajas.core.behaviours.*;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class TrafficLightAgent extends Agent{
	
	private static int IDNumber=0;
	private int ID;
	private ArrayList<AID> receiverAgents;
	private AID receiverCar = null;
	private AID receiverRadio = null;
	
	
	public TrafficLightAgent(AID receiverCar, AID receiverRadio) {
		IDNumber++;
		ID=IDNumber;
		this.receiverCar = receiverCar;
		this.receiverRadio = receiverRadio;
	}
	
	public int getID() {
		return ID;
	}
	
	protected void setup() {
		System.out.println("Hello! Traffic-Agent "+getAID().getName()+" is ready.");
		
		addBehaviour(new OneShotBehaviour(){

			@Override
			public void action() {
				//Send msg to the other agent
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.setContent("send");
				
				/*System.out.println("size" + receiverAgents.size());		
				for(int i=0; i < receiverAgents.size(); i++) {
					
				}*/
			
				msg.addReceiver(receiverCar);
				msg.addReceiver(receiverRadio);
				send(msg);
				
			}
			
		});
	}
	
	protected void takeDown(){
		// Printout a dismissal	message
		System.out.println("Traffic-Agent "+getAID().getName()+ "terminating.");
	}
}