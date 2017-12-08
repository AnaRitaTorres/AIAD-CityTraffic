package agents;


import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JOptionPane;
import java.awt.Color;

import sajas.core.Agent;
import sajas.core.behaviours.*;

import uchicago.src.sim.gui.OvalNetworkItem;
import uchicago.src.sim.gui.DisplaySurface;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import behaviours.CrossRoadTrafficLights;
import behaviours.FindOtherTrafficLights;
import graph.GraphNode;

@SuppressWarnings("serial")
public class TrafficLightAgent extends Agent{

	//TODO por semafors em cruzamentos e po los a comunicar para saberem qual vai estar verde para nao causar acidentes(como jp disse)

	private static int IDNumber=0;
	private int ID;
	private String currentColor;
	private int[] position = new int[2];	//posiçao do semaforo (TODO eventualmente atribuir valores de grafo
	private TrafficLightAgent light;
	private OvalNetworkItem s;
	private DisplaySurface disp;
	private Vector<TrafficLightAgent> trafficLights;
	private ArrayList<GraphNode> crossroads;
	private ArrayList<GraphNode> graphNodes;
		
	public TrafficLightAgent(int x, int y, Vector<TrafficLightAgent> trafficLights,ArrayList<GraphNode> crossroads,ArrayList<GraphNode> graphNodes,DisplaySurface disp) {
		IDNumber++;
		ID=IDNumber;
		position[0] = x;
		position[1] = y;
		light = this;
		this.s= new OvalNetworkItem(x,y);
		this.disp=disp;
		this.trafficLights = trafficLights;
		this.crossroads=crossroads;
		this.graphNodes=graphNodes;
	}

	public OvalNetworkItem getS() {
		return s;
	}

	public void setS(OvalNetworkItem s) {
		this.s = s;
	}

	public void changeColor(String color) {

		if(color=="red") {
			s.setColor(Color.red);
		}
		else if(color=="green") {
			s.setColor(Color.green);
		}
		else {
			s.setColor(Color.orange);
		}
		disp.updateDisplay();
		
	}
	public int getID() {
		return ID;
	}

	public int[] getPosition() {

		return position;
	}

	public int getX() {
		return position[0];
	}

	public int getY() {
		return position[1];
	}
	protected void setup() {
		System.out.println("Hello! Traffic-Agent "+getAID().getName()+" is ready.");

		AtomicInteger i = new AtomicInteger(0);

		Vector<String> color = new Vector<String>(3);
		color.addElement("red");
		color.addElement("green");
		color.addElement("orange");
		currentColor = color.elementAt(i.get());	

		changeColor(currentColor);

		addBehaviour(new TickerBehaviour(this, 10000){

			protected void onTick() {  
				if(i.get() == 2){
					i.set(0);
					currentColor = color.elementAt(i.get());
					changeColor(currentColor);
				}
				else{
					i.set(i.get() + 1);
					currentColor = color.elementAt(i.get());
					changeColor(currentColor);
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
					
						if (msg.getConversationId().equals("cor")){
							reply.setPerformative(ACLMessage.INFORM);
							reply.setContent(currentColor);
							reply.setConversationId("cor");
							myAgent.send(reply);
						}
						else if(msg.getConversationId().equals("position")){
							reply.setPerformative(ACLMessage.INFORM);
							String pos = "" + position[0] + position[1] + "";
							reply.setContent(pos);
							reply.setConversationId("position");
							myAgent.send(reply);
						}
						//crossroad behaviour
						else if(msg.getConversationId().equals("cor1")) {
							reply.setPerformative(ACLMessage.INFORM);
							String cor = currentColor;
							reply.setContent(cor);
							reply.setConversationId("cor1");
							myAgent.send(reply);
							System.out.println(reply.getContent());
						}
					
				}
			}

		});
		
		addBehaviour(new CrossRoadTrafficLights(trafficLights.get(0),trafficLights.get(1),crossroads,graphNodes));
		//addBehaviour(new FindOtherTrafficLights(trafficLights.get(0),trafficLights));
		
	}

	protected void takeDown(){
		// Printout a dismissal	message
		System.out.println("Traffic-Agent "+getAID().getName()+ "terminating.");
	}
}