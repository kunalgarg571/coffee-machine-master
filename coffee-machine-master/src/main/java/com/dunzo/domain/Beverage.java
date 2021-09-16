package com.dunzo.domain;

import java.util.List;

public class Beverage {
	private String name;
	private List<Ingredient> requiredIngredients;

	public Beverage(String name, List<Ingredient> requiredIngredients) {
		this.name = name;
		this.requiredIngredients = requiredIngredients;
	}

	public String getName() {
		return name;
	}

	public List<Ingredient> getRequiredIngredients() {
		return requiredIngredients;
	}

	public void addIngredient(Ingredient ingredient) {
		requiredIngredients.add(ingredient);
	}
}
