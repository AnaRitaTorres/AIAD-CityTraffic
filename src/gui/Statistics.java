package gui;

public class Statistics {
	private int totalAccidents = 0;
	private float avgAccidents = 0;
	private float totalDist = 0;	//TODO
	private float avgDistance = 0;	//TODO
	private float avgTravelTime = 0;	//TODO later or not at all se nao houver tempo
	private float totalTimeWaitingTrafficLight = 0;	//TODO
	private float avgTimeWaitingTrafficLight = 0;	//TODO
	private float totalTimeWaitingTraffic = 0;	//TODO
	private float avgTimeWaitingTraffic = 0;	//TODO
	private float avgTimeNotMoving = 0;	//TODO
	
	/*public Statistics(disp){
		this.disp=disp;
	}*/
	
	public void updateTotalAccidents(){
		totalAccidents ++;
		updateDisplay();
	}
	
	public void updateAvgAccidents(int numCars){
		System.out.println(numCars);
		avgAccidents = (float)totalAccidents/(float)numCars;
		updateDisplay();
	}
	
	public void updateDisplay(){
		//TODO fazerdispX.update()
	}
	
}
