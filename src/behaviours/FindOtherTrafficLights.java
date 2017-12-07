package behaviours;

import java.util.Vector;

import agents.TrafficLightAgent;
import jade.lang.acl.ACLMessage;
import sajas.core.behaviours.Behaviour;

@SuppressWarnings("serial")
public class FindOtherTrafficLights  extends Behaviour {
	
	private int step = 0;
	private TrafficLightAgent light;
	private Vector<TrafficLightAgent> lights;
	
	public FindOtherTrafficLights(TrafficLightAgent light,Vector<TrafficLightAgent> lights) {
		this.light=light;
		this.lights=lights;
		
	}
	@Override
	public void action() {
		switch (step){
		case 0:
			//send the cfp to all traffic lights
			ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
			for(int i = 0; i < lights.size(); i++){
				if(light!= lights.get(i)) {
					cfp.addReceiver(lights.elementAt(i).getAID());
				}
			}
			cfp.setContent("position1");
			cfp.setConversationId("position1");
			light.send(cfp);
			
			step=1;
			break;
		case 1:
			ACLMessage reply = light.receive(); 
			if(reply != null){
				//TODO point o intersection
			}
			else
				block();
			step=4;
			break;
		}
		
	}
	
	@Override
	public boolean done() {
		return step==4;
	}
	
}