package net.technisys.guayagamer.model;

import java.time.Duration;
import java.time.LocalTime;

import net.technisys.guayagamer.constant.Constant;
import net.technisys.guayagamer.exceptions.InvalidArgumentsException;
import net.technisys.guayagamer.interfaces.ISession;

public class LunchSession implements ISession {

	private LocalTime startSession;
	private LocalTime EndTimeSession;
	private Conference lunch;

	public LunchSession() throws InvalidArgumentsException {
		lunch = new Conference(Constant.LUNCH, Duration.between(Constant.START_LUNCH, Constant.END_LUNCH));
	}

	@Override
	public boolean isFull() {
		return true;
	}

	@Override
	public void addConference(Conference conference) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeConference(Conference conference) {
		// TODO Auto-generated method stub
	}

	@Override
	public void printSession() {
		// TODO Auto-generated method stub
	}

}
