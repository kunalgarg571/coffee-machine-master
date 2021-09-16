package com.dunzo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dunzo.domain.Beverage;
import com.dunzo.domain.CoffeeMachine;
import com.dunzo.domain.CoffeeMachineIngredient;
import com.dunzo.domain.Ingredient;
import com.dunzo.exceptions.GenericException;
import com.dunzo.exceptions.IngredientNotAvailableException;

public class CoffeeMachineServiceImpl implements CoffeeMachineService {
	private static Logger logger = LoggerFactory.getLogger(CoffeeMachineServiceImpl.class);

	private Semaphore semaphore;
	private CoffeeMachine coffeeMachine;
	private int success;
	private int failure;

	public CoffeeMachineServiceImpl(int outlets) {
		this.coffeeMachine = new CoffeeMachine(outlets);
		semaphore = new Semaphore(outlets);
		success = 0;
		failure = 0;
	}

	@Override
	public void getBeverage(String beverageName) {
		try {
			semaphore.acquireUninterruptibly();
			Beverage beverageRecipe = getBeverageRecipe(beverageName);
			collectIngredients(beverageRecipe, this.coffeeMachine.getAvailableIngredients());
			logger.info(dispatchBeverage(beverageName));
			success++;
		} catch (GenericException exception) {
			logger.warn(exception.getMessage());
			failure++;
		} catch (InterruptedException exception) {
			logger.error("Some unexpected error occured, please try again!!");
			failure++;
		} finally {
			semaphore.release();
		}
	}

	private String dispatchBeverage(String beverageName) throws InterruptedException {
		Thread.sleep(2000);
		return beverageName + " is prepared";
	}

	private synchronized void collectIngredients(Beverage beverage,
			Map<String, CoffeeMachineIngredient> availableIngredients) throws GenericException {
		logger.info("Collecting ingredients for " + beverage.getName());
		List<Ingredient> requiredIngredients = beverage.getRequiredIngredients();
		for (Ingredient ingredient : requiredIngredients) {
			if (ingredient.getQuantity() > availableIngredients.get(ingredient.getName()).getQuantity()) {
				throw new IngredientNotAvailableException(beverage.getName() + " cannot be prepared because "
						+ ingredient.getName() + " is not available");
			}
		}
		for (Ingredient ingredient : requiredIngredients) {
			CoffeeMachineIngredient temp = availableIngredients.get(ingredient.getName());
			temp.takeIngredient(ingredient.getQuantity());
		}
		logger.info("Collected ingredients for " + beverage.getName());
	}

	private Beverage getBeverageRecipe(String beverageName) throws GenericException {
		Beverage beverageRecipe = this.coffeeMachine.getBeverageRecipes().get(beverageName);
		if (beverageRecipe == null) {
			throw new GenericException(beverageName + " can't be served");
		}
		return beverageRecipe;
	}

	@Override
	public List<String> getLowIngredients() {
		List<String> lowIngredientList = new ArrayList<>();
		for (CoffeeMachineIngredient ingredient : this.coffeeMachine.getAvailableIngredients().values()) {
			if (ingredient.isLow()) {
				lowIngredientList.add(ingredient.getName());
			}
		}
		return lowIngredientList;
	}

	@Override
	public void addBeverageRecipe(Beverage beverage) {
		if (this.coffeeMachine.getBeverages().size() >= this.coffeeMachine.getOutlets()) {
			logger.warn("Bevarege can't be added, currently all the outlets are being used.");
		} else
			this.coffeeMachine.addBeverageRecipe(beverage); // add a check
	}

	@Override
	public void addIngredients(List<CoffeeMachineIngredient> ingredients) {
		for (CoffeeMachineIngredient ingredient : ingredients) {
			this.coffeeMachine.addIngredient(ingredient);
		}
	}

	@Override
	public void addNewIngredient(CoffeeMachineIngredient ingredient) {
		this.coffeeMachine.addIngredient(ingredient);
	}

	@Override
	public void addIngredient(Ingredient ingredient) {
		if (this.coffeeMachine.getAvailableIngredients().containsKey(ingredient.getName())) {
			this.coffeeMachine.addIngredient(ingredient);
		} else
			logger.error("Ingredient cannot be added, as there is no ingredient of this type");
	}

	@Override
	public int getSuccessCount() {
		return success;
	}

	@Override
	public int getFailureCount() {
		return failure;
	}
}
