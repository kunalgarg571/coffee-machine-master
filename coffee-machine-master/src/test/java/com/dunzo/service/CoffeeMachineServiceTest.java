package com.dunzo.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import com.dunzo.domain.Beverage;
import com.dunzo.domain.CoffeeMachineIngredient;
import com.dunzo.domain.Ingredient;

public class CoffeeMachineServiceTest {

	private CoffeeMachineService coffeeMachineService;

	private static final int OUTLETS = 4;

	@Before
	public void setUp() {
		coffeeMachineService = new CoffeeMachineServiceImpl(OUTLETS);
		addIngredientsToMachine();
		addBeverageRecipesToMachine();
	}

	@Test
	public void testWhenNotEnoughIngredientsAreNotAvailable() {
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(OUTLETS);
		List<String> drinks = Arrays.asList(new String[] { "hot_tea", "black_tea", "hot_tea", "hot_coffee" });

		for (String beverage : drinks) {
			CoffeeMachineThread coffeeMachineThread = new CoffeeMachineThread(this.coffeeMachineService, beverage);
			executor.execute(coffeeMachineThread);
		}
		executor.shutdown();
		try {
			executor.awaitTermination(2, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals(2, this.coffeeMachineService.getSuccessCount());
		assertEquals(2, this.coffeeMachineService.getFailureCount());
	}

	@Test
	public void testWhenAllDrinksCanBeServed() {
		addIngredient(new Ingredient("hot_water", 6000d));
		addIngredient(new Ingredient("hot_milk", 4000d));
		addIngredient(new Ingredient("ginger", 300d));
		addIngredient(new Ingredient("sugar", 400d));
		addIngredient(new Ingredient("tea_leaves", 300d));
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(OUTLETS);
		List<String> drinks = Arrays.asList(new String[] { "hot_tea", "black_tea", "green_tea", "hot_coffee", "hot_tea",
				"hot_tea", "hot_coffee", "hot_tea" });
		for (String beverage : drinks) {
			CoffeeMachineThread coffeeMachineThread = new CoffeeMachineThread(this.coffeeMachineService, beverage);
			executor.execute(coffeeMachineThread);
		}
		executor.shutdown();
		try {
			executor.awaitTermination(2, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals(8, this.coffeeMachineService.getSuccessCount());
		assertEquals(0, this.coffeeMachineService.getFailureCount());
	}

	@Test
	public void testLowIngredients() {
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(OUTLETS);
		List<String> drinks = Arrays.asList(new String[] { "hot_tea", "hot_tea", "hot_tea" });
		for (String beverage : drinks) {
			CoffeeMachineThread coffeeMachineThread = new CoffeeMachineThread(this.coffeeMachineService, beverage);
			executor.execute(coffeeMachineThread);
		}
		executor.shutdown();
		try {
			executor.awaitTermination(2, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals(2, this.coffeeMachineService.getSuccessCount());
		assertEquals(1, this.coffeeMachineService.getFailureCount());
		assertEquals(Arrays.asList(new String[] { "tea_leaves", "hot_water" }),
				this.coffeeMachineService.getLowIngredients());
	}

	private void addBeverageRecipesToMachine() {
		this.coffeeMachineService.addBeverageRecipe(createHotTeaRecipe());
		this.coffeeMachineService.addBeverageRecipe(createHotCoffeeRecipe());
		this.coffeeMachineService.addBeverageRecipe(createBlacktTeaRecipe());
		this.coffeeMachineService.addBeverageRecipe(createGreenTeaRecipe());
	}

	private Beverage createHotTeaRecipe() {
		List<Ingredient> ingredients = new ArrayList<>();
		ingredients.add(new Ingredient("hot_water", 200d));
		ingredients.add(new Ingredient("hot_milk", 100d));
		ingredients.add(new Ingredient("ginger", 10d));
		ingredients.add(new Ingredient("sugar", 10d));
		ingredients.add(new Ingredient("tea_leaves", 30d));

		return new Beverage("hot_tea", ingredients);
	}

	private Beverage createHotCoffeeRecipe() {
		List<Ingredient> ingredients = new ArrayList<>();
		ingredients.add(new Ingredient("hot_water", 100d));
		ingredients.add(new Ingredient("hot_milk", 400d));
		ingredients.add(new Ingredient("ginger", 30d));
		ingredients.add(new Ingredient("sugar", 40d));
		ingredients.add(new Ingredient("tea_leaves", 30d));

		return new Beverage("hot_coffee", ingredients);
	}

	private Beverage createBlacktTeaRecipe() {
		List<Ingredient> ingredients = new ArrayList<>();
		ingredients.add(new Ingredient("hot_water", 300d));
		ingredients.add(new Ingredient("sugar", 40d));
		ingredients.add(new Ingredient("ginger", 30d));
		ingredients.add(new Ingredient("tea_leaves", 10d));

		return new Beverage("black_tea", ingredients);
	}

	private Beverage createGreenTeaRecipe() {
		List<Ingredient> ingredients = new ArrayList<>();
		ingredients.add(new Ingredient("hot_water", 100d));
		ingredients.add(new Ingredient("green_tea_leaves", 30d));
		ingredients.add(new Ingredient("ginger", 25d));
		ingredients.add(new Ingredient("sugar", 40d));

		return new Beverage("green_tea", ingredients);
	}

	private void addIngredientsToMachine() {
		List<CoffeeMachineIngredient> ingredients = new ArrayList<>();
		ingredients.add(new CoffeeMachineIngredient("hot_water", 500d, 200d));
		ingredients.add(new CoffeeMachineIngredient("hot_milk", 500d, 200d));
		ingredients.add(new CoffeeMachineIngredient("ginger", 100d, 50d));
		ingredients.add(new CoffeeMachineIngredient("sugar", 100d, 50d));
		ingredients.add(new CoffeeMachineIngredient("tea_leaves", 100d, 50d));
		ingredients.add(new CoffeeMachineIngredient("green_tea_leaves", 200d, 50d));
		this.coffeeMachineService.addIngredients(ingredients);
	}

	private void addIngredient(Ingredient ingredient) {
		this.coffeeMachineService.addIngredient(ingredient);
	}
}
