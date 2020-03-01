package net.technisys.guayagamer.exceptions;

public class AddConferenceException extends Exception {

	private static final long serialVersionUID = -7828967584005678852L;

	@Override
	public String getMessage() {
		return "The conference is more big than remaing time";
	}

}
