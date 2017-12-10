package behaviours;

import sajas.core.behaviours.*;

import java.util.ArrayList;
import java.util.Vector;

import agents.TrafficLightAgent;
import graph.GraphNode;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.core.AID;


@SuppressWarnings("serial")
public class FindCrossroadTrafficLight extends Behaviour{
	
	private TrafficLightAgent light;
	private Vector<TrafficLightAgent> lights;
	private int step=0;
	private MessageTemplate mt;
	private int repliesCnt = 0;
	private boolean same=false;
	private ArrayList<AID> aid = new ArrayList<AID>();
	private ArrayList<GraphNode> graphNodes;
	private ArrayList<GraphNode> crossroads;
	
	public FindCrossroadTrafficLight(TrafficLightAgent light, Vector<TrafficLightAgent> lights,ArrayList<GraphNode> crossroads,ArrayList<GraphNode> graphNodes) {
		this.light=light;
		this.lights=lights;
		this.crossroads=crossroads;
		this.graphNodes=graphNodes;
	}
	
public boolean sameCrossroad(String position){
		
		int x1 = light.getX();
		int y1 = light.getY();
		
		String[] positions = position.split(";");
		
		int x2 = Integer.parseInt(positions[0]);
		int y2 = Integer.parseInt(positions[1]);
		
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
		
		if(n1!= null && n2!=null) {
			for(int i=0; i < crossroads.size(); i++) {
				if(crossroads.get(i).getAdj().contains(n1) && n2.getAdj().contains(crossroads.get(i))){
					same=true;
				}
				if(crossroads.get(i).getAdj().contains(n2) && n1.getAdj().contains(crossroads.get(i))) {
					same=true;
				}
				if(crossroads.get(i).getAdj().contains(n1) && crossroads.get(i).getAdj().contains(n2)) {
					same=true;
				}
				if(n2.getAdj().contains(crossroads.get(i)) && n1.getAdj().contains(crossroads.get(i))) {
					same=true;
				}
						
			}
		}
		
		return same;
	}
	
	@Override
	public void action() {
		switch (step){
		case 0:
			//send the cfp to all traffic lights
			ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
			for(int i = 0; i < lights.size(); i++){
				cfp.addReceiver(lights.elementAt(i).getAID());
			}
			cfp.setContent("position");
			cfp.setConversationId("position1");
			cfp.setReplyWith("cfp"+System.currentTimeMillis());
			myAgent.send(cfp);
			mt = MessageTemplate.and(MessageTemplate.MatchConversationId("position1"), MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));			
			step = 1;
			break;
		case 1:
			//receive all answers from traffic lights
			ACLMessage reply = light.receive(mt); 
			if(reply != null){
				if(sameCrossroad(reply.getContent())) {
					light.setHaspair(true);
					for(int i=0; i < lights.size(); i++) {
						if(lights.get(i).getAID().equals(reply.getSender())) {
							if(light.getX() > lights.get(i).getX()) {
								light.setPair(light,lights.get(i));
							}
							else if(light.getX() < lights.get(i).getX()) {
								light.setPair(lights.get(i),light);
							}
							else {
								if(light.getY() > lights.get(i).getY()) {
									light.setPair(light,lights.get(i));
								}
								else if(light.getY() > lights.get(i).getY()){
									light.setPair(lights.get(i),light);
								}
							}
							
							myAgent.addBehaviour(new CrossRoadTrafficLights(light.getPair()));
						}
							
					}
				}
				repliesCnt++;
				if(same || repliesCnt== lights.size()){
					step = 3;
				}
			}
			else
				block();
			
			break;
		}
		
	}
	
	
	
	@Override
	public boolean done() {
		return step==3;
	}
	
	
}