package data;

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
	private int location;
	private int minLight;
	private int maxLight;
	private int minTemp;
	private int maxTemp;
	private double currentLight;
	private double currentTemperature;
	
	public Animal(String name, int location, int minLight, int maxLight, int minTemp, int maxTemp ){
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

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public int getMinLight() {
		return minLight;
	}

	public void setMinLight(int minLight) {
		this.minLight = minLight;
	}

	public int getMaxLight() {
		return maxLight;
	}

	public void setMaxLight(int maxLight) {
		this.maxLight = maxLight;
	}

	public int getMinTemp() {
		return minTemp;
	}

	public void setMinTemp(int minTemp) {
		this.minTemp = minTemp;
	}

	public int getMaxTemp() {
		return maxTemp;
	}

	public void setMaxTemp(int maxTemp) {
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
