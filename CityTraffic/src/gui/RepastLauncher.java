package gui;

import java.awt.Color;
import java.util.ArrayList;

import sajas.sim.repast3.Repast3Launcher;
import sajas.core.Runtime;
import sajas.wrapper.ContainerController;
import sajas.core.Agent;

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

import agents.*;

public class RepastLauncher extends Repast3Launcher {
	
	private static final boolean BATCH_MODE = true;
	private static final int N_RADIOS = 5;
	private static final int N_VEHICLES = 5;
	private static final int N_LIGHTS = 5;
	
	
	private ContainerController mainContainer;
	private boolean runInBatchMode;
	private DisplaySurface displaySurf;
	private ArrayList<RadioAgent> radioAgents;
	private ArrayList<VehicleAgent> vehicleAgents;
	private ArrayList<TrafficLightAgent> lightAgents;
		
	public RepastLauncher(boolean runInBatchMode) {
		super();
		this.runInBatchMode = runInBatchMode;
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
			//buildModel();
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
		
		displaySurf.display();
		
		
	}
	
	@Override
	public String getName() { 
		return "CityTraffic ";
	}
	
	@Override
	public String[] getInitParam() {
		String[] initParams = { };
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
		ArrayList<AID> receivers = new ArrayList<AID>();
		AID receiverCar = null;
		AID receiverRadio = null;
		
		try {
			//create radios
			
			
			for(int i=0; i < N_RADIOS;i++) {
				
				RadioAgent radio = new RadioAgent();
				radioAgents.add(radio);
				mainContainer.acceptNewAgent("Radio" + i, radio).start();
				//receivers.add(radio.getAID());
				receiverRadio = radio.getAID();
			}
			
			//create vehicles
			for(int i=0; i < N_VEHICLES;i++) {
				
				VehicleAgent vehicle = new VehicleAgent();
				vehicleAgents.add(vehicle);
				mainContainer.acceptNewAgent("Vehicle" + i, vehicle).start();
				//receivers.add(vehicle.getAID());
				receiverCar = vehicle.getAID();
			}
			
			//create traffic lights
			for(int i=0; i < N_LIGHTS;i++) {
				
				TrafficLightAgent tLight = new TrafficLightAgent(receiverCar, receiverRadio);
				lightAgents.add(tLight);
				mainContainer.acceptNewAgent("Light" + i, tLight).start();
			}
		}catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		boolean runMode = !BATCH_MODE; 
		
		SimInit init = new SimInit();
		init.setNumRuns(1);   // works only in batch mode
		init.loadModel(new RepastLauncher(runMode), null, runMode);
	}
}