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
	private int posAnimal;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_animal);
		loadAnimalArray();
		posAnimal=getIntent().getIntExtra("animalPosition", -1);
		ArrayList<String> positions = getIntent().getStringArrayListExtra("positionsOnSearch");
		if (positions!=null){
			//position on arrAnimals if on the main activity the user used the search method
			posAnimal= Integer.parseInt(positions.get(posAnimal));
		}
		if(posAnimal!=-1){
			fillAnimal(arrAnimals.get(posAnimal));
		}
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
	
	public void fillAnimal(Animal animal){
		if (posAnimal!= -1){
			EditText editName= (EditText)findViewById(R.id.editName);
			String animalName= animal.getName();
			if( animalName!= null)
				editName.setText(animalName);
			
			EditText edtLocation = (EditText) findViewById(R.id.editLocation);
			char location = animal.getLocation();
			edtLocation.setText(Character.valueOf(location).toString());
			
			EditText editMinTemp= (EditText)findViewById(R.id.editTempMin);
			editMinTemp.setText(Double.toString(animal.getMinTemp()));
		
			EditText editMaxTemp= (EditText) findViewById(R.id.editTempMax);
			editMaxTemp.setText(Double.toString(animal.getMaxTemp()));
			
			EditText editMinLight= (EditText)findViewById(R.id.editLightMin);
			editMinLight.setText(Double.toString(animal.getMinLight()));
			
			EditText editMaxLight= (EditText) findViewById(R.id.editLightMax);
			editMaxLight.setText(Double.toString(animal.getMaxLight()));
			
			
			}
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
		
		Animal newAnimal = new Animal(name,location.toCharArray()[0], Double.parseDouble(minLight), Double.parseDouble(maxLight), Double.parseDouble(minTemp), Double.parseDouble(maxTemp));
		
		//add the animal to the array
		if (posAnimal==-1){
			
			arrAnimals.add(newAnimal);
			
		}
		//edit the corresponding animal
		else{
			arrAnimals.set(posAnimal, newAnimal);
		}
		Log.i("Stop", "Saving data");
    	(new AnimalManager(getApplicationContext())).saveAnimalsOnFile(arrAnimals);
    	Intent intent = new Intent( this, MainActivity.class );
		startActivityForResult(intent,ADD_ANIMAL);
	}

}
