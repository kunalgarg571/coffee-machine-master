package com.dunzo.service;

import java.util.List;

import com.dunzo.domain.Beverage;
import com.dunzo.domain.CoffeeMachineIngredient;
import com.dunzo.domain.Ingredient;

public interface CoffeeMachineService {
	public void addBeverageRecipe(Beverage beverage);
		
	public void getBeverage(String beverageName);
	
	public List<String> getLowIngredients();
			
	public void addIngredients(List<CoffeeMachineIngredient> ingredients);
	
	public void addNewIngredient(CoffeeMachineIngredient ingredient);
	
	public void addIngredient(Ingredient ingredient);
	
	public int getSuccessCount();
	
	public int getFailureCount();
}
