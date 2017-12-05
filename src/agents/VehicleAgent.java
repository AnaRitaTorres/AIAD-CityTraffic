package agents;

import java.util.Vector;

import javax.swing.JOptionPane;

import sajas.core.Agent;
import sajas.core.behaviours.*;
import agents.TrafficLightAgent;
import jade.lang.acl.ACLMessage;
import jade.core.AID;
import Behaviors.EncounterTrafficLight;

@SuppressWarnings("serial")
public class VehicleAgent extends Agent{
	
	//variavel percepcao visual - tem carros á frente ou não
	
	private static int IDNumber=0;
	private int ID;
	private AID tLight;
	public int[] position = new int[2];	//posiçao atual do carro
	private Vector<TrafficLightAgent> trafficLights;
	public VehicleAgent car = this;
	
	public VehicleAgent(Vector<TrafficLightAgent> trafficLights) {
		IDNumber++;
		ID=IDNumber;
		this.trafficLights = trafficLights;
		position[0] = 0;//TODO por posiçoes do grafo
		position[1] = 0;
	}
	
	public int getID() {
		return ID;
	}
	
	protected void setup() {
		
		System.out.println("Hello! Vehicle-Agent "+getAID().getName()+ "is ready.");
		
		addBehaviour(new TickerBehaviour(this, 1000){
			
			@Override
			protected void onTick() {    
				//carro anda				
				JOptionPane.showMessageDialog(null,"car position: " + position[0] + position [1]);
				position[0] = position[0] + 1;
				position[1] = position[1] + 1;//TODO eventualmente faze lo andar pelos pontos do grafo
				
				//carro ve se tem semaforo
				int i;
				for(i = 0; i < trafficLights.size(); i++){
					if(trafficLights.elementAt(i).position[0] == position[0] && trafficLights.elementAt(i).position[1] == position[1]){
						final int index = i;
						addBehaviour(new EncounterTrafficLight(car, trafficLights.elementAt(index)));
					}
				}
				
				//TODO carro ver se tem carros
				//TODO carro para o tick behavior se tiver chegado ao destino
			}
		});
	}
	
	protected void takeDown(){
		// Printout a dismissal	message
		System.out.println("Vehicle-Agent "+getAID().getName()+ "terminating.");
	}
}