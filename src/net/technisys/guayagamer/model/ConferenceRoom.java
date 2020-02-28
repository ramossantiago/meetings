package net.technisys.guayagamer.model;

import java.util.List;

public class ConferenceRoom {

	private String name;
	private List<Session1> sessions;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Session1> getSessions() {
		return sessions;
	}

	public void setSessions(List<Session1> sessions) {
		this.sessions = sessions;
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
