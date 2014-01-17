package com.example.petshop;

import java.util.ArrayList;
import data.Animal;
import data.AnimalManager;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class AnimalDetailsActivity extends Activity {
	private ArrayList<Animal> arrAnimals;
	private ArrayAdapter <Animal> adpAnimals;
	private int pos;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_animal_details);
		
		loadAnimalArray();
		//The user did not activate the search
		pos=getIntent().getIntExtra("animalPosition", -1);
		
		ArrayList<String> positions = getIntent().getStringArrayListExtra("positionsOnSearch");
		if (positions!=null){
			//position on arrAnimals if on the main activity the user used the search method
			pos= Integer.parseInt(positions.get(pos));
		}
		fillAnimalDetails();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.animal_details, menu);
		return true;
	}
	
	public void loadAnimalArray(){
		arrAnimals = (new AnimalManager(getApplicationContext())).loadAnimalsFromFile();
    	if(arrAnimals == null){
    		arrAnimals = new ArrayList<Animal>();
    	}
    	adpAnimals = new ArrayAdapter<Animal>(this, android.R.layout.simple_list_item_1, arrAnimals);
	}

	private void fillAnimalDetails(){
		
		if (pos!= -1){
			
			TextView txtAnimalName = ((TextView) findViewById(R.id.txtAnimalName));
			String animalName= arrAnimals.get(pos).getName();
			if(animalName != null)
				txtAnimalName.setText(animalName);
			
			EditText edtCurrentLight = ((EditText) findViewById(R.id.editCurrentLight));
			String currentLight= "250";
			if(currentLight != null)
				edtCurrentLight.setText(currentLight);
			
			EditText edtCurrentTemp = ((EditText) findViewById(R.id.editCurrentTemp));
			String currentTemp= "25";
			if(currentTemp != null)
				edtCurrentTemp.setText(currentTemp);
			
			TextView txtTempThreshold = ((TextView) findViewById(R.id.txtTempThreshold));
			//TextView txtTempThreshold = ((TextView) findViewById(R.id.txtTempThreshold));
			String tempThreshold= arrAnimals.get(pos).getMinTemp()+" / "+ arrAnimals.get(pos).getMaxTemp();
			if(tempThreshold != null)
				txtTempThreshold.setText(tempThreshold);
			
			TextView txtLightThreshold = ((TextView) findViewById(R.id.txtLight));
			//TextView txtLightThreshold = (TextView) ( findViewById(R.id.txtLightThreshold));
			String lightThreshold= arrAnimals.get(pos).getMinLight()+" / "+arrAnimals.get(pos).getMaxLight();
			if(lightThreshold != null)
				txtLightThreshold.setText(lightThreshold);
			
			
		}
	}
}
