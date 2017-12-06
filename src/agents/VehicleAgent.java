package agents;

import java.util.Vector;

import javax.swing.JOptionPane;

import sajas.core.Agent;
import sajas.core.behaviours.*;
import agents.TrafficLightAgent;
import behaviours.EncounterTrafficLight;
import behaviours.FindTrafficLights;
import jade.lang.acl.ACLMessage;
import jade.core.AID;

@SuppressWarnings("serial")
public class VehicleAgent extends Agent{
	
	private static int IDNumber=0;
	private int ID;
	private AID tLight;
	public int[] position = new int[2];	//posiçao atual do carro
	private Vector<TrafficLightAgent> trafficLights;
	public VehicleAgent car = this;
	private AID lightAtCarPos;
	
	public VehicleAgent(Vector<TrafficLightAgent> trafficLights) {
		IDNumber++;
		ID=IDNumber;
		this.trafficLights = trafficLights;
		position[0] = 0;//TODO (4)por posiçoes do grafo
		position[1] = 0;
	}
	
	public void setLightAtCarPos(AID light){
		lightAtCarPos = light;
	}
	
	public int getID() {
		return ID;
	}
	
	protected void setup() {
		
		System.out.println("Hello! Vehicle-Agent "+getAID().getName()+ "is ready.");
		
		addBehaviour(new TickerBehaviour(this, 1000){	//TODO (2)o valor dos ticks é a velocidade do carro uns andam de 1 em 1 seg outros meio em meio outros 2 em 2  etc
			
			@Override
			protected void onTick() {    
								
				System.out.println("car position: " + position[0] + position [1]);
				
				//carro ve se tem semaforo
				lightAtCarPos = null;
				//perguntar a todos os semaforos a posiçao
				addBehaviour(new FindTrafficLights(car, trafficLights));
				//perguntar cor do semaforo que encontrou
				
				System.out.println("light" + lightAtCarPos);
				//TODO lighATCarPor dá semre null deste nado mas do lado do behavior nao
				
				if(lightAtCarPos != null){
					car.addBehaviour(new EncounterTrafficLight(car, lightAtCarPos));
				}
				else{
					position[0] = position[0] + 1;
					position[1] = position[1] + 1;//TODO (1) eventualmente faze lo andar pelos pontos do grafo
				}

				
				/*int i;
				boolean found = false;
				for(i = 0; i < trafficLights.size(); i++){
					if(trafficLights.elementAt(i).position[0] == position[0] && trafficLights.elementAt(i).position[1] == position[1]){
						found = true;
						final int index = i;
					}
				}
				if(found == false){
					
				}*/
				
				//TODO (3)carro ver se tem carros 
				//TODO (5)carro para o tick behavior se tiver chegado ao destino
			}
		});
	}
	
	protected void takeDown(){
		// Printout a dismissal	message
		System.out.println("Vehicle-Agent "+getAID().getName()+ "terminating.");
	}
}