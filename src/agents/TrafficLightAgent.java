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
import jade.core.AID;
import behaviours.CrossRoadTrafficLights;
import behaviours.FindCrossroadTrafficLight;
import graph.GraphNode;

@SuppressWarnings("serial")
public class TrafficLightAgent extends Agent{

	private static int IDNumber=0;
	private int ID;
	private String currentColor;
	private int[] position = new int[2];
	private TrafficLightAgent light;
	private OvalNetworkItem s;
	private DisplaySurface disp;
	private Vector<TrafficLightAgent> trafficLights;
	private ArrayList<GraphNode> crossroads;
	private ArrayList<GraphNode> graphNodes;
	private boolean haspair=false;
	private ArrayList<TrafficLightAgent> pair = new ArrayList<TrafficLightAgent>(2);
	private AtomicInteger i;

		
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
		java.util.Random r = new java.util.Random();
		int index = r.nextInt(3);
		this.i = new AtomicInteger(index);
		
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
		else if(color=="orange"){
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
	
	public boolean isHaspair() {
		return haspair;
	}
	
	public void setCurrentColor(String color){
		this.currentColor = color;
	}

	public ArrayList<TrafficLightAgent> getPair() {
		return pair;
	}

	public void setPair(TrafficLightAgent pair,TrafficLightAgent pair1) {
		this.pair.add(pair);
		this.pair.add(pair1);
	}

	public void setHaspair(boolean haspair) {
		this.haspair = haspair;
	}
	
	protected void setup() {
		System.out.println("Hello! Traffic-Agent "+getAID().getName()+" is ready.");

		Vector<String> color = new Vector<String>(3);
		color.addElement("red");
		color.addElement("green");
		color.addElement("orange");
		currentColor = color.elementAt(i.get());	

		changeColor(currentColor);

		addBehaviour(new TickerBehaviour(this, 3000){

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
							//System.out.println(reply.getContent());
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
							reply.setContent(currentColor);
							reply.setConversationId("cor1");
							myAgent.send(reply);
						}
						else if(msg.getConversationId().equals("position1")) {
							reply.setPerformative(ACLMessage.INFORM);
							String pos = "" + position[0] +";"+ position[1] + "";
							reply.setContent(pos);
							reply.setConversationId("position1");
							myAgent.send(reply);
						}
						
				}
			}

		});
		
		
		addBehaviour(new FindCrossroadTrafficLight(this,trafficLights,crossroads,graphNodes));
		
	}

	protected void takeDown(){
		// Printout a dismissal	message
		System.out.println("Traffic-Agent "+getAID().getName()+ "terminating.");
	}


}