package com.example.petshop.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import android.content.Context;


public class AnimalManager {
	private static final String FILENAME = "AnimalGroup";
	private Context mContext;
	
	public AnimalManager(Context c){
		mContext = c;
	}
	public ArrayList<Animal> loadAnimalsFromFile(){
		try {
			FileInputStream fis = mContext.openFileInput(FILENAME);
			ObjectInputStream ois = new ObjectInputStream(fis);
			@SuppressWarnings("unchecked")
			ArrayList<Animal> arr = (ArrayList<Animal>) ois.readObject();
			ois.close();
			fis.close();
			return arr;
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (OptionalDataException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;		
	}
	public void saveAnimalsOnFile(ArrayList<Animal> arr){
		try {
			FileOutputStream fos = mContext.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(arr);
			oos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Saves the data of the animal sending to the server the min and max values of the light
	 * and the temperature, and saving in memory the full animal. It return true if the saving succeeded.
	 * @return true if the animal was saved.
	 */
	public boolean saveAnimal(Animal animal){
		//validate there is not already an animal with the same location
		//send the max and min values to the server. The max and min from each variable goes into the same parameter.
		//if no errors save the data on the memory
		//return true if it was saved 
		
		return false;
	}
	/**
	 * Loads the animal with the determined location from memory.
	 * @param location Location id of the animal
	 * @return the animal with the determined location or null if there is not one.
	 */
	public Animal loadAnimal(int location){
		ArrayList<Animal>animals=this.loadAnimalsFromFile();
		Animal animal=null;
		for(int i=0,ii=animals.size();i<ii&&animal==null;i++){
			if(animals.get(i).getLocation()==location){
				animal=animals.get(i);
			}
		}
		return animal;
	}
	
	public void deleteAnimal(int position){
		ArrayList<Animal>animals=this.loadAnimalsFromFile();
		animals.remove(position);
		this.saveAnimalsOnFile(animals);
	}
	
}
