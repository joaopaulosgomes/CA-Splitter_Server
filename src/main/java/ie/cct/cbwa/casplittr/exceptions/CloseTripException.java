package ie.cct.cbwa.casplittr.exceptions;

public class CloseTripException extends RuntimeException{
	
	public CloseTripException(String errorMessage) {
		super (errorMessage);
	}

	public CloseTripException(String errorMessage, Throwable cause) {
		super (errorMessage, cause);
		
	}
}
