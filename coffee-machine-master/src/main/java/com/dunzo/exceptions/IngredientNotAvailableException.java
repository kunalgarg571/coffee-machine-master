package com.dunzo.exceptions;

public class IngredientNotAvailableException extends GenericException {

	private static final long serialVersionUID = -9189750008959622712L;

	public IngredientNotAvailableException(String message) {
		super(message);
	}
}
