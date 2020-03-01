package net.technisys.guayagamer.model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

import net.technisys.guayagamer.exceptions.InvalidArgumentsException;

public class Conference {

	private String name;
	private LocalTime startTime;
	private LocalTime endTime;
	private Duration duration;

	public Conference() {
		// TODO Auto-generated constructor stub
	}

	public Conference(String name, Duration duration) throws InvalidArgumentsException {
		super();

		if (Objects.isNull(name) || "".equals(name.trim())) {
			throw new InvalidArgumentsException("Conference name is required");
		}

		if (Objects.isNull(duration) || duration.isZero()) {
			throw new InvalidArgumentsException("Conference duration can´t be zero");
		}

		this.name = name;
		this.duration = duration;
	}

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
	}

	public void printConference() {
		System.out.println("\t" + this.getStartTime() + " " + this.name + " " + this.getDurationInMinutes() + "min");
	}
}
