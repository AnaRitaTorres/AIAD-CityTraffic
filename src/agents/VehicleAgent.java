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
	private int step;
	Behaviour searchLight, dealLight;

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
				step = 0;
				lightAtCarPos = null;

				System.out.println("car position: " + position[0] + position [1]);
				car.addBehaviour(new Behaviour() {

					@Override
					public boolean done() {
						return step == 3;
					}

					@Override
					public void action() {
						//carro ve se tem semaforo
						switch (step){
						case 0:
							//perguntar a todos os semaforos a posiçao
							searchLight = new FindTrafficLights(car, trafficLights);
							addBehaviour(searchLight);
							step = 1;
							break;
						case 1:
							if (searchLight.done()){
								step = 2;
							}
							break;
						case 2:
							//perguntar cor do semaforo que encontrou
							if(lightAtCarPos != null){
								dealLight = new EncounterTrafficLight(car, lightAtCarPos);
								addBehaviour(dealLight);
								step = 3;
							} else{
								position[0] = position[0] + 1;
								position[1] = position[1] + 1;//TODO (1) eventualmente faze lo andar pelos pontos do grafo
								step = 3;
							}
							break;
						}
					}
				});



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