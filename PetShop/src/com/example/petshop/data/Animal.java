package com.example.petshop.data;

import java.io.Serializable;

public class Animal implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	/* This attribute identifies the cage where the animal is. 
	 * Each cage has its own sensors and actuators. (Acts like unique primary key).
	 */
	private char location;
	private double minLight;
	private double maxLight;
	private double minTemp;
	private double maxTemp;
	private double currentLight;
	private double currentTemperature;
	
	public Animal(String name, char location, double minLight, double maxLight, double minTemp, double maxTemp ){
		this.name=name;
		this.location= location;
		this.maxLight= maxLight;
		this.minLight= minLight;
		this.maxTemp= maxTemp;
		this.minTemp= minTemp;
		this.currentLight=0;
		this.currentTemperature=0;
	}

	@Override
	public String toString() {
		return name + " - " + location ;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public char getLocation() {
		return location;
	}

	public void setLocation(char location) {
		this.location = location;
	}

	public double getMinLight() {
		return minLight;
	}

	public void setMinLight(double minLight) {
		this.minLight = minLight;
	}

	public double getMaxLight() {
		return maxLight;
	}

	public void setMaxLight(double maxLight) {
		this.maxLight = maxLight;
	}

	public double getMinTemp() {
		return minTemp;
	}

	public void setMinTemp(double minTemp) {
		this.minTemp = minTemp;
	}

	public double getMaxTemp() {
		return maxTemp;
	}

	public void setMaxTemp(double maxTemp) {
		this.maxTemp = maxTemp;
	}

	public double getCurrentLight() {
		return currentLight;
	}

	public void setCurrentLight(double currentLight) {
		this.currentLight = currentLight;
	}

	public double getCurrentTemperature() {
		return currentTemperature;
	}

	public void setCurrentTemperature(double currentTemperature) {
		this.currentTemperature = currentTemperature;
	}
	
	
}
