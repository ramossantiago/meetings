package net.technisys.guayagamer.interfaces;

import net.technisys.guayagamer.model.Conference;

public interface ISession {

	public boolean isFull();
	
	public boolean isCompleteFull();
	
	public void addConference(Conference conference) throws Exception;
	
	public void removeConference(Conference conference);
	
}
