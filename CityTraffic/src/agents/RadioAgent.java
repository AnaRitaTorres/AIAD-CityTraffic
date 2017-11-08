package agents;

import javax.swing.JOptionPane;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;


@SuppressWarnings("serial")
public class RadioAgent extends Agent{
	
	protected void setup() {
		
		System.out.println("Hello! Radio-Agent "+getAID().getName()+" is ready.");
		
		addBehaviour(new CyclicBehaviour(){

			@Override
			public void action() {
				//Receive msg from other agent
				ACLMessage msg = receive();
				if(msg != null) {
					JOptionPane.showMessageDialog(null,"radio msg "+msg.getContent());
				}
				else
					block();
				
			}
			
		});
	}

}
