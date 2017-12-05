package gui;

import java.util.ArrayList;
import java.awt.Color;

import sajas.sim.repast3.Repast3Launcher;
import sajas.core.Runtime;
import sajas.wrapper.ContainerController;

import jade.core.AID;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import jade.wrapper.ControllerException;

import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.gui.ColorMap;
import uchicago.src.sim.gui.Value2DDisplay;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.SimGraphics;
import uchicago.src.sim.space.Object2DGrid;

import agents.*;

public class Launcher extends Repast3Launcher {
	
	private static final boolean BATCH_MODE = true;
	private static final int HEIGHT = 50;
	private static final int WIDTH = 70;
	private static final int N_RADIOS = 1;
	private static final int N_VEHICLES = 1;
	private static final int N_LIGHTS = 1;
	
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
		
		
	}
	
	public void buildModel() {
		System.out.println("Running BuildModel");
		
		
	}
	
	public void buildSchedule() {
		System.out.println("Running BuildSchedule");
		
	}
	
	public void buildDisplay() {
		System.out.println("Running BuildDisplay");
		
		
		
		
	}
	
	
	
	@Override
	public String getName() { 
		return "CityTraffic ";
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