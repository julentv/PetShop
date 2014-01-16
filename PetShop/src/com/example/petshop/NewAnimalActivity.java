package com.example.petshop;

import java.util.ArrayList;

import data.Animal;
import data.AnimalManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

public class NewAnimalActivity extends Activity {
	private ArrayList<Animal> arrAnimals;
	private ArrayAdapter <Animal> adpAnimals;
	public static final int ADD_ANIMAL = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_animal);
		loadAnimalArray();
		
		Button btnSave = (Button)findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new View.OnClickListener() {
			    @Override
				public void onClick(View v) {
			    	createNewAnimal();
			      }
			 });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_animal, menu);
		return true;
	}
	
	public void loadAnimalArray(){
		arrAnimals = (new AnimalManager(getApplicationContext())).loadAnimalsFromFile();
    	if(arrAnimals == null){
    		arrAnimals = new ArrayList<Animal>();
    	}
    	adpAnimals = new ArrayAdapter<Animal>(this, android.R.layout.simple_list_item_1, arrAnimals);
	}
	
	public void createNewAnimal(){
		EditText editName= (EditText)findViewById(R.id.editName);
		String name= editName.getText().toString();
		String location = ((EditText) findViewById(R.id.editLocation)).getText().toString();
		EditText editMinTemp= (EditText)findViewById(R.id.editTempMin);
		String minTemp= editMinTemp.getText().toString();
		EditText editMaxTemp= (EditText) findViewById(R.id.editTempMax);
		String maxTemp= editMaxTemp.getText().toString();
		EditText editMinLight= (EditText)findViewById(R.id.editLightMin);
		String minLight= editMinLight.getText().toString();
		EditText editMaxLight= (EditText) findViewById(R.id.editLightMax);
		String maxLight= editMaxLight.getText().toString();
		
		Animal newAnimal = new Animal(name, Integer.parseInt(location), Integer.parseInt(minLight), Integer.parseInt(maxLight), Integer.parseInt(minTemp), Integer.parseInt(maxTemp));
		
		//add the event to the array
//		if (pos==-1){
			
			arrAnimals.add(newAnimal);
			
//		}
		//edit the corresponding event
//		else{
//			arrEvents.set(pos, newEvent);
//		}
		Log.i("Stop", "Saving data");
    	(new AnimalManager(getApplicationContext())).saveAnimalsOnFile(arrAnimals);
    	Intent intent = new Intent( this, MainActivity.class );
		startActivityForResult(intent,ADD_ANIMAL);
	}

}
