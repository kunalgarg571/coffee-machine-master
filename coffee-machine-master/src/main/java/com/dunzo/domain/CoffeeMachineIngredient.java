package com.dunzo.domain;

public class CoffeeMachineIngredient extends Ingredient {

	private Double minQuantity;

	public CoffeeMachineIngredient(String name, Double quantity, Double minQuantity) {
		super(name, quantity);
		this.minQuantity = minQuantity;
	}

	public Double getMinQuantity() {
		return minQuantity;
	}

	public boolean isLow() {
		return quantity < minQuantity;
	}

	public void refillIngredient(Double value) {
		this.quantity += value;
	}

	public void takeIngredient(Double value) {
		this.quantity -= value;
	}

}
