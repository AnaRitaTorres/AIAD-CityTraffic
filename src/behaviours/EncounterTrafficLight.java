package behaviours;


import javax.swing.JOptionPane;

import sajas.core.Agent;
import sajas.core.behaviours.*;
import agents.TrafficLightAgent;
import agents.VehicleAgent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.core.AID;

public class EncounterTrafficLight extends Behaviour{

	private TrafficLightAgent light;
	private VehicleAgent car;
	private int step = 0;
	private ACLMessage reply;
	
	public EncounterTrafficLight(VehicleAgent car, TrafficLightAgent light){
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
			cfp.addReceiver(light.getAID());
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
				car.position[0] = car.position[0] + 1;
				car.position[1] = car.position[1] + 1;//TODO eventualmente faze lo andar pelos pontos do grafo
			}
			break;
		}
	}

	public boolean done(){
		return step == 4;
	}
		
}
