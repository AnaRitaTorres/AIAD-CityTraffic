package behaviours;

import jade.lang.acl.ACLMessage;
import sajas.core.Agent;
import sajas.core.behaviours.*;
import java.util.Vector;
import agents.TrafficLightAgent;
import agents.VehicleAgent;
import jade.core.AID;

@SuppressWarnings("serial")
public class FindTrafficLights extends Behaviour{

	private int step = 0;
	private int repliesCnt = 0;
	private boolean foundLight = false;
	private VehicleAgent car;
	private Vector<TrafficLightAgent> lights;
	
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
			car.send(cfp);
			
			step = 1;
			break;
		case 1:
		//receive all answers from traffic lights
			ACLMessage reply = car.receive(); 
			if(reply != null){
				String carPos = "" + car.position[0] + car.position[1] + "";
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
