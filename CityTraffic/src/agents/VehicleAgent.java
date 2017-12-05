package agents;

import javax.swing.JOptionPane;

import sajas.core.Agent;
import sajas.core.behaviours.*;

import jade.lang.acl.ACLMessage;
import jade.core.AID;

@SuppressWarnings("serial")
public class VehicleAgent extends Agent{
	
	//variavel percepcao visual - tem carros á frente ou não
	
	private static int IDNumber=0;
	private int ID;
	private AID tLight;
	
	public VehicleAgent(AID tLight) {
		IDNumber++;
		ID=IDNumber;
		this.tLight = tLight;
	}
	
	public int getID() {
		return ID;
	}
	
	protected void setup() {
		System.out.println("Hello! Vehicle-Agent "+getAID().getName()+ "is ready.");
		
		addBehaviour(new CyclicBehaviour(){

			@Override
			public void action() {
				//System.out.println("Cor?");
				ACLMessage cfp= new ACLMessage(ACLMessage.CFP);
				cfp.addReceiver(tLight);
				cfp.setContent("Cor?");
				send(cfp);
				
			}
			
		});
	}
	
	protected void takeDown(){
		// Printout a dismissal	message
		System.out.println("Vehicle-Agent "+getAID().getName()+ "terminating.");
	}
}