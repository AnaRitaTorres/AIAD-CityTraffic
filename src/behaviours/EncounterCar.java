package behaviours;

import java.util.Vector;

import agents.VehicleAgent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import sajas.core.behaviours.*;

public class EncounterCar extends Behaviour{

	//ver se tem carro
	//se tiver esperar que nao tenha

	private VehicleAgent car;
	private Vector<VehicleAgent> cars;
	private int step = 0;
	private int repliesCnt = 0;
	private boolean foundCar = false;
	private ACLMessage reply;
	private MessageTemplate mt;
	long begin = 0;
	long end;

	public EncounterCar(VehicleAgent car, Vector<VehicleAgent> cars) {
		this.car = car;
		this.cars = cars;
	}

	@Override
	public void action() {
		String carNextPos = "" + car.getNextPosition().getX() + car.getNextPosition().getY() + "";

		switch(step){
		case 0:
			//send the cfp to all cars
			if(cars.size() == 1){
				step = 3;
				car.dt2 = 0;
			}
			else{
				ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
				for(int i = 0; i < cars.size(); i++){
					if(cars.elementAt(i).getAID() != car.getAID()){
						cfp.addReceiver(cars.elementAt(i).getAID());
					}
				}
				cfp.setContent("position");
				cfp.setConversationId("position");
				cfp.setReplyWith("cfp"+System.currentTimeMillis());
				car.send(cfp);
				mt = MessageTemplate.and(MessageTemplate.MatchConversationId("position"), MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));			

				repliesCnt = 0;
				foundCar = false;
				step = 1;
			}
			
			break;
		case 1:
			//receive all answers from cars
			reply = car.receive(mt); 
			if(reply != null){
				if(reply.getContent().equals(carNextPos)){
					foundCar = true;
					if(begin == 0){
						begin = System.currentTimeMillis();
					}
					step = 2;
				}
				repliesCnt++;
				if(foundCar == false && repliesCnt == cars.size()-1){
					step = 3;
					car.dt2 = 0;
				}

			}
			else{
				block();
			}
			break;
		case 2:
			if(reply.getContent().equals(carNextPos)){
				step = 0;
			}
			else{
				step = 3;
				end = System.currentTimeMillis();
				car.dt2 = end - begin;
			}
			break;
		}

	}

	@Override
	public boolean done() {
		return step == 3;
	}

}
