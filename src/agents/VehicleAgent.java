package agents;

import java.util.Vector;

import javax.swing.JOptionPane;

import sajas.core.Agent;
import sajas.core.behaviours.*;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.OvalNetworkItem;
import uchicago.src.sim.gui.RectNetworkItem;
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
	private RectNetworkItem r;
	Behaviour searchLight, dealLight;
	private DisplaySurface disp;

	public VehicleAgent(int[]position, int velocity, Vector<TrafficLightAgent> trafficLights, DisplaySurface disp) {
		IDNumber++;
		ID=IDNumber;
		this.trafficLights = trafficLights;
		this.position = position;
		this.velocity = velocity;
		this.r = new RectNetworkItem(position[0],position[1]);
		this.disp=disp;
	}

	public void setLightAtCarPos(AID light){
		lightAtCarPos = light;
	}

	public int getID() {
		return ID;
	}
	
	public RectNetworkItem getS() {
		return r;
	}

	public void setS(RectNetworkItem r) {
		this.r = r;
	}
	
	public int getX() {
		return position[0];
	}
	
	public int getY() {
		return position[1];
	}
	protected void setup() {

		System.out.println("Hello! Vehicle-Agent "+getAID().getName()+ "is ready.");

		addBehaviour(new TickerBehaviour(this, velocity){

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
						
						//TODO (3)carro ver se tem carros dentro da maquina de estados em baixo? antes de ver semaforo? criar outro behaviour (ja tenho dois carros com velocidades e posiçoes diferentes
						
						
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


				//TODO (5)carro para o tick behavior se tiver chegado ao destino (ou seja implica criar posiçoes iniciais e finas e faze lo percorrer o caminha, implica implementar djkistra
			}
		});
	}

	protected void takeDown(){
		// Printout a dismissal	message
		System.out.println("Vehicle-Agent "+getAID().getName()+ "terminating.");
	}
}