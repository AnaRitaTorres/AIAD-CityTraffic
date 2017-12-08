package behaviours;


import javax.swing.JOptionPane;

import sajas.core.Agent;
import sajas.core.behaviours.*;
import agents.VehicleAgent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.core.AID;

//@SuppressWarnings("serial")
public class EncounterTrafficLight extends Behaviour{

	private AID light;
	private VehicleAgent car;
	private int step = 0;
	private ACLMessage reply;
	
	public EncounterTrafficLight(VehicleAgent car, AID light){
		this.car = car;
		this.light = light;
	}
	
	@Override
	public void action() {
		ACLMessage cfp= new ACLMessage(ACLMessage.CFP);
		switch(step){
		case 0:
			//send request for color to traffic light encountered by the car
			System.out.println("Cor?");
			cfp.addReceiver(light);
			cfp.setContent("Cor?");
			cfp.setConversationId("cor");
			car.send(cfp);
	
			step = 1;
			break;
		case 1:
			//wait for response by traffic light
			reply= car.receive();
			if(reply != null){
				step = 2;
			}
			break;
		case 2:
			//handle traffic light color
			if(! reply.getContent().equals("red")){
				step = 4;
			}
			break;
		}
	}

	@Override//ver se continua a funcionar
	public boolean done(){
		return step == 4;
	}
		
}
