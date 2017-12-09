package behaviours;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import sajas.core.Agent;
import sajas.core.behaviours.*;
import java.util.Vector;
import agents.TrafficLightAgent;
import agents.VehicleAgent;
import jade.core.AID;

public class FindTrafficLights extends Behaviour{

	private int step = 0;
	private int repliesCnt = 0;
	private boolean foundLight = false;
	private VehicleAgent car;
	private Vector<TrafficLightAgent> lights;
	private MessageTemplate mt;
	
	public FindTrafficLights(VehicleAgent car, Vector<TrafficLightAgent> lights) {
		this.lights = lights;
		this.car = car;
	}
	
	@Override
	public void action() {
		
		switch (step){
		case 0:
		//send the cfp to all traffic lights
			ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
			for(int i = 0; i < lights.size(); i++){
				cfp.addReceiver(lights.elementAt(i).getAID());
			}
			cfp.setContent("position");
			cfp.setConversationId("position");
			cfp.setReplyWith("cfp"+System.currentTimeMillis());
			myAgent.send(cfp);
			mt = MessageTemplate.and(MessageTemplate.MatchConversationId("position"), MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));			
			step = 1;
			break;
		case 1:
		//receive all answers from traffic lights
			ACLMessage reply = car.receive(mt); 
			if(reply != null){
				String carPos = "" + car.getPosition().getX() + car.getPosition().getY() + "";
				if(reply.getContent().equals(carPos)){
					car.setLightAtCarPos(reply.getSender());
					foundLight = true;
				}
				repliesCnt++;
				if(foundLight == true || repliesCnt == lights.size()){
					step = 2;
				}
			}
			else{
				block();
			}
			break;
		}
		
	}

	@Override
	public boolean done() {
		return step == 2;
	}
	
}
