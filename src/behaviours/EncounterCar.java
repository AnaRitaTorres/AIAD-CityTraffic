package behaviours;

import java.util.Vector;

import agents.VehicleAgent;
import jade.lang.acl.ACLMessage;
import sajas.core.behaviours.*;

public class EncounterCar extends Behaviour{

	//ver se tem carro
	//se tiver esperar que nao tenha

	private VehicleAgent car;
	private Vector<VehicleAgent> cars;
	private int step = 0;
	private int repliesCnt = 0;
	private boolean foundCar = false;
	private ACLMessage r;

	public EncounterCar(VehicleAgent car, Vector<VehicleAgent> cars) {
		this.car = car;
		this.cars = cars;
	}

	@Override
	public void action() {
		String carNextPos = "" + car.getNextPosition()[0] + car.getNextPosition()[1] + "";
		
		switch(step){
		case 0:
			//send the cfp to all cars
			ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
			for(int i = 0; i < cars.size(); i++){
				if(cars.elementAt(i).getAID() != car.getAID()){
					cfp.addReceiver(cars.elementAt(i).getAID());
				}
			}
			cfp.setContent("position");
			cfp.setConversationId("position");
			car.send(cfp);

			step = 1;
			break;
		case 1:
			//receive all answers from cars
			ACLMessage reply = car.receive(); 
			//TODO resolver isto e acabar de testar o behaviour para passar às colisoes e depois imlementar movimentos no grafo random e depois estatisticas e depois extras (entretanto verificar lights da rita)
			if(reply != null){
				if(reply.getContent().equals(carNextPos)){
					foundCar = true;
					r = reply;
					step = 2;
					System.out.println("AQUIIIIIIIIIII");

				}
				repliesCnt++;
				if(foundCar == false && repliesCnt == cars.size()){
					System.out.println("AQUIIIIIIIIIII");
					step = 3;
				}
				
			}
			else{
				block();
			}
			break;
		case 2:
			if(r.getContent().equals(carNextPos)){
				step = 0;
			}
			else{
				step = 3;
			}
			break;
		}

	}

	@Override
	public boolean done() {
		return step == 3;
	}

}
