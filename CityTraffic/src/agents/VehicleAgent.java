package agents;

import javax.swing.JOptionPane;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class VehicleAgent extends Agent{
	
	//variavel percepcao visual - tem carros á frente ou não
	
	protected void setup() {
		System.out.println("Hello! Vehicle-Agent "+getAID().getName()+ "is ready.");
		
		addBehaviour(new CyclicBehaviour(){

			@Override
			public void action() {
				//Receive msg from other agent
				ACLMessage msg = receive();
				if(msg != null) {
					JOptionPane.showMessageDialog(null,"car msg " +msg.getContent());
				}
				else
					block();
				
			}
			
		});
	}
	
	protected void takeDown(){
		// Printout a dismissal	message
		System.out.println("Vehicle-Agent "+getAID().getName()+ "terminating.");
	}
}