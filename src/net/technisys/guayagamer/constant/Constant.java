package net.technisys.guayagamer.constant;

import java.time.LocalTime;

public class Constant {

	private Constant(){
	}
	
	// morning session
	public static final LocalTime START_TIME_MORNING_SESSION = LocalTime.of(9, 0);
	public static final LocalTime END_TIME_MORNING_SESSION = LocalTime.of(12, 0);
	
	// evening session
	public static final LocalTime START_TIME_EVENING_SESSION = LocalTime.of(13, 0);
	public static final LocalTime MIN_END_TIME_EVENING_SESSION = LocalTime.of(16, 0);
	public static final LocalTime MAX_END_TIME_EVENING_SESSION = LocalTime.of(17, 0);

	// lunch
	public static final LocalTime START_LUNCH = LocalTime.of(13, 0);
	public static final LocalTime END_LUNCH = LocalTime.of(14, 0);
	
	
	public static final String CONFERENCE_ROOM = "SALA";
	public static final String MORNING_SESSION = "SESION DIA";
	public static final String EVENING_SESSION = "SESION TARDE";
	
	
	
	public static final int MIN_HOUR_PER_ROOM = 6;
	public static final int MAX_HOUR_PER_ROOM = 7;
	
	public static final int MIN_HOURS_SESSION1 = 3;
	public static final int MAX_HOURS_SESSION1 = 3;
	
	public static final int MIN_HOURS_SESSION2 = 3;
	public static final int MAX_HOURS_SESSION2 = 4;
	
}
