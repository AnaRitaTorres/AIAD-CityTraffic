package gui;

public class Statistics {
	private int totalAccidents = 0;
	private float avgAccidents = 0;
	private float totalDistance = 0;	//TODO
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
	
	public int getTotalAccidents() {
		return totalAccidents;
	}
	
	public float getAvgAccidents() {
		return avgAccidents;
	}
	
	public float getTotalDistance() {
		return totalDistance;
	}
	
	public float getAvgDistance() {
		return avgDistance;
	}
	
	public float getAvgTravelTime() {
		return avgTravelTime;
	}
	
	public float getTotalTimeWaitingTrafficLight() {
		return totalTimeWaitingTrafficLight;
	}
	
	public float getAvgTimeWaitingTrafficLight() {
		return avgTimeWaitingTrafficLight;
	}
	
	public float getTotalTimeWaitingTraffic(){
		return  totalTimeWaitingTraffic;
	}
	
	public float getAvgTimeWaitingTraffic() {
		return  avgTimeWaitingTraffic;
	}
	
	public float getAvgTimeNotMoving() {
		return avgTimeNotMoving;
	}
	
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
