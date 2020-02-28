package net.technisys.guayagamer.model;

import java.util.ArrayList;
import java.util.List;

import net.technisys.guayagamer.interfaces.ISession;

public class Session2 implements ISession{

	List<Conference> conferences;
	
	public Session2() {
		conferences = new ArrayList<>();
	}
	
	
	@Override
	public boolean isSessionFull() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void addConference() {
		// TODO Auto-generated method stub
		
	}
	
	
}
