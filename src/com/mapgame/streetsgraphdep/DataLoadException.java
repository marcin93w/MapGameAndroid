package com.mapgame.streetsgraphdep;

public class DataLoadException extends Exception {

	private static final long serialVersionUID = 1L;
	Exception innerException;
	
	public DataLoadException() {
		super();
	}
	
	public DataLoadException(String message) {
		super(message);
	}
	
	public DataLoadException(Exception innerException) {
		super();
		this.innerException = innerException;
	}
	
	public Exception getInnerException() {
		return innerException;
	}
	
}
