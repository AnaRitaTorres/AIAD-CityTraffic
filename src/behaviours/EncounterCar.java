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
			
			System.out.println("AQUIIIIIIIIIII" + reply);
			//TODO TODO TODO TODO TODO deste lado a reply aparece a null, resolver isto e acabar de testar o behaviour para passar às colisoes e depois imlementar movimentos no grafo random e depois estatisticas e depois extras (entretanto verificar lights da rita)
			
			if(reply != null){
				String carNextPos = "" + car.getNextPosition()[0] + car.getNextPosition()[1] + "";
				if(reply.getContent().equals(carNextPos)){
					foundCar = true;
					r = reply;
				}
				repliesCnt++;
				if(foundCar == false && repliesCnt == cars.size()){
					step = 3;
				}
			}
			else{
				block();
			}
			break;
		case 2:
			//??
			break;
		}

	}



	@Override
	public boolean done() {
		return step == 3;
	}

}
