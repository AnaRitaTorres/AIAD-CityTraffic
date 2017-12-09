package behaviours;

import sajas.core.behaviours.TickerBehaviour;

import agents.TrafficLightAgent;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.core.AID;

@SuppressWarnings("serial")
public class CrossRoadTrafficLights extends TickerBehaviour{
	
	private TrafficLightAgent light1;
	private AID light2;
	private int step = 0;
	private ACLMessage reply;
	private MessageTemplate mt;
	
	public CrossRoadTrafficLights(TrafficLightAgent light1,AID light2) {
		
		super(light1,10000);
		this.light1=light1;
		this.light2=light2;
		System.out.println(light2);
	}
	
	@Override
	protected void onTick(){
		
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
			System.out.println(reply.getContent());
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
	
			break;
		}
		
	}
	
	
}