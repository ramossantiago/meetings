package net.technisys.guayagamer.main;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import net.technisys.guayagamer.model.ConferenceRoom;

public class Main {

	private String[] input = { "my first nintendo 30min", "game sega cuarto", "sony vega 60min" };
	private static List<ConferenceRoom> conferenceRooms;

	public static void main(String[] args) {

		// Main main = new Main();
		// main.conferenceRooms.forEach(c -> c.toString()); 

		Duration duration = Duration.ofMinutes(15);

		System.out.println(duration.toMinutes());

		LocalTime time = LocalTime.of(10, 0);

		LocalTime time2 = LocalTime.of(10, 30);

		System.out.println(Duration.between(time, time2).toMinutes() + " Minutes");

	}

}
