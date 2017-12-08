package gui;

import java.util.Vector;
import java.util.ArrayList;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import sajas.sim.repast3.Repast3Launcher;
import sajas.core.Runtime;
import sajas.wrapper.ContainerController;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import javafx.scene.Node;
import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Network2DDisplay;
import uchicago.src.sim.gui.OvalNetworkItem;

import agents.*;
import graph.*;

public class Launcher extends Repast3Launcher {
	
	private static final boolean BATCH_MODE = true;
	private static final int HEIGHT = 400;
	private static final int WIDTH = 400;
	private static final int N_RADIOS = 0;
	private static final int N_VEHICLES = 2;
	private static final int N_LIGHTS = 2;
	private static final int N_NODES = 100;
	
	private int numNodes = N_NODES;
	private Graph graph;
	private int color=0;
	private int time = 2000;
	private Schedule schedule;
	private ArrayList<GraphNode> graphNodes = new ArrayList<GraphNode>();
	private Connection c = new Connection();
	private ArrayList<MyNode> nodes = new ArrayList<MyNode>();
	private ArrayList<MyNode> agents = new ArrayList<MyNode>();
	private ContainerController mainContainer;
	private boolean runInBatchMode;
	private DisplaySurface displaySurf;
	private int numRadios = N_RADIOS;
	private int numVehicles = N_VEHICLES;
	private int numLights = N_LIGHTS;
	private String file = "map1.txt";
	public Vector<RadioAgent> radioAgents;
	public Vector<VehicleAgent> vehicleAgents;
	public Vector<TrafficLightAgent> lightAgents;
	Vector<Color> semColor = new Vector<Color>(3);
		
	public Launcher(boolean runInBatchMode) {
		super();
		this.runInBatchMode = runInBatchMode;
	}
		
	public int getNumNodes() {
		return numNodes;
	}

	public void setNumNodes(int numNodes) {
		this.numNodes = numNodes;
	}

	public int getNumRadios() {
		return numRadios;
	}

	public void setNumRadios(int numRadios) {
		this.numRadios = numRadios;
	}

	public int getNumVehicles() {
		return numVehicles;
	}

	public void setNumVehicles(int numVehicles) {
		this.numVehicles = numVehicles;
	}

	public int getNumLights() {
		return numLights;
	}

	public void setNumLights(int numLights) {
		this.numLights = numLights;
	}
	
	public String getName () {
	    return "City Traffic";
	}
	
	/*public Schedule getSchedule() {
	    return schedule;
	}*/
	
	public void readFromFile(String file) {
		
		ArrayList<GraphNode> adj = new ArrayList<GraphNode>();
			
		try{
			
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			
			strLine = br.readLine();
			
			String[] nNodes = strLine.split("!");
						
			int num = Integer.valueOf(nNodes[0]);
			
			for(int i=0; i < num; i++) {
				strLine = br.readLine();
				String[] nodesRead = strLine.split(";");

				int x = Integer.valueOf(nodesRead[0]);
				int y= Integer.valueOf(nodesRead[1]);
				
				GraphNode n = new GraphNode(x,y,adj);
				graphNodes.add(n);
			}
				
			in.close();
			}catch (Exception e){
				System.err.println("FILE STREAM ERROR: " + e.getMessage());
			}
		
		graph = new Graph(graphNodes);
		
	}
	
	@Override
	public void setup() {
		super.setup();
		
		//schedule = null;
		if(displaySurf != null) {
			displaySurf.dispose();
		}
		displaySurf = null;
				
		displaySurf = new DisplaySurface(this,"City Traffic Window 1");
		registerDisplaySurface("City Traffic Window 1",displaySurf);
		//schedule = new Schedule ();
	}
	
	@Override
	public void begin() {
		super.begin();
		readFromFile(file);
		if(!runInBatchMode) {
			buildModel();
			buildDisplay();
			buildSchedule();
		}
		
		displaySurf.display();
	}
	
	public void buildGraph() {
		
		ConnectNodes();
		TransformNodes(graphNodes);
		ConnectNodesVisual();
		crossRoads();
		
	}
	
	public void TransformNodes( ArrayList<GraphNode> graphNodes) {
		
		for(int i=0; i < graphNodes.size(); i++) {
			OvalNetworkItem o = new OvalNetworkItem(graphNodes.get(i).getX(),graphNodes.get(i).getY());
			MyNode n = new MyNode(graphNodes.get(i).getX(),graphNodes.get(i).getY(),o);
			nodes.add(n);
		}
	}
	
	public void removeEgdeGraph() {
		for(int i=0; i < graphNodes.size(); i++) {
			
			if(graphNodes.get(i).getY()==240) {
				if(graphNodes.get(i).getX()==310) {
					graphNodes.get(i).removeAdj(310,250);
				}
				else if(graphNodes.get(i).getX()==320) {
					graphNodes.get(i).removeAdj(320,250);
				}
				else if(graphNodes.get(i).getX()==330) {
					graphNodes.get(i).removeAdj(330, 250);
				}
			}
			else if(graphNodes.get(i).getY()==50) {
				if(graphNodes.get(i).getX()==115) {
					graphNodes.get(i).removeAdj(115, 110);
				}
				else if(graphNodes.get(i).getX()==130) {
					graphNodes.get(i).removeAdj(130, 110);
					
				}
				else if(graphNodes.get(i).getX()==145) {
					graphNodes.get(i).removeAdj(145, 110);
				}
				else if(graphNodes.get(i).getX()==175) {
					graphNodes.get(i).removeAdj(175, 240);
					graphNodes.get(i).removeAdj(175, 110);
					
				}
				else if(graphNodes.get(i).getX()==190) {
					graphNodes.get(i).removeAdj(190, 160);
					graphNodes.get(i).removeAdj(190, 110);
				}
				else if(graphNodes.get(i).getX()==205) {
					graphNodes.get(i).removeAdj(205, 210);
					graphNodes.get(i).removeAdj(205, 50);
				}
				else if(graphNodes.get(i).getX()==40) {
					graphNodes.get(i).removeAdj(40, 110);
				}
				else if(graphNodes.get(i).getX()==55) {
					graphNodes.get(i).removeAdj(55, 110);
				}
				else if(graphNodes.get(i).getX()==70) {
					graphNodes.get(i).removeAdj(70, 110);
				}
				else if(graphNodes.get(i).getX()==85) {
					graphNodes.get(i).removeAdj(85, 110);
				}
			}
			else if(graphNodes.get(i).getY()==180){
				if(graphNodes.get(i).getX()==40) {
					graphNodes.get(i).removeAdj(40, 280);
					graphNodes.get(i).removeAdj(40, 240);
				}
				else if(graphNodes.get(i).getX()==55) {
					graphNodes.get(i).removeAdj(55, 280);
					graphNodes.get(i).removeAdj(55, 240);
				}
			}
			else if(graphNodes.get(i).getY()==210) {
				if(graphNodes.get(i).getX()==130){
					graphNodes.get(i).removeAdj(130,300);
				}
				else if(graphNodes.get(i).getX()==145){
					graphNodes.get(i).removeAdj(145, 300);
				}
				
			}
			else if (graphNodes.get(i).getY()==250 && graphNodes.get(i).getX()==330){
				graphNodes.get(i).removeAdj(350, 210);
			}
			else if(graphNodes.get(i).getY()== 130 && graphNodes.get(i).getX()==310) {
				graphNodes.get(i).removeAdj(310, 240);
			}
			else if(graphNodes.get(i).getY()==240) {
				if(graphNodes.get(i).getX()==115) {
					graphNodes.get(i).removeAdj(160, 240);
				}
				else if (graphNodes.get(i).getX()==130) {
					graphNodes.get(i).removeAdj(130, 300);
				}
				else if (graphNodes.get(i).getX()==145) {
					graphNodes.get(i).removeAdj(145, 300);
				}
				else if (graphNodes.get(i).getX()==130) {
					graphNodes.get(i).removeAdj(130, 210);
				}
				else if (graphNodes.get(i).getX()==145) {
					graphNodes.get(i).removeAdj(145, 210);
				}
			}
			else if(graphNodes.get(i).getY()==110) {
				if(graphNodes.get(i).getX()==175) {
					graphNodes.get(i).removeAdj(175, 240);
				}
				else if(graphNodes.get(i).getX()==190) {
					graphNodes.get(i).removeAdj(190, 160);
				}
				else if(graphNodes.get(i).getX()==205) {
					graphNodes.get(i).removeAdj(205,210);
				}
				else if(graphNodes.get(i).getX()==205) {
					graphNodes.get(i).removeAdj(205,50);
				}
			}
			else if(graphNodes.get(i).getY()==210) {
				if(graphNodes.get(i).getX()==130) {
					graphNodes.get(i).removeAdj(130, 240);
				}
				else if(graphNodes.get(i).getX()==145) {
					graphNodes.get(i).removeAdj(145, 240);
				}
			}
			
			else if(graphNodes.get(i).getY()==280) {
				if(graphNodes.get(i).getX()==40) {
					graphNodes.get(i).removeAdj(40, 240);
				}else if(graphNodes.get(i).getX()==55) {
					graphNodes.get(i).removeAdj(55, 240);
				}
			}
			else if(graphNodes.get(i).getY()==300) {
				if(graphNodes.get(i).getX()==130) {
					graphNodes.get(i).removeAdj(130, 240);
				}else if(graphNodes.get(i).getX()==145) {
					graphNodes.get(i).removeAdj(145, 240);
				}
				
			}
		}	
		
		
	}
	
	public void ConnectNodes() {
		graph.connectVertical(graphNodes);
		graph.connectStreetY(graphNodes, 110);
		graph.connectStreetY(graphNodes, 50);
		graph.connectStreetY(graphNodes,360);
		graph.connectStreetY(graphNodes, 240);
		graph.connectToFrom(graphNodes,55, 25, 350);
		graph.connectToFrom(graphNodes,150, 90,130);
		graph.connectToFrom(graphNodes,350, 295,130);
		graph.connectToFrom(graphNodes, 265, 175, 340);
		graph.connectToFrom(graphNodes,330 , 295, 250);
		graph.connect2Nodes(graphNodes,350, 330, 210 ,250);
		graph.connect2Nodes(graphNodes, 190, 175, 160, 170);
		graph.connect2Nodes(graphNodes, 205, 190,210 , 160);
		graph.connect2Nodes(graphNodes, 265, 245, 310, 340);
		graph.connect2Nodes(graphNodes,280,265,300,310);
		graph.connect2Nodes(graphNodes, 295, 280, 260, 300);
		graph.connect2Nodes(graphNodes, 70, 55, 360, 350);
		graph.connect2Nodes(graphNodes,160, 145, 350, 360);
		graph.connect2Nodes(graphNodes, 175, 160, 340, 350);
		graph.connect2Nodes(graphNodes, 350, 330, 210, 210);
		graph.connect2Nodes(graphNodes, 145, 130, 300, 300);
		graph.connect2Nodes(graphNodes, 55, 40, 280, 280);
		graph.connect2Nodes(graphNodes, 55, 40, 180, 180);
		
		removeEgdeGraph();
		
	
	}
	
	
	
	public void removeEdgeVisual() {
		for(int i=0; i < nodes.size(); i++) {
			if(nodes.get(i).getY()==240) {
				if(nodes.get(i).getX()==310) {
					nodes.get(i).removeConnection(310,250,nodes);
				}
				else if(nodes.get(i).getX()==320) {
					nodes.get(i).removeConnection(320,250,nodes);
				}
				else if(nodes.get(i).getX()==330) {
					nodes.get(i).removeConnection(330, 250,nodes);
				}
			}
			else if(nodes.get(i).getY()==50) {
				if(nodes.get(i).getX()==115) {
					nodes.get(i).removeConnection(115, 110, nodes);
				}
				else if(nodes.get(i).getX()==130) {
					nodes.get(i).removeConnection(130, 110, nodes);
					
				}
				else if(nodes.get(i).getX()==145) {
					nodes.get(i).removeConnection(145, 110, nodes);
				}
				else if(nodes.get(i).getX()==175) {
					nodes.get(i).removeConnection(175, 240, nodes);
					nodes.get(i).removeConnection(175, 110, nodes);
					
				}
				else if(nodes.get(i).getX()==190) {
					nodes.get(i).removeConnection(190, 160, nodes);
					nodes.get(i).removeConnection(190, 110, nodes);
				}
				else if(nodes.get(i).getX()==205) {
					nodes.get(i).removeConnection(205, 210, nodes);
					nodes.get(i).removeConnection(205, 50, nodes);
				}
				else if(nodes.get(i).getX()==40) {
					nodes.get(i).removeConnection(40, 110, nodes);
				}
				else if(nodes.get(i).getX()==55) {
					nodes.get(i).removeConnection(55, 110, nodes);
				}
				else if(nodes.get(i).getX()==70) {
					nodes.get(i).removeConnection(70, 110, nodes);
				}
				else if(nodes.get(i).getX()==85) {
					nodes.get(i).removeConnection(85, 110, nodes);
				}
			}
			else if(nodes.get(i).getY()==180){
				if(nodes.get(i).getX()==40) {
					nodes.get(i).removeConnection(40, 280, nodes);
					nodes.get(i).removeConnection(40, 240, nodes);
				}
				else if(nodes.get(i).getX()==55) {
					nodes.get(i).removeConnection(55, 280, nodes);
					nodes.get(i).removeConnection(55, 240, nodes);
				}
			}
			else if(nodes.get(i).getY()==210) {
				if(nodes.get(i).getX()==130){
					nodes.get(i).removeConnection(130,300, nodes);
				}
				else if(nodes.get(i).getX()==145){
					nodes.get(i).removeConnection(145, 300, nodes);
				}
			}
							
			
			else if (nodes.get(i).getY()==250 && nodes.get(i).getX()==330){
				nodes.get(i).removeConnection(350, 210, nodes);
			}
			else if(nodes.get(i).getY()== 130 && nodes.get(i).getX()==310) {
				nodes.get(i).removeConnection(310, 240, nodes);
			}
			else if(nodes.get(i).getY()==240) {
				if(nodes.get(i).getX()==115) {
					nodes.get(i).removeConnection(160, 240, nodes);
				}
				else if (nodes.get(i).getX()==130) {
					nodes.get(i).removeConnection(130, 300, nodes);
				}
				else if (nodes.get(i).getX()==145) {
					nodes.get(i).removeConnection(145, 300, nodes);
				}
				else if (nodes.get(i).getX()==130) {
					nodes.get(i).removeConnection(130, 210, nodes);
				}
				else if (nodes.get(i).getX()==145) {
					nodes.get(i).removeConnection(145, 210, nodes);
				}
			}
			else if(nodes.get(i).getY()==110) {
				if(nodes.get(i).getX()==175) {
					nodes.get(i).removeConnection(175, 240, nodes);
				}
				else if(nodes.get(i).getX()==190) {
					nodes.get(i).removeConnection(190, 160,nodes);
				}
				else if(nodes.get(i).getX()==205) {
					nodes.get(i).removeConnection(205, 210,nodes);
				}
				else if(nodes.get(i).getX()==205) {
					nodes.get(i).removeConnection(205, 50,nodes);
				}
				
				
			}
			else if(nodes.get(i).getY()==210) {
				if(nodes.get(i).getX()==130) {
					nodes.get(i).removeConnection(130, 240, nodes);
				}
				else if(nodes.get(i).getX()==145) {
					nodes.get(i).removeConnection(145, 240, nodes);
				}
				
			}
			else if(nodes.get(i).getY()==280) {
				if(nodes.get(i).getX()==40) {
					nodes.get(i).removeConnection(40, 240, nodes);
				}else if(nodes.get(i).getX()==55) {
					nodes.get(i).removeConnection(55, 240, nodes);
				}
			}
			else if(nodes.get(i).getY()==300) {
				if(nodes.get(i).getX()==130) {
					nodes.get(i).removeConnection(130, 240, nodes);
				}else if(nodes.get(i).getX()==145) {
					nodes.get(i).removeConnection(145, 240, nodes);
				}
				
			}
		}
	}
	public void ConnectNodesVisual() {
		c.connectVertical(nodes);
		
		c.connectStreetY(nodes,110);
		c.connectStreetY(nodes, 50);
		c.connectStreetY(nodes,360);
		c.connectStreetY(nodes, 240);
		
		c.connectToFrom(nodes, 265, 175, 340);
		c.connectToFrom(nodes,55, 25, 350);
		c.connectToFrom(nodes,150, 90,130);
		c.connectToFrom(nodes,350, 295,130);
		c.connectToFrom(nodes,330 , 295, 250);
		
		c.connect2Nodes(nodes, 70, 55, 360, 350);
		c.connect2Nodes(nodes,160, 145, 350, 360);
		c.connect2Nodes(nodes, 175, 160, 340, 350);
		c.connect2Nodes(nodes, 265, 245, 310, 340);
		c.connect2Nodes(nodes,280,265,300,310);
		c.connect2Nodes(nodes, 295, 280, 260, 300);
		c.connect2Nodes(nodes,350, 330, 210 ,250);
		c.connect2Nodes(nodes, 205, 190,210 , 160);
		c.connect2Nodes(nodes, 350, 330, 210, 210);
		c.connect2Nodes(nodes, 145, 130, 300, 300);
		c.connect2Nodes(nodes, 55, 40, 280, 280);
		c.connect2Nodes(nodes, 55, 40, 180, 180);
				
		removeEdgeVisual();
		
	
	}
	
	public void crossRoads() {
		 
		OvalNetworkItem o1 = new OvalNetworkItem(295,240);
		OvalNetworkItem o2 = new OvalNetworkItem(265,110);
		OvalNetworkItem o3 = new OvalNetworkItem(160,110);
		OvalNetworkItem o4 = new OvalNetworkItem(190,240);
		OvalNetworkItem o5 = new OvalNetworkItem(40,350);
		
		o1.setColor(Color.black);
		o2.setColor(Color.black);
		o3.setColor(Color.black);
		o4.setColor(Color.black);
		o5.setColor(Color.black);
		
		MyNode c1 = new MyNode(295,240,o1);
		MyNode c2 = new MyNode(265,110,o2);
		MyNode c3 = new MyNode(160,110,o3);
		MyNode c4 = new MyNode(190,240,o4);
		MyNode c5 = new MyNode(40,350,o5);
		
		agents.add(c1);
		agents.add(c2);
		agents.add(c3);
		agents.add(c4);
		agents.add(c5);
		
		
	}
	
	public void buildModel() {
		
		semColor.addElement(Color.red);
		semColor.addElement(Color.green);
		semColor.addElement(Color.orange);
		buildGraph();
		
	}
	
	public void buildSchedule() {
		
		
	}
	
	public void buildDisplay() {

		Network2DDisplay display = new Network2DDisplay (nodes,WIDTH,HEIGHT);
		display.setNodesVisible(false);
		Network2DDisplay display2 = new Network2DDisplay (agents,WIDTH,HEIGHT);
		displaySurf.addDisplayableProbeable (display, "City Traffic");
		displaySurf.addDisplayableProbeable (display2, "City");
		displaySurf.addZoomable (display);
		displaySurf.setBackground (java.awt.Color.white);
		
		
	}
	
	@Override
	public String[] getInitParam() {
		String[] initParams = {"NumRadios", "NumVehicles", "NumLights"};
		return initParams;
	}
	
	@Override
	protected void launchJADE() {
		
		Runtime rt = Runtime.instance();
		Profile p = new ProfileImpl();
		mainContainer = rt.createMainContainer(p);
		 
		launchAgents();
	}
	
	private void launchAgents() {
		
		radioAgents= new Vector<RadioAgent>();
		vehicleAgents= new Vector<VehicleAgent>();
		lightAgents= new Vector<TrafficLightAgent>();
		//AID receiverLight = null;
		//AID receiverRadio = null;
		
		try {
			
			//create radios
			for(int i=0; i < numRadios;i++) {
				RadioAgent radio = new RadioAgent();
				radioAgents.add(radio);
				mainContainer.acceptNewAgent("Radio" + i, radio).start();			
			}
			
			//create traffic lights
			//for(int i=0; i < numLights;i++) {
				TrafficLightAgent tLight1 = new TrafficLightAgent(160,50, lightAgents,displaySurf);
				TrafficLightAgent tLight = new TrafficLightAgent(100,110,lightAgents,displaySurf);
				lightAgents.add(tLight);
				lightAgents.add(tLight1);
				mainContainer.acceptNewAgent("Light" + 0, tLight).start();
				mainContainer.acceptNewAgent("Light" + 1, tLight1).start();
				
				
				MyNode n = new MyNode(tLight.getX(),tLight.getY(),tLight.getS());
				MyNode n1 = new MyNode(tLight1.getX(),tLight1.getY(),tLight1.getS());
				
				agents.add(n);
				agents.add(n1);
				
				//receiverLight = tLight.getAID();
			//}
			
			//create vehicles
			int velocity = 1000;//só para testar
			int[] position = new int[2];;//so para testar
			for(int i=0; i < numVehicles;i++) {
				java.util.Random r = new java.util.Random();
				//int velocity = r.nextInt(1500) + 500;	DESCOMENTAR
				position[0] = 0;//TODO (4)por posiçoes do graph
				position[1] = 0;
				VehicleAgent vehicle = new VehicleAgent(position, velocity, lightAgents,displaySurf);
				vehicleAgents.add(vehicle);
				mainContainer.acceptNewAgent("Vehicle" + i, vehicle).start();
				
				//DESENHA O CARRO
				//MyNode n1 = new MyNode(vehicle.getX(),vehicle.getY(),vehicle.getS());
				//agents.add(n1);
				velocity += 1000;	//só para testar
				position[0] = 2;//só para testar
				position[1] = 2;//so para testar
			}
						
		}catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		
		boolean runMode = !BATCH_MODE; 
		
		SimInit init = new SimInit();
		init.setNumRuns(1);   // works only in batch mode
		init.loadModel(new Launcher(runMode), null, runMode);
	}
}