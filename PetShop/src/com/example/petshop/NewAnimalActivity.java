package com.example.petshop;

import java.util.ArrayList;
import java.util.Locale;

import com.example.petshop.data.Animal;
import com.example.petshop.data.AnimalManager;
import com.example.petshop.http.SimpleHttpClient;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
	private Locale locale = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_animal);
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);               
	    Configuration config = getBaseContext().getResources().getConfiguration();

	    String lang = settings.getString("pref_set_language", "Spanish");
	    if (lang.equals("Español")||lang.equals("Spanish")){
	    	lang="es";
		}else{
			lang="en";
		}
	    if (! "".equals(lang) && ! config.locale.getLanguage().equals(lang))
	    {
	        locale = new Locale(lang);
	        Locale.setDefault(locale);
	        config.locale = locale;
	        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
	    }
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
	
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
	    super.onConfigurationChanged(newConfig);
	    if (locale != null)
	    {
	        newConfig.locale = locale;
	        Locale.setDefault(locale);
	        getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
	    }
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
		
		//Enviar x internet 
		sendValues("tempmax"+newAnimal.getLocation(),Double.valueOf(newAnimal.getMaxTemp()).toString());
		sendValues("tempmin"+newAnimal.getLocation(),Double.valueOf(newAnimal.getMinTemp()).toString());
		sendValues("lightmax"+newAnimal.getLocation(),Double.valueOf(newAnimal.getMaxLight()).toString());
		sendValues("lightmin"+newAnimal.getLocation(),Double.valueOf(newAnimal.getMinLight()).toString());
		
		
		Log.i("Stop", "Saving data");
    	(new AnimalManager(getApplicationContext())).saveAnimalsOnFile(arrAnimals);
    	Intent intent = new Intent( this, MainActivity.class );
		startActivityForResult(intent,ADD_ANIMAL);
	}
	
	public void sendValues(String variable, String value){
		// First check if there is connectivity
		ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

	    if (networkInfo != null && networkInfo.isConnected()) {
			// OK -> Access the Internet
	    	String ruta="http://json.internetdelascosas.es/arduino/add.php?device_id=8&data_name="+variable+"&data_value="+value;
	    	SimpleHttpClient shc = new SimpleHttpClient(ruta);
			shc.start();	
	    } else {
			// No -> Display error message
	        //Toast.makeText(this, R.string.msg_error_no_connection, Toast.LENGTH_SHORT).show();
	    }		
	}

}
