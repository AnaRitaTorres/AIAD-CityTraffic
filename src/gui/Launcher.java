//TODO fazer experiencias com numero diferente de carros (e semaforos?) para por os resultados dessas estatisticas no relatorio
//TODO por estatisticas na janela em direto

//TODO melhorias implementar sentidos do transito, dar para por semaforos em qualquer sitio e alterer o numero de lights agenst na janelinha

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
	private static final int N_LIGHTS = 9;
	private static final int N_NODES = 100;
	
	private int numNodes = N_NODES;
	private Graph graph;
	private Statistics stats = new Statistics();
	private ArrayList<GraphNode> graphNodes = new ArrayList<GraphNode>();
	private ArrayList<MyNode> nodes = new ArrayList<MyNode>();
	private ArrayList<MyNode> lightsNodes = new ArrayList<MyNode>();
	private ArrayList<MyNode> carsNodes = new ArrayList<MyNode>();
	private ArrayList<GraphNode> crossroadNodes = new ArrayList<GraphNode>();
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
		
	public void readFromFile(String file) {
		
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
				
				GraphNode n = new GraphNode(x,y);
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
		
		if(!runInBatchMode) {
			buildModel();
			buildDisplay();
		}
		
		displaySurf.display();
	}
	
	public void buildGraph() {
		groupByY();
		groupByX();
		groupByDirect();
		TransformNodes(graph.getNodes());
		crossRoads();
	}
	
	
	public void connectY(int x, int yi, int yf, int inc) {
		while(yi!=yf) {
			ConnectGraphNodes(x,yi,x,yi+inc);
			yi=yi+inc;
		}
	}
	
	public void connectX(int y, int xi, int xf, int inc) {
		while(xi!=xf) {
			ConnectGraphNodes(xi,y,xi+inc,y);
			xi=xi+inc;
		}
	}
	
	public void groupByDirect() {
		ConnectGraphNodes(55,350,70,360);
		ConnectGraphNodes(145,360,160,350);
		ConnectGraphNodes(160,350,175,340);
		ConnectGraphNodes(245,340,265,310);
		ConnectGraphNodes(265,310,280,300);
		ConnectGraphNodes(280,300,295,260);
		ConnectGraphNodes(330,240,350,210);
		ConnectGraphNodes(190,160,205,210);
	}
	
	public void groupByX() {
		connectX(50,25,205,15);
		connectX(110,25,205,15);
		connectX(240,25,205,15);
		connectX(50,215,245,15);
		connectX(50,265,280,15);
		connectX(50,205,215,10);
		connectX(50,245,265,20);
		connectX(130,295,310,15);
		connectX(130,310,350,10);
		connectX(110,205,215,10);
		connectX(110,215,245,15);
		connectX(110,245,265,20);
		connectX(110,265,280,15);
		connectX(180,40,55,15);
		connectX(280,40,55,15);
		connectX(300,130,145,15);
		connectX(350,25,55,15);
		connectX(360,70,145,15);
		connectX(340,175,205,15);
		connectX(340,215,245,15);
		connectX(340,205,215,10);
		connectX(240,295,310,15);
		connectX(240,310,330,10);
		connectX(210,330,350,20);
		connectX(240,280,295,15);
		
	}
	
	public void groupByY() {
		connectY(25,50,350,10);
		connectY(40,110,180,10);
		connectY(40,280,350,10);
		connectY(55,110,180,10);
		connectY(55,280,350,10);
		connectY(70,110,360,10);
		connectY(85,110,360,10);
		connectY(100,50,360,10);
		connectY(130,110,210,10);
		connectY(130,300,360,10);
		connectY(145,110,210,10);
		connectY(145,300,360,10);
		connectY(160,50,350,10);
		connectY(175,240,340,10);
		connectY(190,160,340,10);
		connectY(205,210,340,10);
		connectY(215,50,340,10);
		connectY(230,50,340,10);
		connectY(245,50,340,10);
		connectY(265,50,310,10);
		connectY(280,50,300,10);
		connectY(295,130,260,10);
		connectY(320,130,240,10);
		connectY(330,130,240,10);
		connectY(350,130,210,10);
		connectY(205,50,110,30);
		connectY(130,210,240,30);
		connectY(145,210,240,30);
		connectY(115,110,360,10);
			
	}
	
	public void TransformNodes( ArrayList<GraphNode> graphNodes) {
		
		
		for(int i=0; i < graphNodes.size(); i++) {
			OvalNetworkItem o = new OvalNetworkItem(graphNodes.get(i).getX(),graphNodes.get(i).getY());
			MyNode n = new MyNode(o, graphNodes.get(i).getX(),graphNodes.get(i).getY());
			nodes.add(n);
		}
		
		for(int i=0; i < graphNodes.size(); i++) {
			for(int x=0; x < nodes.size(); x++) {
				if(graphNodes.get(i).getX()== nodes.get(x).getX() && graphNodes.get(i).getY()== nodes.get(x).getY()) {
					MyNode n= nodes.get(x);
					if(graphNodes.get(i).getAdj().size()!=0) {
						for(int j=0; j < graphNodes.get(i).getAdj().size(); j++ ) {
							for(int w=0; w < nodes.size(); w++) {
								if(graphNodes.get(i).getAdj().get(j).getX() == nodes.get(w).getX() && graphNodes.get(i).getAdj().get(j).getY() == nodes.get(w).getY()) {
									n.makeEdgeToFrom(nodes.get(w), 5, Color.magenta);
								}
								
							}
						}
					}
				}
			}
		}
		
		
	}
	
	
	public void ConnectGraphNodes(int xi, int yi, int xf, int yf) {
			
		for(int i=0; i < graph.getNodes().size(); i++) {
			if(graph.getNodes().get(i).getX()==xi && graph.getNodes().get(i).getY()==yi) {
				for(int j=0; j < graph.getNodes().size(); j++) {
					if(graph.getNodes().get(j).getX()==xf && graph.getNodes().get(j).getY()==yf) {
						graph.getNodes().get(i).addAdj(graph.getNodes().get(j));
						graph.getNodes().get(j).addAdj(graph.getNodes().get(i));
					}
				}
			}
		}
		
	}
	
	
	public void crossRoads() {
		 
		addCrossroad(295,240);
		addCrossroad(265,110);
		addCrossroad(160,110);
		addCrossroad(190,240);
		addCrossroad(40,350);
	}
	
	public void addCrossroad(int x, int y) {
		for(int i=0; i < graphNodes.size(); i++) {
			if(graphNodes.get(i).getX()== x && graphNodes.get(i).getY()==y) {
				crossroadNodes.add(graphNodes.get(i));
			}
		}
	}
	
	public void buildModel() {
		buildGraph();
	}
	
	public void buildDisplay() {
		
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
	
	
	public void createTrafficLight(int x, int y,Vector<TrafficLightAgent> lightAgents) {
		
		TrafficLightAgent t = new TrafficLightAgent(x,y,lightAgents,crossroadNodes,graphNodes,displaySurf);
		lightAgents.add(t);
		MyNode n = new MyNode(t.getS(), t.getX(),t.getY());
		lightsNodes.add(n);
		
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
		readFromFile(file);
		launchAgents();
		
	}
	
	private void launchAgents() {
		
		radioAgents= new Vector<RadioAgent>();
		vehicleAgents= new Vector<VehicleAgent>();
		lightAgents= new Vector<TrafficLightAgent>();
				
		try {
			
			//create radios
			for(int i=0; i < numRadios;i++) {
				RadioAgent radio = new RadioAgent();
				radioAgents.add(radio);
				mainContainer.acceptNewAgent("Radio" + i, radio).start();			
			}
			
				createTrafficLight(145,110,lightAgents);
				createTrafficLight(245,110,lightAgents);
				createTrafficLight(295,230,lightAgents);
				createTrafficLight(175,240,lightAgents);
				createTrafficLight(40,340,lightAgents);
				createTrafficLight(160,100,lightAgents);
				createTrafficLight(265,100,lightAgents);
				createTrafficLight(280,240,lightAgents);
				createTrafficLight(190,250,lightAgents);
				createTrafficLight(25,350,lightAgents);
				
				for(int i=0; i < lightAgents.size(); i++) {
					mainContainer.acceptNewAgent("Light" + i, lightAgents.get(i)).start();
				}
				
								
			//create vehicles
			for(int i=0; i < numVehicles;i++) {
				java.util.Random r = new java.util.Random();
				int p = r.nextInt(11);
				int velocity;
				if(p <= 7){
					velocity = r.nextInt(60) + 5;
				}
				else{
					velocity = r.nextInt(60) + 65;
				}
				r = new java.util.Random();
				int pos = r.nextInt(graph.getNodes().size());
				GraphNode posInit = graph.getNodes().get(pos);
				GraphNode posEnd = posInit;//TODO
				//55+30, 110
				VehicleAgent vehicle = new VehicleAgent(posInit, posEnd, velocity, vehicleAgents, lightAgents, graph, carsNodes, displaySurf, stats);
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