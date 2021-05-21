package ie.cct.cbwa.casplittr.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandlingException {

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException e){
		
		return new ResponseEntity<String>("Username or/and Password are incorrect. Please Try again!", HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(CloseTripException.class)
	public ResponseEntity<String> handleCloseTripException(CloseTripException e){
		
		return new ResponseEntity<String>("This trip has been closed. Expenses cannot be added", HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(NullObjectException.class)
	public ResponseEntity<String> nullObjectException(NullObjectException e){
		
		return new ResponseEntity<String>("The ArrayList is empty", HttpStatus.NO_CONTENT);
	}	
}



