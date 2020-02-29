package net.technisys.guayagamer.model;

import java.time.Duration;
import java.time.LocalTime;

public class Conference {

	private String name;
	private LocalTime startTime;
	private LocalTime endTime;
	private Duration duration;
	
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
		this.endTime = startTime.plusMinutes(duration.toMinutes());
	}
	
	public Duration getDuration() {
		return duration;
	}
	
	public void setDuration(Duration duration) {
		this.duration = duration;
		setEndTime();
	}
	
	public void printConference(){
		System.out.println(this.getStartTime() +" " + this.name + this.getDuration().toMinutes() + "min");
	}
}
