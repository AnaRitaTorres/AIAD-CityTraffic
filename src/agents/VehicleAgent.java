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
	private int velocity;
	Behaviour searchLight, dealLight;

	public VehicleAgent(int[]position, int velocity, Vector<TrafficLightAgent> trafficLights) {
		IDNumber++;
		ID=IDNumber;
		this.trafficLights = trafficLights;
		this.position = position;
		this.velocity = velocity;
	}

	public void setLightAtCarPos(AID light){
		lightAtCarPos = light;
	}

	public int getID() {
		return ID;
	}

	protected void setup() {
		
		//TODO (7) tratar de colisões
		
		//para testar
				if(getAID().getName().equals("Vehicle1@City Traffic")){
					this.position[0] = 2;
					this.position[1] = 2;
				}

		System.out.println("Hello! Vehicle-Agent "+ getAID().getName() + " is ready.");

		addBehaviour(new TickerBehaviour(this, velocity){

			@Override
			protected void onTick() {  
				step = 0;
				lightAtCarPos = null;

				System.out.println("car " + getAID().getName()+ " position: " + position[0] + position [1]);
				car.addBehaviour(new Behaviour() {

					@Override
					public boolean done() {
						return step == 5;
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
								step = 4;
							}
							break;
						case 3:
							if(dealLight.done()){
								step = 4;
							}
							break;
						case 4:
							//TODO(1) falar com carros VER SE TEM CAAROS À FRENTE criar outro behaviour
							position[0] = position[0] + 1;
							position[1] = position[1] + 1;//TODO (0) eventualmente faze lo andar pelos pontos do grafo (e testar carro vs light para depois fazer carro vs carro)
							step = 5;
							break;
						}
					}
				});


				//TODO (5)carro para o tick behavior se tiver chegado ao destino (ou seja implica criar posiçoes iniciais e finas e faze lo percorrer o caminha, implica implementar djkistra
			}
		});
	}

	protected void takeDown(){
		// Printout a dismissal	message
		System.out.println("Vehicle-Agent "+getAID().getName()+ "terminating.");
	}
}