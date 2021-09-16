package com.dunzo.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoffeeMachine {
	private int outlets;
	private Map<String, CoffeeMachineIngredient> availableIngredients;
	private Map<String, Beverage> beverageRecipes;

	public CoffeeMachine(int outlets) {
		this.outlets = outlets;
		this.availableIngredients = new HashMap<>();
		this.beverageRecipes = new HashMap<>();
	}

	public int getOutlets() {
		return outlets;
	}

	public Map<String, CoffeeMachineIngredient> getAvailableIngredients() {
		return availableIngredients;
	}

	public Map<String, Beverage> getBeverageRecipes() {
		return beverageRecipes;
	}

	public List<String> getBeverages() {
		return new ArrayList<>(beverageRecipes.keySet());
	}

	public void addIngredient(CoffeeMachineIngredient ingredient) {
		this.availableIngredients.put(ingredient.getName(), ingredient);
	}

	public void addIngredient(Ingredient ingredient) {
		CoffeeMachineIngredient coffeeMachineIngredient = this.availableIngredients.get(ingredient.getName());
		coffeeMachineIngredient.refillIngredient(ingredient.getQuantity());
	}

	public void addBeverageRecipe(Beverage beverage) {
		this.beverageRecipes.put(beverage.getName(), beverage);
	}
}
