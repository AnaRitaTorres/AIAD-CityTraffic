package gui;

public class Statistics {
	private int totalAccidents = 0;
	private float avgAccidents = 0;
	private double totalDistance = 0;
	private double avgDistance = 0;
	private float totalTimeWaitingTrafficLight = 0;
	private float avgTimeWaitingTrafficLight = 0;
	private float totalTimeWaitingTraffic = 0;
	private float avgTimeWaitingTraffic = 0;
	private float avgTimeNotMoving = 0;
	private int numCarsArrivedDestination = 0;
	
	public int getTotalAccidents() {
		return totalAccidents;
	}
	
	public float getAvgAccidents() {
		return avgAccidents;
	}
	
	public double getTotalDistance() {
		return totalDistance;
	}
	
	public double getAvgDistance() {
		return avgDistance;
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
	
	public int getNumCarsArrivedDestination(){
		return numCarsArrivedDestination;
	}
	
	public void updateTotalAccidents(){
		totalAccidents ++;
	}
	
	public void updateAvgAccidents(int numCars){
		avgAccidents = (float)totalAccidents/(float)numCars;
	}
	
	public void updateTotalDistance(double dist){
		totalDistance += dist;
	}
	
	public void updateAvgDistance(int numCars){
		avgDistance = totalDistance/(float)numCars;
	}
	
	public void updateTotalTimeWaitingTrafficLight(long time){
		totalTimeWaitingTrafficLight = totalTimeWaitingTrafficLight + time;
	}
	
	public void updateAvgTimeWaitingTrafficLight(int numCars){
		avgTimeWaitingTrafficLight = totalTimeWaitingTrafficLight/(float)numCars;
		updateAvgTimeNotMoving(numCars);
	}
	
	public void updateTotalTimeWaitingTraffic(long time){
		totalTimeWaitingTraffic = totalTimeWaitingTraffic + time;
	}
	
	public void updateAvgTimeWaitingTraffic(int numCars){
		avgTimeWaitingTraffic = totalTimeWaitingTraffic/(float)numCars;
		updateAvgTimeNotMoving(numCars);
	}
	
	public void updateAvgTimeNotMoving(int numCars){
		avgTimeNotMoving = (totalTimeWaitingTrafficLight + totalTimeWaitingTraffic)/(float)numCars;
	}
	
	public void updateNumCarsArrivedDestination(){
		numCarsArrivedDestination++;
	}
	
}
