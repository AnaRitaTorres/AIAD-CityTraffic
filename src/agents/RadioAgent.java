package agents;

import javax.swing.JOptionPane;

import sajas.core.Agent;
import sajas.core.behaviours.CyclicBehaviour;

import jade.lang.acl.ACLMessage;



@SuppressWarnings("serial")
public class RadioAgent extends Agent{
	
	
	//TODO vai ser necessario??
	//TODO melhorias - esquivar de zonas de transito
	
	private static int IDNumber=0;
	private int ID;
		
	public RadioAgent() {
		IDNumber++;
		ID=IDNumber;
	}
	
	public int getID() {
		return ID;
	}
			
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
