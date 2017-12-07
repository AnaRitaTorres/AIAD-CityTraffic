package behaviours;

import sajas.core.behaviours.*;

import agents.*;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class CrossRoadTrafficLights extends Behaviour{
	
	private TrafficLightAgent light1;
	private TrafficLightAgent light2;
	private int step = 0;
	private ACLMessage reply;
	
	public CrossRoadTrafficLights(TrafficLightAgent light1,TrafficLightAgent light2) {
		
		this.light1=light1;
		this.light2=light2;
		
	}
	
	@Override
	public void action() {
		ACLMessage cfp= new ACLMessage(ACLMessage.CFP);
		//send msg to the other light to knows its color
		switch(step){
		case 0:
			cfp.addReceiver(light2.getAID());
			cfp.setContent("Cor?");
			cfp.setConversationId("cor");
			light1.send(cfp);
			step=1;
			break;
		case 1:
			//wait for response by traffic light
			reply= light1.receive();
			if(reply != null){
				step = 2;
			}
			break;
		case 2:
			if(reply.getContent().equals("red"))
				light1.changeColor("green");
			else 				
				light1.changeColor("red");//TODO ver a situação do laranja
			
			step=4;
			break;
		default:
			break;
		}
		
	}
	
	@Override
	public boolean done() {
		return step == 4;
	}
}