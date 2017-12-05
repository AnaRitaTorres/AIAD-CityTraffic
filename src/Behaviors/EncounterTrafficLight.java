package Behaviors;


import javax.swing.JOptionPane;

import sajas.core.Agent;
import sajas.core.behaviours.*;
import agents.TrafficLightAgent;
import agents.VehicleAgent;
import jade.lang.acl.ACLMessage;
import jade.core.AID;

public class EncounterTrafficLight extends Behaviour{

	private TrafficLightAgent light;
	private VehicleAgent car;
	
	public EncounterTrafficLight(VehicleAgent car, TrafficLightAgent light){
		this.car = car;
		this.light = light;
	}
	
	//TODO fazer maquina de estados como exemplo de pede cor, espera resposta, deel with it
	
	@Override
	public void action() {
		ACLMessage cfp= new ACLMessage(ACLMessage.CFP);
		System.out.println("Cor?");
		cfp.addReceiver(light.getAID());
		cfp.setContent("Cor?");
		car.send(cfp);
	}
	
	/*if(resposta vermelho??){
		
	}*/

	
		
	public boolean done(){
		return true;//????
	}
		
}
