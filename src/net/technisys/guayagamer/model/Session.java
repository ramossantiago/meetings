package net.technisys.guayagamer.model;

import java.time.Duration;
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
	
	private Duration minDuration;
	private Duration maxDuration;
	
	private Duration remainingMinutes;
	private Duration usedMinutes;

//	public Session() {
//		conferences = new ArrayList<>();
//		this.remainingMinutes = Duration.ZERO;
//	}

	public Session(String name, LocalTime startSession, LocalTime minimumEndTimeSession,
			LocalTime maximumEndTimeSession) {
		super();
		// TODO validate input and throw exception
		this.name = name;
		this.startSession = startSession;
		this.minimumEndTimeSession = minimumEndTimeSession;
		this.maximumEndTimeSession = maximumEndTimeSession;
		this.minDuration = Duration.between(startSession, minimumEndTimeSession);
		this.maxDuration = Duration.between(startSession, maximumEndTimeSession);
		conferences = new ArrayList<>();
		calculateUsedRemainingTime();
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

	public long getRemainingMinutes() {
		return remainingMinutes.toMinutes();
	}

//	private void setRemainingMinutes(Duration remainingMinutes) {
//		this.remainingMinutes = remainingMinutes;
//	}

	@Override
	public boolean isFull() {
		Long canEndLapseTime = maxDuration.toMinutes() - minDuration.toMinutes(); 
		
		if (remainingMinutes.toMinutes() <= canEndLapseTime) {
			return true;
		}
		
		return false;
	}

	
	@Override
	public boolean isCompleteFull() {
		if (remainingMinutes.toMinutes() == 0) {
			return true;
		}

		return false;
	}
	
	
	@Override
	public void addConference(Conference conference) throws Exception {

		if (isCompleteFull()) {
			throw new Exception("La session esta llena");
		}
		
		this.conferences.add(conference);
		calculateUsedRemainingTime();
	}

	@Override
	public void removeConference(Conference conference) {
		this.conferences.remove(conference);
		calculateUsedRemainingTime();
	}

	private void calculateUsedRemainingTime() {
		Long minutesInSession = 0l;

		for (Conference conference : conferences) {
			minutesInSession += conference.getDurationInMinutes();
		}

		usedMinutes = Duration.ofMinutes(minutesInSession);
		remainingMinutes = Duration.ofMinutes(maxDuration.toMinutes() - minutesInSession);
	}
	
	
	public void printSession() {
		System.out.println(this.name + " -> " + "is Full: "+ isCompleteFull()+ " - " +this.remainingMinutes.toMinutes() +" minutes free");
		long timeUsed = 0;
		
		for (Conference conference : conferences) {
			conference.setStartTime(this.getStartSession().plusMinutes(timeUsed));
			timeUsed += conference.getDurationInMinutes();
			conference.printConference();
		}
		
		
//		conferences.forEach(c -> {
//			//c.setStartTime(this.getStartSession().plusMinutes(timeUsed));
//			//timeUsed += c.getDurationInMinutes();
//			c.printConference();
//		});
	}

	

}
