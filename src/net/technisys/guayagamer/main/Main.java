package net.technisys.guayagamer.main;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.technisys.guayagamer.constant.Constant;
import net.technisys.guayagamer.exceptions.InvalidArgumentsException;
import net.technisys.guayagamer.model.Conference;
import net.technisys.guayagamer.model.ConferenceRoom;
import net.technisys.guayagamer.model.Session;

public class Main {

	private static List<Conference> inputConferences = new ArrayList<>();
	private static LinkedList<Conference> freeConferencesQueue = new LinkedList<>();
	private static List<ConferenceRoom> conferenceRooms;

	static int contador = 0;

	public static void main(String[] args) throws Exception {

		if (args.length == 0) {
			System.out.println("No se ha detallado el archivo de entrada de conferencias");
			System.out.println("Se debe ejecutar ejecutable.jar <filename>");
		}
		String filename = args[0];

		int lineNumber = 1;
		Long duration = 0l;
		String line;
		File file;
		Scanner sc = null;
		Duration totalConferenceDuration = Duration.ofMinutes(0);

		try {
			file = new File(filename);
			sc = new Scanner(file);
			Pattern regex = Pattern.compile(Constant.CONFERENCE_REGEX, Pattern.CASE_INSENSITIVE);

			while (sc.hasNextLine()) {
				line = sc.nextLine();
				duration = 0l;

				Matcher match = regex.matcher(line.trim());
				if (match.find()) {
					String nombreConferencia = match.group(1);

					if (match.group(2).equalsIgnoreCase(Constant.CUARTO)) {
						duration = Constant.CUARTO_DURATION;
					} else {
						duration = Long.valueOf(match.group(3));
					}
					totalConferenceDuration = totalConferenceDuration.plusMinutes(duration);

					inputConferences.add(new Conference(nombreConferencia, Duration.ofMinutes(duration)));

				} else {
					System.out.println("No se pudo procesar la linea; \"" + line
							+ "\", como una entrada de conferencia valida, Favor revise el archivo y ejecute nuevamente");
				}
			}
		} catch (InvalidArgumentsException e) {
			System.out.println(e.getMessage());
		} catch (NumberFormatException e) {
			System.out.println("El numero de duracion enviado no es un numero valido, linea " + lineNumber);
		} finally {
			if (!Objects.isNull(sc)) {
				sc.close();
			}
		}

		conferenceRooms = new ArrayList<>();
		int conferenceRoomNeeded = 0;

		Double resultado = Double.valueOf(totalConferenceDuration.toMinutes())
				/ Double.valueOf(Constant.MAX_HOUR_PER_ROOM * 60);
		conferenceRoomNeeded = (int) resultado.doubleValue();
		if ((resultado - conferenceRoomNeeded) > 0) {
			conferenceRoomNeeded++;
		}

		Session session;
		for (int i = 0; i < conferenceRoomNeeded; i++) {
			ConferenceRoom conferenceRoom = new ConferenceRoom(Constant.CONFERENCE_ROOM + " " + (i + 1));
			session = new Session(conferenceRoom.getName() + " " + Constant.MORNING_SESSION,
					Constant.START_TIME_MORNING_SESSION, Constant.END_TIME_MORNING_SESSION,
					Constant.END_TIME_MORNING_SESSION);
			conferenceRoom.getSessions().add(session);

			// session = new Session(Constant.LUNCH, Constant.START_LUNCH,
			// Constant.END_LUNCH, Constant.END_LUNCH);
			// conferenceRoom.getSessions().add(session);

			session = new Session(conferenceRoom.getName() + " " + Constant.EVENING_SESSION,
					Constant.START_TIME_EVENING_SESSION, Constant.MIN_END_TIME_EVENING_SESSION,
					Constant.MAX_END_TIME_EVENING_SESSION);
			conferenceRoom.getSessions().add(session);

			// session = new Session(Constant.REVIEW, Constant.START_REVIEW,
			// Constant.END_REVIEW, Constant.END_REVIEW);
			// conferenceRoom.getSessions().add(session);

			conferenceRooms.add(conferenceRoom);
		}

		// TODO borrar
		// dummyConference();
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

		System.out.println("FINAL RESULT:");
		conferenceRooms.forEach(c -> c.printConferenceRoom());

	}

	private static void addConferences(Session session) throws Exception {

		if (!freeConferencesQueue.isEmpty()) {
			Conference nextConference = freeConferencesQueue.getFirst();

			if (session.getRemainingMinutes() >= nextConference.getDurationInMinutes()) {
				session.addConference(nextConference);
				freeConferencesQueue.remove(nextConference);
			} else {
				tryOtherConferences(session);
				rescheduleSameSession(session, nextConference);
				rescheduleOtherSession(session, nextConference);
			}
		}
	}

	private static void tryOtherConferences(Session session) throws Exception {
		List<Conference> fixingConferences = new ArrayList<>();

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
		}

	}

	private static void rescheduleSameSession(Session session, Conference nextConference) {

		long neededTimeForFix = nextConference.getDurationInMinutes() - session.getRemainingMinutes();
		Conference deleteConference = new Conference();
		boolean sucessChanged = false;

		// QUITAR UNA QUE TENGA LA DURACION FALTANTE
		for (Conference conf : session.getConferences()) {
			if (conf.getDurationInMinutes().equals(neededTimeForFix)) {
				deleteConference = conf;
				sucessChanged = true;
				break;
			}
		}

		// QUITAR UNA QUE SU DURACION + LO FALTANTE SEA IGUAL A LA DURACION QUE NECESITO
		// UBICAR
		if (!sucessChanged) {
			for (Conference conf : session.getConferences()) {
				if (conf.getDurationInMinutes().equals(nextConference.getDurationInMinutes() + neededTimeForFix)) {
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

	}

	private static void rescheduleOtherSession(Session session, Conference nextConference) throws Exception {

		long neededTimeForFix = nextConference.getDurationInMinutes();
		long sessionTotalTime = 0;

		List<Conference> fixingConferences = new ArrayList<>();
		Session sessionForChangeSchedule = null;

		rooms: for (ConferenceRoom room : conferenceRooms) {

			for (Session ses : room.getSessions()) {
				sessionTotalTime = 0;
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
						sessionTotalTime += co.getDurationInMinutes();
						fixingConferences.add(co);
					}

					if (sessionTotalTime == neededTimeForFix) {
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

	private static void dummyConference() throws InvalidArgumentsException {
		inputConferences = new ArrayList<>();

		// put conference inside rooms
		// TODO delete
		Conference item = new Conference("Conferencia uno", Duration.ofMinutes(30));
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
