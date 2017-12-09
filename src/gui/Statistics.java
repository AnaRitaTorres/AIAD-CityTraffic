package gui;

public class Statistics {
	private int totalAccidents = 0;
	private float avgAccidents = 0;
	private double totalDistance = 0;
	private double avgDistance = 0;
	private float avgTravelTime = 0;	//TODO later or not at all se nao houver tempo
	private float totalTimeWaitingTrafficLight = 0;	//TODO vou aqui
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
		avgAccidents = (float)totalAccidents/(float)numCars;
		updateDisplay();
	}
	
	public void updateTotalDistance(double dist){
		totalDistance += dist;
	}
	
	public void updateAvgDistance(int numCars){
		avgDistance = totalDistance/(float)numCars;
	}
	
	//update avgTravelTime
	
	public void updateTotalTimeWaitingTrafficLight(float time){
		totalTimeWaitingTrafficLight += time;
		System.out.println("total " + totalTimeWaitingTrafficLight);
	}
	
	public void updateAvgTimeWaitingTrafficLight(int numCars){
		avgTimeWaitingTrafficLight = totalTimeWaitingTrafficLight/(float)numCars;
		System.out.println("avg " + avgTimeWaitingTrafficLight);
	}
	
	public void updateTotalTimeWaitingTraffic(float time){
		totalTimeWaitingTraffic += time;
	}
	
	public void updateAvgTimeWaitingTraffic(int numCars){
		avgTimeWaitingTraffic = totalTimeWaitingTraffic/(float)numCars;
	}
	
	public void updateAvgTimeNotMoving(int numCars){
		avgTimeNotMoving = (totalTimeWaitingTrafficLight + totalTimeWaitingTraffic)/(float)numCars;
	}
	
	public void updateDisplay(){
		//TODO fazerdispX.update()
	}
	
}
