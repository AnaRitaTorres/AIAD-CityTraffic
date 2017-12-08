//TODO(4) fazer experiencias com numero diferente de carros (e semaforos?) e 
//tirar estatisticas do numero de colisoes, distancia percorrida, tempo demorado, tempo de espera em semaforos, tempo total que os carros estao parados durante o percurso(semaforos+transito)

//TODO melhorias implementar sentidos do transito

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
	private ArrayList<MyNode> lightsNodes = new ArrayList<MyNode>();
	private ArrayList<MyNode> carsNodes = new ArrayList<MyNode>();
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
		
	}
	
	public void TransformNodes( ArrayList<GraphNode> graphNodes) {
		
		for(int i=0; i < graphNodes.size(); i++) {
			OvalNetworkItem o = new OvalNetworkItem(graphNodes.get(i).getX(),graphNodes.get(i).getY());
			MyNode n = new MyNode(o, graphNodes.get(i).getX(),graphNodes.get(i).getY());
			nodes.add(n);
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
		c.connect2Nodes(nodes, 190, 175, 160, 170);
		c.connect2Nodes(nodes, 205, 190,210 , 160);
	}
	
	public void buildModel() {
		
		semColor.addElement(Color.red);
		semColor.addElement(Color.green);
		semColor.addElement(Color.orange);
		buildGraph();
		
	}
	
	public void buildSchedule() {
		
		/*class ChangeLight extends BasicAction {
			public void execute() {
				if(color==2) {
					lightAgents.get(0).changeColor(semColor.get(color));
					color=0;
				}
				else {
					lightAgents.get(0).changeColor(semColor.get(color));
					color++;
				}
				displaySurf.updateDisplay(); 
			}
			
		}*/
		
		//ChangeLight run = new ChangeLight();
		//schedule.scheduleActionBeginning(1000,run,500.0);
		
		/*schedule.scheduleActionAtInterval(time, new BasicAction() {
			public void execute() {
				
		    	if(color==0) {
		    		lightAgents.get(0).changeColor(semColor.get(color));  
		    		color++;
		    		time = time +2000;
		    	 }
		    	displaySurf.updateDisplay(); 
		     }
		},2000);
		
		schedule.scheduleActionBeginning(time, new BasicAction() {
			public void execute() {
				
		    	if(color==1) {
		    		lightAgents.get(0).changeColor(semColor.get(color));  
		    		color++;
		    		time= time+2000;
		    	 }
		    	 displaySurf.updateDisplay(); 
			 }
		},2000);
		
		schedule.scheduleActionBeginning(time, new BasicAction() {
			public void execute() {
				
		    	if(color==2) {
		    		lightAgents.get(0).changeColor(semColor.get(color));  
		    		color=0;
		    		time=time+2000;
		    	 }
		    	 displaySurf.updateDisplay(); 
			 }
		},2000);*/
	}
	
	public void buildDisplay() {

		//TODO(10) na janela das settings pro para dar para alterar numero de agentes
		
		Network2DDisplay display = new Network2DDisplay (nodes,WIDTH,HEIGHT);
		display.setNodesVisible(false);
		Network2DDisplay display1 = new Network2DDisplay (lightsNodes, WIDTH,HEIGHT);
		Network2DDisplay display2 = new Network2DDisplay (carsNodes, WIDTH,HEIGHT);
		displaySurf.addDisplayableProbeable (display, "City Traffic");
		displaySurf.addDisplayableProbeable (display2, "CityTraffic");
		displaySurf.addDisplayableProbeable (display1, "City");
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
				//TrafficLightAgent tLight1 = new TrafficLightAgent(160,50);
				TrafficLightAgent tLight = new TrafficLightAgent(100,110,displaySurf);
				lightAgents.add(tLight);
				//lightAgents.add(tLight1);
				mainContainer.acceptNewAgent("Light" + 0, tLight).start();
				//mainContainer.acceptNewAgent("Light" + 1, tLight1).start();
				
				
				MyNode n = new MyNode(tLight.getS(), tLight.getX(),tLight.getY());
				//MyNode n1 = new MyNode(tLight1.getX(),tLight1.getY(),tLight1.getS());
				
				lightsNodes.add(n);
				//agents.add(n1);
				
				//receiverLight = tLight.getAID();
			//}
				
				
				//55,110 / 100,60
			
			//create vehicles
			//for(int i=0; i < numVehicles;i++) {
				java.util.Random r = new java.util.Random();
				//int velocity = r.nextInt(1500) + 500;	DESCOMENTAR
				VehicleAgent vehicle = new VehicleAgent(55, 110, 1000, vehicleAgents, lightAgents, graph, carsNodes, displaySurf);
				vehicleAgents.add(vehicle);
				mainContainer.acceptNewAgent("Vehicle" + 1, vehicle).start();
				
				vehicle = new VehicleAgent(85, 110, 2000, vehicleAgents, lightAgents, graph, carsNodes, displaySurf);
				vehicleAgents.add(vehicle);
				mainContainer.acceptNewAgent("Vehicle" + 2, vehicle).start();
				
			//}
						
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