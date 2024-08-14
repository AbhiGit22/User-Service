package com.example.exceptions;

public class InvalidPasswordException extends Exception{
	
	public InvalidPasswordException(String message) {
		super(message);
	}

}
