package com.example.petshop;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;
import com.example.petshop.http.SimpleHttpClient;
import com.example.settings.MySettingsActivity;

import data.Animal;
import data.AnimalManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;


public class AnimalDetailsActivity extends Activity {
	private ArrayList<Animal> arrAnimals;
	private int pos;
	private final String TEMPERATURE_IDENTIFICATOR="temp";
	private final String LIGHT_IDENTIFICATOR="light";
	private final String TEMP_ID="Temperature";
	private final String LIGHT_ID="Light";
	private Locale locale = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_animal_details);
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
		//The user did not activate the search
		pos=getIntent().getIntExtra("animalPosition", -1);
		
		ArrayList<String> positions = getIntent().getStringArrayListExtra("positionsOnSearch");
		if (positions!=null){
			//position on arrAnimals if on the main activity the user used the search method
			pos= Integer.parseInt(positions.get(pos));
		}
		fillAnimalDetails();
		updateCurrentValues();
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.animal_details, menu);
		MenuItem mnuShare = menu.findItem(R.id.mnu_share);
		ShareActionProvider shareProvider = (ShareActionProvider) mnuShare.getActionProvider();
		shareProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		//String eventDetail= arrAnimals.get(pos).getName()+" has a temperature of "+ arrAnimals.get(pos).getCurrentTemperature()+" and a light of "+arrAnimals.get(pos).getCurrentLight();
		
		String edtCurrentLight = ((TextView) findViewById(R.id.editCurrentLight)).getText().toString();
		String edtCurrentTemp = ((TextView) findViewById(R.id.editCurrentTemp)).getText().toString();
		String eventDetail= arrAnimals.get(pos).getName()+" has a temperature of "+ edtCurrentTemp+" and a light of "+edtCurrentLight;
		intent.putExtra(Intent.EXTRA_TEXT, eventDetail);
		shareProvider.setShareIntent(intent);
		return true;
	}
	
	public void loadAnimalArray(){
		arrAnimals = (new AnimalManager(getApplicationContext())).loadAnimalsFromFile();
    	if(arrAnimals == null){
    		arrAnimals = new ArrayList<Animal>();
    	}
	}

	private void fillAnimalDetails(){
		
		if (pos!= -1){
			
			TextView txtAnimalName = ((TextView) findViewById(R.id.txtAnimalName));
			Animal animal=arrAnimals.get(pos);
			String animalName= animal.getName();
			if(animalName != null)
				txtAnimalName.setText(animalName);
			
			TextView edtCurrentLight = ((TextView) findViewById(R.id.editCurrentLight));
			String currentLight= Double.valueOf(animal.getCurrentLight()).toString();
			if(currentLight != null)
				edtCurrentLight.setText(currentLight);
			
			TextView edtCurrentTemp = ((TextView) findViewById(R.id.editCurrentTemp));
			String currentTemp= Double.valueOf(animal.getCurrentTemperature()).toString();
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
	private void updateCurrentValues(){
		
		//receive the current temperature
		receiveValue(TEMPERATURE_IDENTIFICATOR, TEMP_ID);
		//receive the current light
		receiveValue(LIGHT_IDENTIFICATOR, LIGHT_ID);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 
		if (item.getItemId()== R.id.mnu_share){
			
			
//			Intent i= new Intent(this, MySettingsActivity.class);
//			startActivity(i);
		}
		
		return super.onOptionsItemSelected(item);
	}
	/* WIFI CONNECTION*/
	
	public void receiveValue(String variable, String parameter){
		// First check if there is connectivity
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

	    if (networkInfo != null && networkInfo.isConnected()) {
			// OK -> Access the Internet
	    	String ruta="http://json.internetdelascosas.es/arduino/getlast.php?device_id=8&data_name="+variable+"&nitems=1";
			new RefreshLocation().execute(ruta,parameter);
	    } else {
			// No -> Display error message
	        Toast.makeText(this, "Error getting the current value of "+parameter, Toast.LENGTH_SHORT).show();
	    }		
	}
	
	private class RefreshLocation extends AsyncTask<String, Integer, ArrayList<String>>{				
		
		@Override
		protected ArrayList<String> doInBackground(String... params) {
			String url = params[0];
			SimpleHttpClient shc = new SimpleHttpClient(url);
	    	publishProgress(40);
			String result = shc.doGet();
			if(result != null){
				ArrayList<String> resultado= new ArrayList<String>();
				resultado.add(params[1]);
				resultado.add(Double.valueOf(getValueFromJson(result)).toString());
				return resultado;
			}else
				return null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		
		@Override
		protected void onPostExecute(ArrayList<String> result) {
			//here does what it need to do with the components
			if(result.get(0).equals(TEMP_ID)){
				TextView edtCurrentTemp = ((TextView) findViewById(R.id.editCurrentTemp));
				String currentTemp= result.get(1);
				if(currentTemp != null){
					if(currentTemp.length()>6){
						currentTemp=currentTemp.substring(0, 6);
					}
					edtCurrentTemp.setText(currentTemp);
					Animal animal=arrAnimals.get(pos);
					animal.setCurrentTemperature(Double.parseDouble(currentTemp));
					arrAnimals.set(pos, animal);
					(new AnimalManager(getApplicationContext())).saveAnimalsOnFile(arrAnimals);
				}					
				//cambiar imagen termometro
				
				
				
			}else if(result.get(0).equals(LIGHT_ID)){
				TextView edtCurrentLight = ((TextView) findViewById(R.id.editCurrentLight));
				String currentLight= result.get(1);
				if(currentLight != null){
					if(currentLight.length()>5){
						currentLight=currentLight.substring(0, 5);
						Animal animal=arrAnimals.get(pos);
						animal.setCurrentLight(Double.parseDouble(currentLight));
						arrAnimals.set(pos, animal);
						(new AnimalManager(getApplicationContext())).saveAnimalsOnFile(arrAnimals);
					}
					edtCurrentLight.setText(currentLight);
				}
				//cambiar imagen bombilla
				
				
			}
			
		}
		
		private double getValueFromJson(String data){
			double value=0;
			try {
				data=data.replace("[", "");
				data=data.replace("]", "");
				JSONObject json = new JSONObject(data);
				value = json.getDouble("data_value");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return value;
		}
							
	}
}
