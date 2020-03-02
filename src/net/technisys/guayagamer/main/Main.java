package net.technisys.guayagamer.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.technisys.guayagamer.abstracts.Session;
import net.technisys.guayagamer.constant.Constant;
import net.technisys.guayagamer.exceptions.InvalidArgumentsException;
import net.technisys.guayagamer.exceptions.SessionException;
import net.technisys.guayagamer.model.Conference;
import net.technisys.guayagamer.model.ConferenceRoom;
import net.technisys.guayagamer.model.LunchSession;
import net.technisys.guayagamer.model.RegularSession;
import net.technisys.guayagamer.model.ReviewSession;

public class Main {

	private static List<Conference> inputConferences = new ArrayList<>();
	private static LinkedList<Conference> freeConferencesQueue = new LinkedList<>();
	private static List<ConferenceRoom> conferenceRooms;
	private static Duration totalConferenceDuration;

	static int contador = 0;

	public static void main(String[] args) {

		String filename = "";

		try {

			if (args.length == 0) {
				System.out.println("No se ha detallado el archivo de entrada de conferencias");
				System.out.println("Se debe ejecutar ejecutable.jar <filename>");
				return;
			}
			filename = args[0];

			readInputConferences(filename);
			createConferenceRooms();
			scheduleConferences();
			printConferenceSchedule();
		} catch (InvalidArgumentsException | SessionException e) {
			System.out.println(e.getMessage());
		} catch (FileNotFoundException e) {
			System.out.println("No se puede encontrar el archivo de entrada " + filename);
		} catch (Exception e) {
			System.out.println("Existe un error en la ejecucion del programa con detalle: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void readInputConferences(String filename) throws InvalidArgumentsException, FileNotFoundException {

		Long duration;
		String line;
		File file;
		totalConferenceDuration = Duration.ofMinutes(0);

		file = new File(filename);
		try (Scanner sc = new Scanner(file)) {
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
							+ "\", como una entrada de conferencia valida, la entrada ser� ignorada");
				}
			}
		}

	}

	private static void createConferenceRooms() throws InvalidArgumentsException {

		conferenceRooms = new ArrayList<>();
		int conferenceRoomNeeded = 0;

		Double timeNeeded = Double.valueOf(totalConferenceDuration.toMinutes())
				/ Double.valueOf(Constant.MAX_HOUR_PER_ROOM * 60);
		conferenceRoomNeeded = (int) timeNeeded.doubleValue();
		if ((timeNeeded - conferenceRoomNeeded) > 0) {
			conferenceRoomNeeded++;
		}

		RegularSession session;
		for (int i = 0; i < conferenceRoomNeeded; i++) {
			ConferenceRoom conferenceRoom = new ConferenceRoom(Constant.CONFERENCE_ROOM + " " + (i + 1));
			session = new RegularSession(conferenceRoom.getName() + " " + Constant.MORNING_SESSION,
					Constant.START_TIME_MORNING_SESSION, Constant.END_TIME_MORNING_SESSION,
					Constant.END_TIME_MORNING_SESSION);
			conferenceRoom.getSessions().add(session);

			conferenceRoom.getSessions().add(new LunchSession());

			session = new RegularSession(conferenceRoom.getName() + " " + Constant.EVENING_SESSION,
					Constant.START_TIME_EVENING_SESSION, Constant.MIN_END_TIME_EVENING_SESSION,
					Constant.MAX_END_TIME_EVENING_SESSION);
			conferenceRoom.getSessions().add(session);

			conferenceRoom.getSessions().add(new ReviewSession());

			conferenceRooms.add(conferenceRoom);
		}

	}

	private static void scheduleConferences() throws SessionException {

		freeConferencesQueue = new LinkedList<>(inputConferences);
		for (ConferenceRoom room : conferenceRooms) {
			for (Session se : room.getSessions()) {
				if (se instanceof RegularSession) {
					while (!se.isFull() && !freeConferencesQueue.isEmpty()) {
						addConferences(se);
					}
				}
			}
		}
	}

	private static void printConferenceSchedule() {
		System.out.println("");
		System.out.println("GUAYAGAMER EVENTOS");
		conferenceRooms.forEach(c -> c.printConferenceRoom());
	}

	private static void addConferences(Session session) throws SessionException {

		RegularSession regularSession = (RegularSession) session;

		if (!freeConferencesQueue.isEmpty()) {
			Conference nextConference = freeConferencesQueue.getFirst();

			if (regularSession.getRemainingMinutes() >= nextConference.getDurationInMinutes()) {
				regularSession.addConference(nextConference);
				freeConferencesQueue.remove(nextConference);
			} else {
				tryOtherConferences(regularSession);
				rescheduleSameSession(regularSession, nextConference);
				rescheduleOtherSession(regularSession, nextConference);
			}
		}
	}

	private static void tryOtherConferences(RegularSession session) throws SessionException {
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

	private static void rescheduleSameSession(RegularSession session, Conference nextConference) {

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

	private static void rescheduleOtherSession(RegularSession session, Conference nextConference)
			throws SessionException {

		long neededTimeForFix = nextConference.getDurationInMinutes();
		long sessionTotalTime = 0;

		List<Conference> fixingConferences = new ArrayList<>();
		Session sessionForChangeSchedule = null;

		rooms: for (ConferenceRoom room : conferenceRooms) {

			for (Session ses : room.getSessions()) {
				sessionTotalTime = 0;
				fixingConferences = new ArrayList<>();

				if (!(ses instanceof RegularSession)) {
					continue;
				}

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

}
