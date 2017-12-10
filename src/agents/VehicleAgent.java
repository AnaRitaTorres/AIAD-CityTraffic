package agents;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
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
import graph.GraphNode;
import graph.MyNode;
import gui.Statistics;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.core.AID;

@SuppressWarnings("serial")
public class VehicleAgent extends Agent{

	private static int IDNumber=0;
	private int ID;
	private GraphNode position;
	private GraphNode nextPosition;
	private GraphNode posEnd;
	private GraphNode lastVisited;
	private Vector<VehicleAgent> cars;
	private Vector<TrafficLightAgent> trafficLights;
	public VehicleAgent car = this;
	private AID lightAtCarPos;
	private int step;
	private int velocity;
	private RectNetworkItem s;
	private DisplaySurface disp;
	private Graph graph;
	private List<GraphNode> path;
	private int indexPath = 0;
	private Statistics stats;
	private ArrayList<MyNode> carsNodes;
	private MyNode n;
	private boolean accident;
	private int numAccidents;
	public long dt;
	public long dt2;
	private int repliesCnt;
	private boolean foundCar;
	private ACLMessage reply;
	private MessageTemplate mt;
	Behaviour searchLight, dealLight, encounterCar;

	public VehicleAgent(GraphNode posInit, GraphNode posEnd, int velocity, Vector<VehicleAgent> cars, Vector<TrafficLightAgent> trafficLights, Graph graph, ArrayList<MyNode> carsNodes, DisplaySurface disp, Statistics stats) {
		IDNumber++;
		ID=IDNumber;
		this.cars = cars;
		this.trafficLights = trafficLights;
		this.position = posInit;
		this.posEnd = posEnd;
		this.lastVisited = posInit;
		this.velocity = velocity;
		this.accident = false;
		this.numAccidents = 0;
		this.s= new RectNetworkItem(posInit.getX(),posInit.getY());
		this.disp=disp;
		this.stats = stats;
		s.setColor(Color.CYAN);
		this.carsNodes = carsNodes;
		//this.graph = graph;	//TODO é preciso tirar do construtor??
		n = new MyNode(getS(),position.getX(), position.getY());
		this.carsNodes.add(n);
		
		List<GraphNode> visited = new ArrayList<>();
		path = new ArrayList<>();
		calculatePath(position, posEnd, visited, path);
		System.out.println("visited=" + visited.size());
		System.out.println("path=" + path);

		/*
		//para apagar
		xtrajetoriaV1[0] = 70+30;
		xtrajetoriaV1[1] = 85+30;
		xtrajetoriaV1[2] = 100+30;
		xtrajetoriaV1[3] = 115+30;
		xtrajetoriaV1[4] = 130+30;
		xtrajetoriaV1[5] = 145+30;
		xtrajetoriaV1[6] = 160+30;

		ytrajetoriaV1[0] = 110;
		ytrajetoriaV1[1] = 110;
		ytrajetoriaV1[2] = 110;
		ytrajetoriaV1[3] = 110;
		ytrajetoriaV1[4] = 110;
		ytrajetoriaV1[5] = 110;
		ytrajetoriaV1[6] = 110;

		/*xtrajetoriaV2[0] = 100;
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

		xtrajetoriaV2[0] = 100+60;
		xtrajetoriaV2[1] = 100+60;
		xtrajetoriaV2[2] = 100+60;
		xtrajetoriaV2[3] = 100+60;
		xtrajetoriaV2[4] = 100+60;
		//xtrajetoriaV2[5] = 100;
		//xtrajetoriaV2[6] = 100;

		//ytrajetoriaV2[0] = 70;
		//ytrajetoriaV2[1] = 80;
		ytrajetoriaV2[0] = 90;
		ytrajetoriaV2[1] = 100;
		ytrajetoriaV2[2] = 110;
		ytrajetoriaV2[3] = 120;
		ytrajetoriaV2[4] = 130;
		*/
	}
	
	public void calculatePath(GraphNode src, GraphNode dest, List<GraphNode> visited, List<GraphNode> path){
		path.add(src);
		visited.add(src);
		if (src.equals(dest)) {
			return;
		}
		List<GraphNode> shortestChildPath = null;
		for (GraphNode child : src.getAdj()) {
			if (!visited.contains(child)) {
				List<GraphNode> childPath = new ArrayList<>();
				calculatePath(child, dest, visited, childPath);
				if (shortestChildPath == null || childPath.size() < shortestChildPath.size()) {
					shortestChildPath = childPath;
				}
			}
		}
		if (shortestChildPath != null) {
			path.addAll(shortestChildPath);
		}
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

	public GraphNode getPosition() {
		return position;
	}
	
	public GraphNode getPositionEnd(){
		return posEnd;
	}

	public GraphNode getNextPosition() {
		//now random depois TODO seguir caminho
		/*java.util.Random r = new java.util.Random();
		int pos = r.nextInt(position.getAdj().size());*/
		
		//path.next();	ISTO
		
		//nao ser igual a last se calhar só é preciso para random movement?
		/*int iLast = position.getAdj().indexOf(lastVisited);
		while(pos == iLast){
			pos = r.nextInt(position.getAdj().size());
		}*/
		if(indexPath + 1 < path.size()){
			indexPath++;
		}
		
		return path.get(indexPath);
	}

	public void updateDisplayCar(){

		carsNodes.remove(n);
		n = new MyNode(this.getS(),this.position.getX(),this.position.getY());
		carsNodes.add(n);
		s.setX(position.getX());
		s.setY(position.getY());
		if(accident == true){
			s.setColor(Color.BLACK);
		}
		disp.updateDisplay();
	}
	
	public double calcDist(GraphNode n1, GraphNode n2){
		return Math.sqrt(Math.pow((n2.getX() - n1.getX()), 2) + Math.pow((n2.getY() - n1.getY()), 2));
	}


	protected void setup() {

		System.out.println("Hello! Vehicle-Agent "+ getAID().getName() + " is ready.");

		step = 0;
		lightAtCarPos = null;
		addBehaviour(new TickerBehaviour(this, velocity){

			@Override
			protected void onTick() {  

				//System.out.println("car " + getAID().getName()+ " position: " + position.getX() + position.getY());
				String strCarPos = "" + position.getX() + position.getY() + "";

				//carro ve se tem semaforo
				switch (step){

				case 0:
					//send the cfp to all cars
					ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
					for(int i = 0; i < cars.size(); i++){
						if(cars.elementAt(i).getAID() != car.getAID()){
							cfp.addReceiver(cars.elementAt(i).getAID());
						}
					}
					cfp.setContent("position");
					cfp.setConversationId("position");
					cfp.setReplyWith("cfp"+System.currentTimeMillis());
					car.send(cfp);
					mt = MessageTemplate.and(MessageTemplate.MatchConversationId("position"), MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));			

					repliesCnt = 0;
					foundCar = false;
					step = 1;
					break;
				case 1:
					//receive all answers from cars
					reply = car.receive(mt); 
					if(reply != null){
						if(reply.getContent().equals(strCarPos)){
							foundCar = true;
							accident = true;
							numAccidents++;
							stats.updateTotalAccidents();
							stats.updateAvgAccidents(cars.size());
						}
						repliesCnt++;
						if(repliesCnt == cars.size()-1){
							step = 2;
						}
					}
					else{
						block();
					}
					break;
				case 2:
					//perguntar a todos os semaforos a posiçao
					searchLight = new FindTrafficLights(car, trafficLights);
					addBehaviour(searchLight);
					step = 3;
					break;
				case 3:
					if (searchLight.done()){
						step = 4;
					}
					break;
				case 4:
					//perguntar cor do semaforo que encontrou
					if(lightAtCarPos != null){
						dealLight = new EncounterTrafficLight(car, lightAtCarPos);
						addBehaviour(dealLight);
						step = 5;

					} else{
						step = 6;
					}
					break;
				case 5:
					if(dealLight.done()){
						stats.updateTotalTimeWaitingTrafficLight(car.dt);
						stats.updateAvgTimeWaitingTrafficLight(cars.size());
						step = 6;
					}
					else{
						block();
					}
					break;

				case 6:
					nextPosition = car.getNextPosition();
					encounterCar = new EncounterCar(car, cars);
					addBehaviour(encounterCar);
					step = 7;
					break;
				case 7:
					if(encounterCar.done()){
						stats.updateTotalTimeWaitingTraffic(car.dt2);
						stats.updateAvgTimeWaitingTraffic(cars.size());
						step = 8;
					}
					else{
						block();
					}
					break;
				case 8:
					//TODO para já andam random, depois andam pelo caminho até ao destino
					car.lastVisited = car.position;
					car.position = car.nextPosition;
					double dist = calcDist(car.lastVisited, car.position);
					stats.updateTotalDistance(dist);
					stats.updateAvgDistance(cars.size());
					updateDisplayCar();

					step = 0;
					repliesCnt = 0;
					foundCar = false;
					break;
				}

				//TODO (5)carro para o tick behavior se tiver chegado ao destino e carro tem de desaparecer (ou seja implica criar posiçoes iniciais e finas e faze lo percorrer o caminha
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
							String pos = "" + position.getX() + position.getY() + "";
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