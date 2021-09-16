package com.dunzo.service;

public class CoffeeMachineThread extends Thread {
	CoffeeMachineService coffeeMachineService;
	String beverage;
	CoffeeMachineThread(CoffeeMachineService coffeeMachineService, String beverage) {
		this.coffeeMachineService = coffeeMachineService;
		this.beverage = beverage;
	}
	@Override
	public void run() {
		coffeeMachineService.getBeverage(beverage);
	}
}
