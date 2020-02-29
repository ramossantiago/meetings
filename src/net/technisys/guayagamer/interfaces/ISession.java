package net.technisys.guayagamer.interfaces;

import net.technisys.guayagamer.model.Conference;

public interface ISession {

	public boolean isFull();
	
	public boolean isCompleteFull();
	
	public void addConference(Conference conference);
	
	public void removeConference(Conference conference);
	
}
