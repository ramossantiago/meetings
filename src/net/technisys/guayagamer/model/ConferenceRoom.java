package net.technisys.guayagamer.model;

import java.util.ArrayList;
import java.util.List;

public class ConferenceRoom {

	private String name;
	private List<Session> sessions;

	public ConferenceRoom() {
		sessions = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Session> getSessions() {
		return sessions;
	}

	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;
	}

	public void printConferenceRoom() {
		System.out.println(name.toUpperCase()+":");
		sessions.forEach(s -> s.printSession());
	}

	
	
	
}
