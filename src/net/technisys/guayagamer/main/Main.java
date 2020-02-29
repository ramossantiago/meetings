package net.technisys.guayagamer.main;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
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

	private static LinkedList<Conference> freeConferencesQueue = new LinkedList<>();
	
	private static List<ConferenceRoom> conferenceRooms;

	public static void main(String[] args) {

		Main main = new Main();
		conferenceRooms = new ArrayList<>();

		// calculate rooms needed
		// TODO completar
		int conferenceRoomNeeded = 2;
		Session session;
		for (int i = 0; i < conferenceRoomNeeded; i++) {
			ConferenceRoom conferenceRoom = new ConferenceRoom();
			conferenceRoom.setName(Constant.CONFERENCE_ROOM + " " + (i + 1));

			session = new Session(conferenceRoom.getName() + " " + Constant.MORNING_SESSION,
					Constant.START_TIME_MORNING_SESSION, Constant.END_TIME_MORNING_SESSION,
					Constant.END_TIME_MORNING_SESSION);
			conferenceRoom.getSessions().add(session);
			session = new Session(conferenceRoom.getName() + " " + Constant.EVENING_SESSION,
					Constant.START_TIME_EVENING_SESSION, Constant.MIN_END_TIME_EVENING_SESSION,
					Constant.MAX_END_TIME_EVENING_SESSION);
			conferenceRoom.getSessions().add(session);

			conferenceRooms.add(conferenceRoom);
		}

		// TODO borrar
		dummyConference();
		freeConferences = new ArrayList<>(inputConferences);

		int contador = 1;
		while (!freeConferences.isEmpty()) {

			for (ConferenceRoom room : conferenceRooms) {
				for (Session se : room.getSessions()) {
					if (!se.isCompleteFull()) {
						addConferences(se);
					}
				}
			}

			System.out.println("CONTADOR " + contador++);

			if (contador > 20)
				break;
		}

		main.conferenceRooms.forEach(c -> c.printConferenceRoom());

		System.out.println(" ");
		System.out.println("Free Conferences ");

		for (Conference c : freeConferences) {
			c.printConference();
		}
	}

	private static void addConferences(Session session) {

		if (!freeConferences.isEmpty()) {

			Conference nextConference = freeConferences.get(0);

			if (nextConference.attempt > 5) {
				fixConference(session, nextConference);
			}

			if (session.getRemainingMinutes() >= nextConference.getDurationInMinutes()) {
				session.addConference(freeConferences.get(0));
				usedConferences.add(nextConference);
				freeConferences.remove(nextConference);
			}
			nextConference.attempt += 1;

		}
	}

	private static void fixConference(Session session, Conference nextConference) {
		
		long diferencia = nextConference.getDurationInMinutes() - session.getRemainingMinutes();
		Conference deleteConference = new Conference();

		for (Conference conf : session.getConferences()) {
			if (conf.getDurationInMinutes().equals(diferencia)) {
				usedConferences.remove(conf);
				freeConferences.add(conf);
				deleteConference = conf;
				deleteConference.attempt = 0;
			}
		}
		session.removeConference(deleteConference);

		System.out.println("NO puedo ubicar la conferencia " + nextConference.getName() + " en session "
				+ session.getName());
		System.out.println("Voy a mover la conferencia " + deleteConference.getName());
		
	}
	
	
	
	private static void dummyConference() {

		// put conference inside rooms
		// TODO delete
		Conference item = new Conference();
		item.setName("Conferencia uno");
		item.setDuration(Duration.ofMinutes(30));
		inputConferences.add(item);
		
		item = new Conference();
		item.setName("Conferencia tres");
		item.setDuration(Duration.ofMinutes(30));
		inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia dos");
		item.setDuration(Duration.ofMinutes(45));
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
		item.setDuration(Duration.ofMinutes(30));
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
		item.setDuration(Duration.ofMinutes(30));
		inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia 16");
		item.setDuration(Duration.ofMinutes(30));
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
		item.setDuration(Duration.ofMinutes(30));
		inputConferences.add(item);

//		item = new Conference();
//		item.setName("Conferencia 22");
//		item.setDuration(Duration.ofMinutes(30));
//		inputConferences.add(item);
//
//		item = new Conference();
//		item.setName("Conferencia 23");
//		item.setDuration(Duration.ofMinutes(15));
//		inputConferences.add(item);

	}

}
