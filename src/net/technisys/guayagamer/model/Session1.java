package net.technisys.guayagamer.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import net.technisys.guayagamer.interfaces.ISession;

public class Session1 implements ISession {

	private LocalTime startSession;
	private LocalTime minimumEndSession;
	private LocalTime maximumEndSession;
	private List<Conference> conferences;

	public Session1() {
		conferences = new ArrayList<>();
	}

	public LocalTime getStartSession() {
		return startSession;
	}

	public void setStartSession(LocalTime startSession) {
		this.startSession = startSession;
	}

	public LocalTime getMinimumEndSession() {
		return minimumEndSession;
	}

	public void setMinimumEndSession(LocalTime minimumEndSession) {
		this.minimumEndSession = minimumEndSession;
	}

	public LocalTime getMaximumEndSession() {
		return maximumEndSession;
	}

	public void setMaximumEndSession(LocalTime maximumEndSession) {
		this.maximumEndSession = maximumEndSession;
	}

	public List<Conference> getConferences() {
		return conferences;
	}

	public void setConferences(List<Conference> conferences) {
		this.conferences = conferences;
	}

	@Override
	public boolean isSessionFull() {
		return false;
	}

	@Override
	public void addConference(Conference conference) {
		this.conferences.add(conference);
	}

}
