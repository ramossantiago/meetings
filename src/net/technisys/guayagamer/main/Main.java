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
	private static LinkedList<Conference> freeConferencesQueue = new LinkedList<>();
	private static List<ConferenceRoom> conferenceRooms;

	static int contador = 0;

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

		contador = 1;
		for (ConferenceRoom room : conferenceRooms) {
			for (Session se : room.getSessions()) {
				while (!se.isFull() && !freeConferencesQueue.isEmpty()) {
					// System.out.println("CONTADOR " + contador++);
					addConferences(se);
				}
			}
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

		/**
		 * *********************************************************
		 * *********************************************************
		 */

		conferenceRooms = new ArrayList<>();

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
		dummyConference2();
		// freeConferences = new ArrayList<>(inputConferences);
		freeConferencesQueue = new LinkedList<>(inputConferences);

		contador = 1;
		for (ConferenceRoom room : conferenceRooms) {
			for (Session se : room.getSessions()) {
				while (!se.isFull() && !freeConferencesQueue.isEmpty()) {
					// System.out.println("CONTADOR " + contador++);
					addConferences(se);
				}
			}
		}

		System.out.println("");
		System.out.println("***********************");
		System.out.println("FINAL RESULT 2:");
		conferenceRooms.forEach(c -> c.printConferenceRoom());

		System.out.println(" ");
		System.out.println("Free Conferences ");

		for (Conference c : freeConferencesQueue) {
			c.printConference();
		}

		/**
		 * *********************************************************
		 * *********************************************************
		 */

		conferenceRooms = new ArrayList<>();

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
		dummyConference3();
		// freeConferences = new ArrayList<>(inputConferences);
		freeConferencesQueue = new LinkedList<>(inputConferences);

		contador = 1;
		for (ConferenceRoom room : conferenceRooms) {
			for (Session se : room.getSessions()) {
				while (!se.isFull() && !freeConferencesQueue.isEmpty()) {
					// System.out.println("CONTADOR " + contador++);
					addConferences(se);
				}
			}
		}

		System.out.println("");
		System.out.println("***********************");
		System.out.println("FINAL RESULT 3:");
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
			// nextConference.attempt += 1;

			if (session.getRemainingMinutes() >= nextConference.getDurationInMinutes()) {
				session.addConference(nextConference);
				freeConferencesQueue.remove(nextConference);
			} else {
				checkOtherConferences(session);
				fixConference2(session, nextConference);
			}
		}
	}

	private static void checkOtherConferences(Session session) throws Exception {
		List<Conference> fixingConferences = new ArrayList<>();

		// buscando una conferencia libre dentro de la lista de disponibles que calce en
		// tiempo exactamente
		for (Conference freeConf : freeConferencesQueue) {
			if (freeConf.getDurationInMinutes().equals(session.getRemainingMinutes())) {
				fixingConferences.add(freeConf);
				break;
			}
		}

		if (!fixingConferences.isEmpty()) {
			for (Conference fixConference : fixingConferences) {
				freeConferencesQueue.remove(fixConference);
				session.addConference(fixConference);
			}
			return;
		}

	}

	private static void fixConference(Session session, Conference nextConference) throws Exception {

		long neededTimeForFix = nextConference.getDurationInMinutes();
		long totalTime = 0;
		List<Conference> fixingConferences = new ArrayList<>();
		Session sessionForChangeSchedule = null;

		for (Conference freeConf : freeConferencesQueue) {
			if (freeConf.getDurationInMinutes().equals(session.getRemainingMinutes())) {
				fixingConferences.add(freeConf);
				break;
			}
		}

		if (!fixingConferences.isEmpty()) {
			for (Conference fixConference : fixingConferences) {
				freeConferencesQueue.remove(fixConference);
				session.addConference(fixConference);
			}
			return;
		}

		// *******************************************************
		// else {
		// Si NO encontre una conferencia disponible que coincida en tiempo, voy a mover
		// la calendarizacion

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

		if (contador == 100) {
			System.out.println("Detener");
		}

		// BUSCAR EN OTRAS SESSIONES POR COINCIDENCIAS
		System.out.println("Buscar en otras sesiones");

		long diferencia = neededTimeForFix - session.getRemainingMinutes();

		rooms: for (ConferenceRoom room : conferenceRooms) {

			// for (Session ses : room.getSessions()) {
			//
			// if (ses.getRemainingMinutes() > 0) {
			// diferencia = neededTimeForFix - ses.getRemainingMinutes();
			// for (Conference co : ses.getConferences()) {
			// if (co.getDurationInMinutes().equals(diferencia)) {
			// fixingConferences.add(co);
			// sessionForChangeSchedule = ses;
			// break rooms;
			// }
			// }
			// }
			// }

			for (Session ses : room.getSessions()) {
				totalTime = 0;
				fixingConferences = new ArrayList<>();

				ses.getConferences().sort(new Comparator<Conference>() {

					@Override
					public int compare(Conference c1, Conference c2) {
						return c1.getDurationInMinutes().compareTo(c2.getDurationInMinutes());
					}
				});

				for (Conference co : ses.getConferences()) {

					if (co.getDurationInMinutes() < neededTimeForFix) {
						totalTime += co.getDurationInMinutes();
						fixingConferences.add(co);
					}

					if (totalTime == neededTimeForFix) {
						sessionForChangeSchedule = ses;
						break rooms;
					}
				}
			}
		}

		if (!Objects.isNull(sessionForChangeSchedule)) {
			for (Conference conf : fixingConferences) {
				sessionForChangeSchedule.removeConference(conf);
				freeConferencesQueue.add(conf);
			}
			sessionForChangeSchedule.addConference(nextConference);
			freeConferencesQueue.remove(nextConference);
		}

	}

	private static void fixConference2(Session session, Conference nextConference) throws Exception {


		long neededTimeForFix = nextConference.getDurationInMinutes();
		List<Conference> fixingConferences = new ArrayList<>();
		
		// IR A BUSCAR EN LAS OTRA SESSIONES, NO UNICMANETE EN AL PROPIA

		// System.out.println("");
		// System.out.println("***");
		// System.out.println("NO puedo ubicar la conferencia " +
		// nextConference.getName() + ": "
		// + nextConference.getDurationInMinutes() + "min, en session " +
		// session.getName() + ": "
		// + session.getRemainingMinutes() + "min");
		//
		// conferenceRooms.forEach(c -> c.printConferenceRoom());
		// System.out.println(" ");
		// System.out.println("Free Conferences ");
		//
		// for (Conference c : freeConferencesQueue) {
		// c.printConference();
		// }
		// System.out.println("***");

		long diferencia = neededTimeForFix - session.getRemainingMinutes();
		Conference deleteConference = new Conference();
		boolean sucessChanged = false;

		// QUITAR UNA QUE TENGA LA DURACION FALTANTE
		for (Conference conf : session.getConferences()) {
			if (conf.getDurationInMinutes().equals(diferencia)) {
				deleteConference = conf;
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
					sucessChanged = true;
					break;
				}
			}
		}

		// SI ENCONTRE UNA COINCIDENCIA BORRAR
		if (sucessChanged) {
			session.removeConference(deleteConference);
			freeConferencesQueue.add(deleteConference);
			return;
		}

		// BUSCAR EN OTRAS SESSIONES POR COINCIDENCIAS
		long totalTime = 0;
		Session sessionForChangeSchedule = null;
		
		rooms: for (ConferenceRoom room : conferenceRooms) {

			for (Session ses : room.getSessions()) {
				totalTime = 0;
				fixingConferences = new ArrayList<>();

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

					if (co.getDurationInMinutes() < neededTimeForFix) {
						totalTime += co.getDurationInMinutes();
						fixingConferences.add(co);
					}

					if (totalTime == neededTimeForFix) {
						sessionForChangeSchedule = ses;
						break rooms;
					}
				}
			}
		}

		if (!Objects.isNull(sessionForChangeSchedule)) {
			for (Conference conf : fixingConferences) {
				sessionForChangeSchedule.removeConference(conf);
				freeConferencesQueue.add(conf);
			}
			sessionForChangeSchedule.addConference(nextConference);
			freeConferencesQueue.remove(nextConference);
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
		item.setDuration(Duration.ofMinutes(45));
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
		item.setName("Conferencia catorce");
		item.setDuration(Duration.ofMinutes(60));
		inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia quince");
		item.setDuration(Duration.ofMinutes(30));
		inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia diesciseis");
		item.setDuration(Duration.ofMinutes(30));
		inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia disecisiete");
		item.setDuration(Duration.ofMinutes(30));
		inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia diseciocho");
		item.setDuration(Duration.ofMinutes(15));
		inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia disecinueve");
		item.setDuration(Duration.ofMinutes(60));
		inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia veinte");
		item.setDuration(Duration.ofMinutes(60));
		inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia veintiuno");
		item.setDuration(Duration.ofMinutes(45));
		inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia veintidos");
		item.setDuration(Duration.ofMinutes(30));
		inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia veintitres");
		item.setDuration(Duration.ofMinutes(15));
		inputConferences.add(item);

	}

	private static void dummyConference2() {

		inputConferences = new ArrayList<>();

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
		item.setName("Conferencia catorce");
		item.setDuration(Duration.ofMinutes(60));
		inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia quince");
		item.setDuration(Duration.ofMinutes(30));
		inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia diesciseis");
		item.setDuration(Duration.ofMinutes(30));
		inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia disecisiete");
		item.setDuration(Duration.ofMinutes(30));
		inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia diseciocho");
		item.setDuration(Duration.ofMinutes(15));
		inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia disecinueve");
		item.setDuration(Duration.ofMinutes(60));
		inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia veinte");
		item.setDuration(Duration.ofMinutes(60));
		inputConferences.add(item);

		// item = new Conference();
		// item.setName("Conferencia veintiuno");
		// item.setDuration(Duration.ofMinutes(45));
		// inputConferences.add(item);
		//
		// item = new Conference();
		// item.setName("Conferencia veintidos");
		// item.setDuration(Duration.ofMinutes(30));
		// inputConferences.add(item);
		//
		// item = new Conference();
		// item.setName("Conferencia veintitres");
		// item.setDuration(Duration.ofMinutes(15));
		// inputConferences.add(item);

	}

	private static void dummyConference3() {

		inputConferences = new ArrayList<>();

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
		item.setName("Conferencia catorce");
		item.setDuration(Duration.ofMinutes(60));
		inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia quince");
		item.setDuration(Duration.ofMinutes(30));
		inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia diesciseis");
		item.setDuration(Duration.ofMinutes(30));
		inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia disecisiete");
		item.setDuration(Duration.ofMinutes(30));
		inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia diseciocho");
		item.setDuration(Duration.ofMinutes(15));
		inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia disecinueve");
		item.setDuration(Duration.ofMinutes(60));
		inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia veinte");
		item.setDuration(Duration.ofMinutes(60));
		inputConferences.add(item);

		// item = new Conference();
		// item.setName("Conferencia veintiuno");
		// item.setDuration(Duration.ofMinutes(45));
		// inputConferences.add(item);

		item = new Conference();
		item.setName("Conferencia veintidos");
		item.setDuration(Duration.ofMinutes(15));
		inputConferences.add(item);

		// item = new Conference();
		// item.setName("Conferencia veintitres");
		// item.setDuration(Duration.ofMinutes(15));
		// inputConferences.add(item);

	}

}
