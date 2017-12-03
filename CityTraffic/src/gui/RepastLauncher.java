package gui;

import java.awt.Color;

import sajas.sim.repast3.Repast3Launcher;
import sajas.core.Runtime;
import sajas.wrapper.ContainerController;

import jade.core.Profile;
import jade.core.ProfileImpl;

import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.gui.ColorMap;
import uchicago.src.sim.gui.Value2DDisplay;
import uchicago.src.sim.gui.DisplaySurface;

public class RepastLauncher extends Repast3Launcher {
	
	private static final int WORLDXSIZE = 40;
	private static final int WORLDYSIZE = 40;
	private static final boolean BATCH_MODE = true;
	
	
	private int worldXSize = WORLDXSIZE;
	private int worldYSize = WORLDYSIZE;
	private ContainerController mainContainer;
	private boolean runInBatchMode;
	private GridSpace gSpace;
	private DisplaySurface displaySurf;
		
	public RepastLauncher(boolean runInBatchMode) {
		super();
		this.runInBatchMode = runInBatchMode;
	}
	
	@Override
	public void setup() {
		super.setup();
		gSpace = null;
		
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
		buildModel();
		buildSchedule();
		buildDisplay();
		
		displaySurf.display();
	}
	
	public void buildModel() {
		System.out.println("Running BuildModel");
		
		gSpace = new GridSpace(worldXSize,worldYSize);
	}
	
	public void buildSchedule() {
		System.out.println("Running BuildSchedule");
		
	}
	
	public void buildDisplay() {
		System.out.println("Running BuildDisplay");
		
		ColorMap map = new ColorMap();
		
		for (int i=0; i < 16; i++) {
			map.mapColor(i, new Color((int)(i*8+127), 0, 0));
		}
		
		map.mapColor(0, Color.gray);
		
		Value2DDisplay displayStreet = new Value2DDisplay(gSpace.getCurrentStreetSpace(),map);
		displaySurf.addDisplayable(displayStreet, "Street");
	}
	
	@Override
	public String getName() { 
		return "City Traffic";
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
	}
	
	public static void main(String[] args) {
		boolean runMode = !BATCH_MODE; 
		
		SimInit init = new SimInit();
		init.setNumRuns(1);   // works only in batch mode
		init.loadModel(new RepastLauncher(runMode), null, runMode);
	}
}