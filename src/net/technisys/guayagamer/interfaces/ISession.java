package net.technisys.guayagamer.interfaces;

import net.technisys.guayagamer.model.Conference;

public interface ISession {

	public boolean isSessionFull();
	
	public void addConference(Conference conference);
}
