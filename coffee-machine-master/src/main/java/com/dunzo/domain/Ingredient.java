package com.dunzo.domain;

public class Ingredient {
	protected String name;
	protected Double quantity;

	public Ingredient(String name, Double quantity) {
		this.name = name;
		this.quantity = quantity;
	}

	public String getName() {
		return name;
	}

	public Double getQuantity() {
		return quantity;
	}

	@Override
	public String toString() {
		return "Ingredient [name=" + name + ", quantity=" + quantity + "]";
	}

}
