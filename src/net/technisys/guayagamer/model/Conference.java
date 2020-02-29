package net.technisys.guayagamer.model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

public class Conference {

	private String name;
	private LocalTime startTime;
	private LocalTime endTime;
	private Duration duration;
	public int attempt = 0;
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
		setEndTime();
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	private void setEndTime() {
		if (!Objects.isNull(startTime)) {
			this.endTime = startTime.plusMinutes(duration.toMinutes());
		}
	}

	public Long getDurationInMinutes() {
		return duration.toMinutes();
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
		setEndTime();
	}

	public void printConference() {
		System.out.println("\t"+this.getStartTime() + " " + this.name +" "+ this.getDurationInMinutes() + "min" + " used "+attempt);
	}
}
