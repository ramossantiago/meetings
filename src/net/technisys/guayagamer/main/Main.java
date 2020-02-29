package net.technisys.guayagamer.main;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import net.technisys.guayagamer.constant.Constant;
import net.technisys.guayagamer.model.Conference;
import net.technisys.guayagamer.model.ConferenceRoom;
import net.technisys.guayagamer.model.Session;

public class Main {

	private String[] inputConferencesString = { "my first nintendo 30min", "game sega cuarto", "sony vega 60min" };
	
	private static List<Conference> inputConferences = new ArrayList<>();
	private static List<Conference> usedConferences = new ArrayList<>();
	private static List<Conference> freeConferences = new ArrayList<>();
	
	private static List<ConferenceRoom> conferenceRooms;

	public static void main(String[] args) {

		Main main = new Main();
		conferenceRooms = new ArrayList<>();
		

		// calculate rooms needed
		// TODO completar
		int conferenceRoomNeeded = 2;
		Session session;
		for (int i=0; i<conferenceRoomNeeded; i++){
			ConferenceRoom conferenceRoom = new ConferenceRoom();
			conferenceRoom.setName(Constant.CONFERENCE_ROOM+" "+( i + 1));
			
			session = new Session(Constant.MORNING_SESSION, Constant.START_TIME_MORNING_SESSION, 
					Constant.END_TIME_MORNING_SESSION, Constant.END_TIME_MORNING_SESSION);
			conferenceRoom.getSessions().add(session);
			session = new Session(Constant.EVENING_SESSION, Constant.START_TIME_EVENING_SESSION, 
					Constant.MIN_END_TIME_EVENING_SESSION, Constant.MAX_END_TIME_EVENING_SESSION);
			conferenceRoom.getSessions().add(session);
			
			conferenceRooms.add(conferenceRoom);
		}
		
		
		// put conference inside rooms
		// TODO delete
		Conference item = new Conference();
		item.setName("Conferencia uno");
		item.setDuration(Duration.ofMinutes(60));
		inputConferences.add(item);
		
		item = new Conference();
		item.setName("Conferencia dos");
		item.setDuration(Duration.ofMinutes(45));
		inputConferences.add(item);
		
		item = new Conference();
		item.setName("Conferencia tres");
		item.setDuration(Duration.ofMinutes(30));
		inputConferences.add(item);
		
		item = new Conference();
		item.setName("Conferencia cuatro");
		item.setDuration(Duration.ofMinutes(45));
		inputConferences.add(item);
		
		item = new Conference();
		item.setName("Conferencia cinco");
		item.setDuration(Duration.ofMinutes(45));
		inputConferences.add(item);
		
		item = new Conference();
		item.setName("Conferencia seis");
		item.setDuration(Duration.ofMinutes(15));
		inputConferences.add(item);
		
		item = new Conference();
		item.setName("Conferencia siete");
		item.setDuration(Duration.ofMinutes(60));
		inputConferences.add(item);
		
		item = new Conference();
		item.setName("Conferencia ocho");
		item.setDuration(Duration.ofMinutes(30));
		inputConferences.add(item);
		
		item = new Conference();
		item.setName("Conferencia nueve");
		item.setDuration(Duration.ofMinutes(45));
		inputConferences.add(item);
		
		item = new Conference();
		item.setName("Conferencia diez");
		item.setDuration(Duration.ofMinutes(30));
		inputConferences.add(item);
		
		item = new Conference();
		item.setName("Conferencia once");
		item.setDuration(Duration.ofMinutes(30));
		inputConferences.add(item);
		
		item = new Conference();
		item.setName("Conferencia doce");
		item.setDuration(Duration.ofMinutes(45));
		inputConferences.add(item);
		
		item = new Conference();
		item.setName("Conferencia trece");
		item.setDuration(Duration.ofMinutes(60));
		inputConferences.add(item);
		
		item = new Conference();
		item.setName("Conferencia 14");
		item.setDuration(Duration.ofMinutes(60));
		inputConferences.add(item);
		
		item = new Conference();
		item.setName("Conferencia 15");
		item.setDuration(Duration.ofMinutes(45));
		inputConferences.add(item);
		
		item = new Conference();
		item.setName("Conferencia 16");
		item.setDuration(Duration.ofMinutes(15));
		inputConferences.add(item);
		
		item = new Conference();
		item.setName("Conferencia 17");
		item.setDuration(Duration.ofMinutes(30));
		inputConferences.add(item);
		
		item = new Conference();
		item.setName("Conferencia 18");
		item.setDuration(Duration.ofMinutes(30));
		inputConferences.add(item);
		
		item = new Conference();
		item.setName("Conferencia 19");
		item.setDuration(Duration.ofMinutes(60));
		inputConferences.add(item);
		
		item = new Conference();
		item.setName("Conferencia 20");
		item.setDuration(Duration.ofMinutes(30));
		inputConferences.add(item);
		
		item = new Conference();
		item.setName("Conferencia 21");
		item.setDuration(Duration.ofMinutes(15));
		inputConferences.add(item);
		
	
		
		
		
		
		
		
		
		
		
		main.conferenceRooms.forEach(c -> c.printConferenceRoom()); 
		
		
		
		
		// Main main = new Main();

		Duration duration = Duration.ofMinutes(15);

		System.out.println(duration.toMinutes());

		LocalTime time = LocalTime.of(10, 0);

		LocalTime time2 = LocalTime.of(10, 30);

		System.out.println(Duration.between(time, time2).toMinutes() + " Minutes");

	}

}
