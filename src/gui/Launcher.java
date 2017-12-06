package gui;

import java.util.Vector;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import sajas.sim.repast3.Repast3Launcher;
import sajas.core.Runtime;
import sajas.wrapper.ContainerController;

import jade.core.AID;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;

import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.space.Object2DGrid;
import uchicago.src.sim.gui.Network2DDisplay;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.gui.OvalNetworkItem;
import uchicago.src.sim.gui.RectNetworkItem;
import uchicago.src.sim.gui.SimGraphics;
import agents.*;
import graph.*;

public class Launcher extends Repast3Launcher {
	
	private static final boolean BATCH_MODE = true;
	private static final int HEIGHT = 400;
	private static final int WIDTH = 400;
	private static final int N_RADIOS = 0;
	private static final int N_VEHICLES = 1;
	private static final int N_LIGHTS = 1;
	private static final int N_NODES = 100;
	
	private int numNodes = N_NODES;
	private Graph grafo;
	private ArrayList<GraphNode> nos = new ArrayList<GraphNode>();
	private Connection c = new Connection();
	private ArrayList<MyNode> nodes = new ArrayList<MyNode>();
	private ArrayList<MyNode> agents = new ArrayList<MyNode>();
	private ContainerController mainContainer;
	private Object2DGrid obj;
	private boolean runInBatchMode;
	private DisplaySurface displaySurf;
	private SimGraphics g;
	private int numRadios = N_RADIOS;
	private int numVehicles = N_VEHICLES;
	private int numLights = N_LIGHTS;
	private String file = "map1.txt";
	public Vector<RadioAgent> radioAgents;
	public Vector<VehicleAgent> vehicleAgents;
	public Vector<TrafficLightAgent> lightAgents;
		
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
				nos.add(n);
			}
				
			in.close();
			}catch (Exception e){
				System.err.println("FILE STREAM ERROR: " + e.getMessage());
			}
		
		grafo = new Graph(nos);
		
	}

	
	@Override
	public void setup() {
		super.setup();
						
		if(displaySurf != null) {
			displaySurf.dispose();
		}
		displaySurf = null;
				
		displaySurf = new DisplaySurface(this,"City Traffic Window 1");
		registerDisplaySurface("City Traffic Window 1",displaySurf);
		
	}
	
	@Override
	public void begin() {
		super.begin();
		readFromFile(file);
		if(!runInBatchMode) {
			buildModel();
			//buildSchedule();
			buildDisplay();
		}
		
		displaySurf.display();
	}
	
	public void buildGraph() {
		
		ConnectNodes();
		TransformNodes(nos);
		ConnectNodesVisual();
		
	}
	
	
	public void TransformNodes( ArrayList<GraphNode> nos) {
		
		for(int i=0; i < nos.size(); i++) {
			OvalNetworkItem o = new OvalNetworkItem(nos.get(i).getX(),nos.get(i).getY());
			MyNode n = new MyNode(nos.get(i).getX(),nos.get(i).getY(),o);
			nodes.add(n);
		}
	}
	
	public void ConnectNodes() {
		grafo.connectVertical(nos);
		grafo.connectStreetY(nos, 110);
		grafo.connectStreetY(nos, 50);
		grafo.connectStreetY(nos,360);
		grafo.connectStreetY(nos, 240);
		grafo.connectToFrom(nos,55, 25, 350);
		grafo.connectToFrom(nos,150, 90,130);
		grafo.connectToFrom(nos,350, 295,130);
		grafo.connectToFrom(nos, 265, 175, 340);
		grafo.connectToFrom(nos,330 , 295, 250);
		grafo.connect2Nodes(nos,350, 330, 210 ,250);
		grafo.connect2Nodes(nos, 190, 175, 160, 170);
		grafo.connect2Nodes(nos, 205, 190,210 , 160);
		grafo.connect2Nodes(nos, 265, 245, 310, 340);
		grafo.connect2Nodes(nos,280,265,300,310);
		grafo.connect2Nodes(nos, 295, 280, 260, 300);
		grafo.connect2Nodes(nos, 70, 55, 360, 350);
		grafo.connect2Nodes(nos,160, 145, 350, 360);
		grafo.connect2Nodes(nos, 175, 160, 340, 350);
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
		
		buildGraph();
	}
	
	public void buildSchedule() {
		System.out.println("Running BuildSchedule");
	}
	
	public void buildDisplay() {

		Network2DDisplay display = new Network2DDisplay (nodes,WIDTH,HEIGHT);
		display.setNodesVisible(true);
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
		AID receiverRadio = null;
		
		try {
			
			//create radios
			for(int i=0; i < numRadios;i++) {
				RadioAgent radio = new RadioAgent();
				radioAgents.add(radio);
				mainContainer.acceptNewAgent("Radio" + i, radio).start();			
			}
			
			//create traffic lights
			for(int i=0; i < numLights;i++) {
				TrafficLightAgent tLight = new TrafficLightAgent(25,50,obj);
				lightAgents.add(tLight);
				mainContainer.acceptNewAgent("Light" + i, tLight).start();
				OvalNetworkItem light = new OvalNetworkItem(25,50);
				light.setColor(Color.black);
				MyNode n = new MyNode(25,50,light);
				agents.add(n);
				
				//receiverLight = tLight.getAID();
			}
			
			//create vehicles
			for(int i=0; i < numVehicles;i++) {
				VehicleAgent vehicle = new VehicleAgent(lightAgents);
				vehicleAgents.add(vehicle);
				mainContainer.acceptNewAgent("Vehicle" + i, vehicle).start();
				
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