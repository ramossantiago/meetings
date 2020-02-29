package net.technisys.guayagamer.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import net.technisys.guayagamer.interfaces.ISession;

public class Session implements ISession {

	private String name;
	private LocalTime startSession;
	private LocalTime minimumEndTimeSession;
	private LocalTime maximumEndTimeSession;
	private List<Conference> conferences;

	public Session() {
		conferences = new ArrayList<>();
	}

	public Session(String name, LocalTime startSession, LocalTime minimumEndTimeSession, LocalTime maximumEndTimeSession) {
		super();
		this.name = name;
		this.startSession = startSession;
		this.minimumEndTimeSession = minimumEndTimeSession;
		this.maximumEndTimeSession = maximumEndTimeSession;
		conferences = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalTime getStartSession() {
		return startSession;
	}

	public void setStartSession(LocalTime startSession) {
		this.startSession = startSession;
	}

	public LocalTime getMinimumEndTimeSession() {
		return minimumEndTimeSession;
	}

	public void setMinimumEndTimeSession(LocalTime minimumEndTimeSession) {
		this.minimumEndTimeSession = minimumEndTimeSession;
	}

	public LocalTime getMaximumEndTimeSession() {
		return maximumEndTimeSession;
	}

	public void setMaximumEndTimeSession(LocalTime maximumEndTimeSession) {
		this.maximumEndTimeSession = maximumEndTimeSession;
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

	public void printSession(){
		System.out.println(this.name);
		conferences.forEach(c -> c.printConference());
	}
	
}
