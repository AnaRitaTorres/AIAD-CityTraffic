package behaviours;

import sajas.core.behaviours.Behaviour;

import java.util.ArrayList;
import java.awt.Color;

import agents.TrafficLightAgent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.core.AID;

@SuppressWarnings("serial")
public class CrossRoadTrafficLights extends Behaviour{
	
	private TrafficLightAgent light1;
	private TrafficLightAgent light2;
	private int step = 0;
	private ACLMessage reply;
	private MessageTemplate mt;
	
	public CrossRoadTrafficLights(ArrayList <TrafficLightAgent> pair) {
		
		this.light1=pair.get(0);
		this.light2=pair.get(1);
	}
	
	@Override
	public void action(){
		
		switch(step){
		case 0:
			//if they are, ask each other for the color
			ACLMessage cfp= new ACLMessage(ACLMessage.CFP);
			cfp.addReceiver(light2.getAID());
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
			if(reply.getContent().equals("green")) {
				light1.changeColor("red");
				
			}
			else if(reply.getContent().equals("red") || reply.getContent().equals("orange")) {
				light1.changeColor("green");
			}
			step=0;
			break;
			
		}
		
	}
	
	public boolean done() {
		return step==4;
	}
	
}