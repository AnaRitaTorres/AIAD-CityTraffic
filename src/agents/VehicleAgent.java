package agents;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
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
	public GraphNode nextPosition;
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
	private List<Stack<GraphNode>> allPaths = new ArrayList<Stack<GraphNode>>();
	private Stack<GraphNode> path = new Stack<GraphNode>();
	private List<GraphNode> onPath = new ArrayList<GraphNode>();
	private int numPaths = 5;
	private int indexPath = 0;
	private Statistics stats;
	private ArrayList<MyNode> carsNodes;
	private MyNode n;
	private boolean accident;
	private boolean end = false;
	private int numAccidents;
	public long dt;
	public long dt2;
	private boolean arrived = false;
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
		this.graph = graph;
		n = new MyNode(getS(),position.getX(), position.getY());
		this.carsNodes.add(n);

		calculatePath(position, posEnd);

		if(allPaths.size() > 0){
			int min = allPaths.get(0).size();
			for(int i = 0; i < allPaths.size(); i++){
				if(allPaths.get(i).size() <= min){
					min = allPaths.get(i).size();
					path = allPaths.get(i);
				}
			}
		}
		else{
			path = new Stack<GraphNode>();
		}
	}

	public void calculatePath(GraphNode src, GraphNode dest){
		path.push(src);
		onPath.add(src);
		if (src.equals(dest)) {
			if(numPaths == 0){return;}
			allPaths.add((Stack<GraphNode>)(path.clone()));
			numPaths--;
		}
		else{
			for (GraphNode child : src.getAdj()) {
				if (!onPath.contains(child)) {
					calculatePath(child, dest);
				}
			}
		}

		path.pop();
		onPath.remove(src);

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
		
		if(indexPath + 1 < path.size()){
			indexPath++;
			if(indexPath == path.size() - 1){
				end = true;
			}
			return path.get(indexPath);
		}
		else{
			if(!arrived){
				stats.updateNumCarsArrivedDestination();
				arrived = true;
			}
			GraphNode remove = new GraphNode(1, 1);

			return remove;
		}
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
		if(end == true){
			s.setColor(Color.MAGENTA);
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

				String strCarPos = "" + position.getX() + position.getY() + "";

				//carro ve se tem semaforo
				switch (step){

				case 0:
					//send the cfp to all cars
					if(cars.size() == 1){
						step = 2;
					}
					else{
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
					}

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
					if(arrived){
						step = 8;
					}
					else{
						encounterCar = new EncounterCar(car, cars);
						addBehaviour(encounterCar);
						step = 7;
					}
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
					if(arrived){
						GraphNode n = new GraphNode(1, 1);
						car.position = n;
						updateDisplayCar();
						step = 10;
					}
					else{
						car.lastVisited = car.position;
						car.position = car.nextPosition;
						updateDisplayCar();
						double dist = calcDist(car.lastVisited, car.position);
						stats.updateTotalDistance(dist);
						stats.updateAvgDistance(cars.size());
						step = 0;
					}
					repliesCnt = 0;
					foundCar = false;
					break;
				case 10:
					break;
				}
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