package agents;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class TrafficLightAgent extends Agent{
	
	protected void setup() {
		System.out.println("Hello! Traffic-Agent "+getAID().getName()+" is ready.");
		
		addBehaviour(new OneShotBehaviour(){

			@Override
			public void action() {
				//Send msg to the other agent
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.setContent("send");
				
				AID[] receiverAgents = new AID[2];
				receiverAgents[0]= new AID("carro",AID.ISLOCALNAME);
				receiverAgents[1]= new AID("radio", AID.ISLOCALNAME);
				
				for(int i=0; i < receiverAgents.length; i++) {
					msg.addReceiver(receiverAgents[i]);
					send(msg);
				}
				
			}
			
		});
	}
	
	protected void takeDown(){
		// Printout a dismissal	message
		System.out.println("Traffic-Agent "+getAID().getName()+ "terminating.");
	}
}