package gui;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

import sajas.sim.repast3.Repast3Launcher;
import sajas.core.Runtime;
import sajas.wrapper.ContainerController;

import jade.core.AID;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;

import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.util.Random;
import uchicago.src.sim.gui.AbstractGraphLayout;
import uchicago.src.sim.gui.Network2DDisplay;
import uchicago.src.sim.gui.OvalNetworkItem;

import agents.*;
import graph.*;

public class Launcher extends Repast3Launcher {
	
	private static final boolean BATCH_MODE = true;
	private static final int HEIGHT = 400;
	private static final int WIDTH = 400;
	private static final int N_RADIOS = 1;
	private static final int N_VEHICLES = 1;
	private static final int N_LIGHTS = 1;
	private static final int N_NODES = 100;
	
	private int numNodes = N_NODES;
	private ArrayList<MyNode> nodes = new ArrayList<MyNode>();
	private ContainerController mainContainer;
	private boolean runInBatchMode;
	private DisplaySurface displaySurf;
	private int numRadios = N_RADIOS;
	private int numVehicles = N_VEHICLES;
	private int numLights = N_LIGHTS;
	private ArrayList<RadioAgent> radioAgents;
	private ArrayList<VehicleAgent> vehicleAgents;
	private ArrayList<TrafficLightAgent> lightAgents;
		
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
			//buildSchedule();
			buildDisplay();
		}
		
		displaySurf.display();
	}
	
	public void buildModel() {
		
		for(int n = 0; n < numNodes; n++) {
			int x = Random.uniform.nextIntFromTo(0, WIDTH - 1);
		    int y = Random.uniform.nextIntFromTo(0, HEIGHT - 1);
		    OvalNetworkItem drawable = new OvalNetworkItem (x, y);
		    MyNode node1 = new MyNode(x, y,drawable);
		    nodes.add(node1);
		}
		
		for(int i = 1; i < nodes.size()/2; i++) {
			MyNode node = (MyNode) nodes.get (i);
			MyNode node1 = (MyNode)nodes.get(i+ 30);
		    node.makeEdgeToFrom(node1, 40, Color.cyan);
		}
	}
		
	public void buildSchedule() {
		System.out.println("Running BuildSchedule");
		
	}
	
	public void buildDisplay() {
		Network2DDisplay display = new Network2DDisplay (nodes,WIDTH,HEIGHT);
		displaySurf.addDisplayableProbeable (display, "City Traffic");
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
		
		radioAgents= new ArrayList<RadioAgent>();
		vehicleAgents= new ArrayList<VehicleAgent>();
		lightAgents= new ArrayList<TrafficLightAgent>();
		AID receiverLight = null;
				
		try {
			
			//create radios
			for(int i=0; i < numRadios;i++) {
				
				RadioAgent radio = new RadioAgent();
				radioAgents.add(radio);
				mainContainer.acceptNewAgent("Radio" + i, radio).start();
				
			}
			
			//create traffic lights
			for(int i=0; i < numLights;i++) {
				
				TrafficLightAgent tLight = new TrafficLightAgent();
				lightAgents.add(tLight);
				mainContainer.acceptNewAgent("Light" + i, tLight).start();
				receiverLight = tLight.getAID();
			}
			
			//create vehicles
			for(int i=0; i < numVehicles;i++) {
				
				VehicleAgent vehicle = new VehicleAgent(receiverLight);
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