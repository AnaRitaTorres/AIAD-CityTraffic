package behaviours;


import java.util.ArrayList;
import sajas.core.behaviours.*;

import agents.TrafficLightAgent;
import graph.GraphNode;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.core.AID;

@SuppressWarnings("serial")
public class CrossRoadTrafficLights extends Behaviour{
	
	private TrafficLightAgent light1;
	private AID light2;
	private int step = 0;
	private ACLMessage reply;
	private ArrayList<GraphNode> crossroads;
	private ArrayList<GraphNode> graphNodes;
	private MessageTemplate mt;
	
	public CrossRoadTrafficLights(TrafficLightAgent light1,AID light2,ArrayList<GraphNode> crossroads,ArrayList<GraphNode> graphNodes) {
		
		this.light1=light1;
		this.light2=light2;
		this.crossroads=crossroads;
		this.graphNodes=graphNodes;
		
	}
	
	@Override
	public void action() {
		
		//check if they are connected to the same crossroad
		switch(step){
		case 0:
			//if they are, ask each other for the color
			ACLMessage cfp= new ACLMessage(ACLMessage.CFP);
			cfp.addReceiver(light2);
			cfp.setContent("Cor?");
			cfp.setConversationId("cor1");
			cfp.setReplyWith("cfp"+System.currentTimeMillis());
			myAgent.send(cfp);
			mt= MessageTemplate.and(MessageTemplate.MatchConversationId("cor1"),MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
			step=1;
			break;
		case 1:
			//wait for the answer and with that info change colors
			
			reply= light1.receive(mt);
			if(reply != null){
				step=2;
			}
			else {
				block();
			}
			break;
		case 2:
			System.out.println(reply.getContent());
			if(reply.getContent().equals("red")|| reply.getContent().equals("orange")) {
				light1.changeColor("green");
			}
			else if(reply.getContent().equals("green")) {
				light1.changeColor("red");
			}
			step=0;
			break;
		}
		
	}
	//TODO averiguar se vale a pena alterar o comportamento para ciclico ou tick
	@Override
	public boolean done() {
		return step == 4;
	}
}