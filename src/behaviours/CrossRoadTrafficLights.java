package behaviours;


import java.util.ArrayList;
import sajas.core.behaviours.*;

import agents.TrafficLightAgent;
import graph.GraphNode;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("serial")
public class CrossRoadTrafficLights extends Behaviour{
	
	private TrafficLightAgent light1;
	private TrafficLightAgent light2;
	private int step = 0;
	private ACLMessage reply;
	private ArrayList<GraphNode> crossroads;
	private ArrayList<GraphNode> graphNodes;
	private MessageTemplate mt;
	
	public CrossRoadTrafficLights(TrafficLightAgent light1,TrafficLightAgent light2,ArrayList<GraphNode> crossroads,ArrayList<GraphNode> graphNodes) {
		
		this.light1=light1;
		this.light2=light2;
		this.crossroads=crossroads;
		this.graphNodes=graphNodes;
		
	}
	
	public boolean sameCrossroad(){
		
		int x1=light1.getX();
		int y1=light1.getY();
		int x2 = light2.getX();
		int y2=light2.getY();
		GraphNode n1=null,n2=null;
		boolean same=false;
		
		for(int i=0; i < graphNodes.size(); i++) {
			if(graphNodes.get(i).getX()== x1 && graphNodes.get(i).getY()==y1) {
				n1=graphNodes.get(i);
			}
			else if(graphNodes.get(i).getX()== x2 && graphNodes.get(i).getY()==y2) {
				n2=graphNodes.get(i);
			}
		}
		
		if(n1!= null && n2!=null && n1!=n2) {
			for(int i=0; i < crossroads.size(); i++) {
				if(crossroads.get(i).getAdj().contains(n1) && crossroads.get(i).getAdj().contains(n2))
					same=true;
			}
		}
		
		return same;
	}
	
	@Override
	public void action() {
		
		//check if they are connected to the same crossroad
		switch(step){
		case 0:
			if(sameCrossroad()) {
				step=1;
			}
			break;
		case 1:
			//if they are, ask each other for the color
			ACLMessage cfp= new ACLMessage(ACLMessage.CFP);
			cfp.addReceiver(light2.getAID());
			cfp.setContent("Cor?");
			cfp.setConversationId("cor1");
			cfp.setReplyWith("cfp"+System.currentTimeMillis());
			myAgent.send(cfp);
			mt= MessageTemplate.and(MessageTemplate.MatchConversationId("cor1"),MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
			step=2;
			break;
		case 2:
			//wait for the answer and with that info change colors
			
			reply= light1.receive(mt);
			if(reply != null){
				step=3;
			}
			else {
				block();
			}
			break;
		case 3:
			if(reply.getContent().equals("red")) {
				light1.changeColor("green");
			}
			else if(reply.getContent().equals("green")) {
				light1.changeColor("red");
			}
			step=4;
			break;
		}
		
	}
	
	@Override
	public boolean done() {
		return step == 4;
	}
}