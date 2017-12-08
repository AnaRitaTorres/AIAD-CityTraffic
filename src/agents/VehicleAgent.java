package agents;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JOptionPane;

import sajas.core.Agent;
import sajas.core.behaviours.*;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.RectNetworkItem;
import agents.TrafficLightAgent;
import behaviours.EncounterCar;
import behaviours.EncounterTrafficLight;
import behaviours.FindTrafficLights;
import graph.Graph;
import graph.MyNode;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.core.AID;

@SuppressWarnings("serial")
public class VehicleAgent extends Agent{

	private static int IDNumber=0;
	private int ID;
	private int[] position = new int[2];	//posiçao atual do carro
	private int[] nextPosition = new int[2]; //posiçao seguinte do carro
	private Vector<VehicleAgent> cars;
	private Vector<TrafficLightAgent> trafficLights;
	public VehicleAgent car = this;//se mudar para private funciona tudo?
	private AID lightAtCarPos;
	private int step;
	private int velocity;
	private RectNetworkItem s;
	private DisplaySurface disp;
	private Graph graph;
	private ArrayList<MyNode> carsNodes;
	private MyNode n;
	Behaviour searchLight, dealLight, encounterCar;

	//para apagar
	private int[] xtrajetoriaV1 = new int[7];
	private int[] ytrajetoriaV1 = new int[7];
	private int[] xtrajetoriaV2 = new int[7];
	private int[] ytrajetoriaV2 = new int[7];
	private int index = 0;


	public VehicleAgent(int x, int y, int velocity, Vector<VehicleAgent> cars, Vector<TrafficLightAgent> trafficLights, Graph graph, ArrayList<MyNode> carsNodes, DisplaySurface disp) {
		IDNumber++;
		ID=IDNumber;
		this.cars = cars;
		this.trafficLights = trafficLights;
		position[0] = x;
		position[1] = y;
		this.velocity = velocity;
		this.s= new RectNetworkItem(x,y);
		this.disp=disp;
		Color[] cores = new Color[5];
		cores[0] = Color.BLACK;
		cores[1] = Color.BLUE;
		cores[2] = Color.CYAN;
		cores[3] = Color.PINK;
		cores[4] = Color.YELLOW;
		java.util.Random r = new java.util.Random();
		int iCor = r.nextInt(5);
		s.setColor(cores[iCor]);
		this.carsNodes = carsNodes;
		this.graph = graph;
		n = new MyNode(getS(),getX(),getY());
		this.carsNodes.add(n);

		//para apagar
		xtrajetoriaV1[0] = 70;
		xtrajetoriaV1[1] = 85;
		xtrajetoriaV1[2] = 100;
		xtrajetoriaV1[3] = 115;
		xtrajetoriaV1[4] = 130;
		xtrajetoriaV1[5] = 145;
		xtrajetoriaV1[6] = 160;

		ytrajetoriaV1[0] = 110;
		ytrajetoriaV1[1] = 110;
		ytrajetoriaV1[2] = 110;
		ytrajetoriaV1[3] = 110;
		ytrajetoriaV1[4] = 110;
		ytrajetoriaV1[5] = 110;
		ytrajetoriaV1[6] = 110;

		xtrajetoriaV2[0] = 100;
		xtrajetoriaV2[1] = 115;
		xtrajetoriaV2[2] = 130;
		xtrajetoriaV2[3] = 145;
		xtrajetoriaV2[4] = 160;
		xtrajetoriaV2[5] = 175;
		xtrajetoriaV2[6] = 190;

		ytrajetoriaV2[0] = 110;
		ytrajetoriaV2[1] = 110;
		ytrajetoriaV2[2] = 110;
		ytrajetoriaV2[3] = 110;
		ytrajetoriaV2[4] = 110;
		ytrajetoriaV2[5] = 110;
		ytrajetoriaV2[6] = 110;

		/*xtrajetoriaV2[0] = 100;
		xtrajetoriaV2[1] = 100;
		xtrajetoriaV2[2] = 100;
		xtrajetoriaV2[3] = 100;
		xtrajetoriaV2[4] = 100;
		xtrajetoriaV2[5] = 100;
		xtrajetoriaV2[6] = 100;

		ytrajetoriaV2[0] = 70;
		ytrajetoriaV2[1] = 80;
		ytrajetoriaV2[2] = 90;
		ytrajetoriaV2[3] = 100;
		ytrajetoriaV2[4] = 110;
		ytrajetoriaV2[5] = 120;
		ytrajetoriaV2[6] = 130;*/
	}

	public RectNetworkItem getS() {
		return s;
	}

	public void setS(RectNetworkItem s) {
		this.s = s;
	}

	public void setLightAtCarPos(AID light){
		lightAtCarPos = light;
	}

	public int getID() {
		return ID;
	}

	public int getX() {
		return position[0];
	}

	public int getY() {
		return position[1];
	}

	public int[] getPosition() {
		return position;
	}

	public int[] getNextPosition() {
		return nextPosition;
	}

	public void updateDisplayCar(){

		carsNodes.remove(n);
		n = new MyNode(this.getS(),this.getX(),this.getY());
		carsNodes.add(n);
		s.setX(getX());
		s.setY(getY());
		disp.updateDisplay();
	}


	protected void setup() {

		System.out.println("Hello! Vehicle-Agent "+ getAID().getName() + " is ready.");

		step = 0;
		lightAtCarPos = null;
		addBehaviour(new TickerBehaviour(this, velocity){

			@Override
			protected void onTick() {  

				System.out.println("car " + getAID().getName()+ " position: " + position[0] + position [1]);

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
					else{
						block();
					}
					break;

				case 4:
					//TODO hardcoded vai ser para mudar para mover no grafo
					if(getAID().getName().equals("Vehicle1@City Traffic")){
						nextPosition[0] = xtrajetoriaV1[index];
						nextPosition[1] = ytrajetoriaV1[index];
					}
					else{
						nextPosition[0] = xtrajetoriaV2[index];
						nextPosition[1] = ytrajetoriaV2[index];
					}
					index++;
					encounterCar = new EncounterCar(car, cars);
					addBehaviour(encounterCar);
					step = 5;
					break;
				case 5:
					if(encounterCar.done()){
						step = 6;
					}
					else{
						block();
					}
					break;
				case 6:

					//position[0] = position[0] + 1;
					//position[1] = position[1] + 1;
					//TODO (2) eventualmente faze lo andar pelos pontos do grafo
					//para já andam random, depois andam pelo caminho até ao destino
					//para testar vou por aqui caminha harcoded

					position[0] = nextPosition[0];
					position[1] = nextPosition[1];

					updateDisplayCar();

					step = 0;
					break;
				}

				//TODO (1) tratar de colisões (colisoes - light - carro) - ver se ha outro carro na posiçao em que estou, se houver guardar o numero de carros(nºde colisoes deste carro) e o carro morre(fica parado aí para sempre

				//TODO (5)carro para o tick behavior se tiver chegado ao destino (ou seja implica criar posiçoes iniciais e finas e faze lo percorrer o caminha, implica implementar djkistra
			}
		});


		addBehaviour(new CyclicBehaviour(){

			@Override
			public void action() {
				MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
				ACLMessage msg = receive(mt);
				if(msg != null){
					ACLMessage reply = msg.createReply();
					if(msg.getPerformative() == ACLMessage.CFP){
						if(msg.getConversationId().equals("position")){
							reply.setPerformative(ACLMessage.INFORM);
							String pos = "" + position[0] + position[1] + "";
							reply.setContent(pos);
							reply.setConversationId("position");
							car.send(reply);
						}
					}
				}
			}

		});

	}

	protected void takeDown(){
		// Printout a dismissal	message
		System.out.println("Vehicle-Agent "+getAID().getName()+ "terminating.");
	}
}