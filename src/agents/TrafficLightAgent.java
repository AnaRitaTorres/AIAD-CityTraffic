package agents;


import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JOptionPane;
import java.awt.Color;

import sajas.core.Agent;
import sajas.core.behaviours.*;

import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class TrafficLightAgent extends Agent {
	
	private static int IDNumber=0;
	private int ID;
	public String currentColor;
	public int[] position = new int[2];	//posiçao do semaforo (TODO eventualmente atribuir valores de grafo
	
	public TrafficLightAgent() {
		IDNumber++;
		ID=IDNumber;
		position[0] = 3;
		position[1] = 3;
	}
	
	public int getID() {
		return ID;
	}
	
	protected void setup() {
		System.out.println("Hello! Traffic-Agent "+getAID().getName()+" is ready.");
		
		AtomicInteger i = new AtomicInteger(0);

		Vector<String> color = new Vector<String>(3);
		color.addElement("red");
		color.addElement("orange");
		color.addElement("green");
		currentColor = color.elementAt(i.get());	

		addBehaviour(new TickerBehaviour(this, 10000){

			protected void onTick() {         
				JOptionPane.showMessageDialog(null,"changed color " + color.elementAt(i.get()));
				if(i.get() == 2){
					i.set(0);
					currentColor = color.elementAt(i.get());
				}
				else{
					i.set(i.get() + 1);
					currentColor = color.elementAt(i.get());
				}
			}
		});
		
		addBehaviour(new CyclicBehaviour(){

			@Override
			public void action() {
				ACLMessage msg = receive();
				if(msg != null){
					ACLMessage reply = msg.createReply();
					if(msg.getPerformative() == ACLMessage.CFP){
						String content = msg.getContent();
						if (content.equals("Cor?")){
							reply.setPerformative(ACLMessage.INFORM);
							reply.setContent(currentColor);
							System.out.println(reply.getContent());
						}
					}
				}
			}

		});
		
	}
		
	protected void takeDown(){
		// Printout a dismissal	message
		System.out.println("Traffic-Agent "+getAID().getName()+ "terminating.");
	}
}