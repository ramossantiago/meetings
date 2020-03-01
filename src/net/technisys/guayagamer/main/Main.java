package net.technisys.guayagamer.main;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import net.technisys.guayagamer.constant.Constant;
import net.technisys.guayagamer.model.Conference;
import net.technisys.guayagamer.model.ConferenceRoom;
import net.technisys.guayagamer.model.Session;

public class Main {

	private String[] inputConferencesString = { "my first nintendo 30min", "game sega cuarto", "sony vega 60min" };

	private static List<Conference> inputConferences = new ArrayList<>();
	// private static List<Conference> usedConferences = new ArrayList<>();
	private static List<Conference> freeConferences = new ArrayList<>();

	private static LinkedList<Conference> freeConferencesQueue = new LinkedList<>();

	private static List<ConferenceRoom> conferenceRooms;

	public static void main(String[] args) throws Exception {

		conferenceRooms = new ArrayList<>();

		// calculate rooms needed
		// TODO completar
		int conferenceRoomNeeded = 3;
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
		// freeConferences = new ArrayList<>(inputConferences);
		freeConferencesQueue = new LinkedList<>(inputConferences);

		int contador = 1;
		while (!freeConferencesQueue.isEmpty()) {

			for (ConferenceRoom room : conferenceRooms) {
				for (Session se : room.getSessions()) {
					if (!se.isFull()) {
						addConferences(se);
					}
				}
			}

			System.out.println("CONTADOR " + contador++);

			if (contador > 20)
				break;
		}

		System.out.println("");
		System.out.println("***********************");
		System.out.println("FINAL RESULT:");
		conferenceRooms.forEach(c -> c.printConferenceRoom());

		System.out.println(" ");
		System.out.println("Free Conferences ");

		for (Conference c : freeConferencesQueue) {
			c.printConference();
		}
	}

	private static void addConferences(Session session) throws Exception {

		if (!freeConferencesQueue.isEmpty()) {

			Conference nextConference = freeConferencesQueue.getFirst();
			nextConference.attempt += 1;

			if (session.getRemainingMinutes() >= nextConference.getDurationInMinutes()) {
				session.addConference(nextConference);
				freeConferencesQueue.remove(nextConference);
			} else {
				System.out.println("NO puedo ubicar la conferencia " + nextConference.getName() + ": "
						+ nextConference.getDurationInMinutes() + "min, en session " + session.getName() + ": "
						+ session.getRemainingMinutes() + "min");
			}

			if (nextConference.attempt > 3) {
				fixConference(session, nextConference);
			}

			if (!session.isFull() && !freeConferencesQueue.isEmpty()) {
				addConferences(session);
			}

		}
	}

	private static void fixConference(Session session, Conference nextConference) throws Exception {
		System.out.println("Fixing");

		nextConference.attempt = 0;
		Conference fixConference = null;
		long neededTime = nextConference.getDurationInMinutes();

		// buscando una conferencia libre dentro de la lista que calce en tiempo
		for (Conference freeConf : freeConferencesQueue) {
			if (session.getRemainingMinutes() == freeConf.getDurationInMinutes()) {
				fixConference = freeConf;
				break;
			}
		}

		if (!Objects.isNull(fixConference)) {
			// usedConferences.add(fixConference);
			freeConferencesQueue.remove(fixConference);
			session.addConference(fixConference);
		} else {

			// IR A BUSCAR EN LAS OTRA SESSIONES, NO UNICMANETE EN AL PROPIA

			System.out.println("");
			System.out.println("***");
			System.out.println("NO puedo ubicar la conferencia " + nextConference.getName() + ": "
					+ nextConference.getDurationInMinutes() + "min, en session " + session.getName() + ": "
					+ session.getRemainingMinutes() + "min");

			conferenceRooms.forEach(c -> c.printConferenceRoom());
			System.out.println(" ");
			System.out.println("Free Conferences ");

			for (Conference c : freeConferencesQueue) {
				c.printConference();
			}
			System.out.println("***");

			long diferencia = neededTime - session.getRemainingMinutes();
			Conference deleteConference = new Conference();
			boolean sucessChanged = false;

			// QUITAR UNA QUE TENGA LA DURACION FALTANTE
			for (Conference conf : session.getConferences()) {
				if (conf.getDurationInMinutes().equals(diferencia)) {
					deleteConference = conf;
					deleteConference.attempt = 0;
					sucessChanged = true;
					break;
				}
			}

			// QUITAR UNA QUE SU DURACION + LO FALTANTE SEA IGUAL A LA DURACION QUE NECESITO
			// UBICAR
			if (!sucessChanged) {
				for (Conference conf : session.getConferences()) {
					if (conf.getDurationInMinutes().equals(nextConference.getDurationInMinutes() + diferencia)) {
						deleteConference = conf;
						deleteConference.attempt = 0;
						sucessChanged = true;
						break;
					}
				}
			}

			// SI ENCONTRE UNA COINCIDENCIA BORRAR
			if (sucessChanged) {
				session.removeConference(deleteConference);
				freeConferencesQueue.add(deleteConference);
				System.out.println("Voy a mover la conferencia " + deleteConference.getName());
			} else {
				System.out.println("No tengo nada que mover");
			}

			
			
			// BUSCAR EN OTRAS SESSIONES POR COINCIDENCIAS
			if (!sucessChanged) {
				long totalTime = 0;
				Session sessionForChangeSchedule = null;
				List<Conference> removeConferences = new ArrayList<>();
				rooms: for (ConferenceRoom room : conferenceRooms) {

					for (Session ses : room.getSessions()) {
						totalTime = 0;
						removeConferences = new ArrayList<>();
						
						if (ses.getName().equals(session.getName())) {
							continue;
						}

						ses.getConferences().sort(new Comparator<Conference>() {
							
							@Override
							public int compare(Conference c1, Conference c2) {
								return c1.getDurationInMinutes().compareTo(c2.getDurationInMinutes()); 
							}
						});
						
						
						for (Conference co : ses.getConferences()) {
							
							if (co.getDurationInMinutes() < neededTime) {
								totalTime += co.getDurationInMinutes();
								removeConferences.add(co);
							}

							if (totalTime == neededTime) {
								sessionForChangeSchedule = ses;
								break rooms;
							}
						}
					}
				}

				if (!Objects.isNull(sessionForChangeSchedule)) {
					for (Conference conf : removeConferences) {
						sessionForChangeSchedule.removeConference(conf);
						freeConferencesQueue.add(conf);
					}
					sessionForChangeSchedule.addConference(nextConference);
					freeConferencesQueue.remove(nextConference);
				}
			}

		}
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
		item.setDuration(Duration.ofMinutes(60));
		inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia seis");
		item.setDuration(Duration.ofMinutes(45));
		inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia siete");
		item.setDuration(Duration.ofMinutes(60));
		inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia ocho");
		item.setDuration(Duration.ofMinutes(45));
		inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia nueve");
		item.setDuration(Duration.ofMinutes(30));
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

//		item = new Conference();
//		item.setName("Conferencia 19");
//		item.setDuration(Duration.ofMinutes(60));
//		inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia 20");
		item.setDuration(Duration.ofMinutes(30));
		inputConferences.add(item);

//		item = new Conference();
//		item.setName("Conferencia 21");
//		item.setDuration(Duration.ofMinutes(30));
//		inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia 22");
		item.setDuration(Duration.ofMinutes(45));
		inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia 23");
		item.setDuration(Duration.ofMinutes(15));
		inputConferences.add(item);

	}

}
