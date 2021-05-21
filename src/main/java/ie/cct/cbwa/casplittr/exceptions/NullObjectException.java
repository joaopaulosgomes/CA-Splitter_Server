package ie.cct.cbwa.casplittr.exceptions;

public class NullObjectException extends RuntimeException{
	
	public NullObjectException(String errorMessage) {
		super (errorMessage);
	}

	public NullObjectException(String errorMessage, Throwable cause) {
		super (errorMessage, cause);
		
	}
}