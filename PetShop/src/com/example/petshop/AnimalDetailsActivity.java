package com.example.petshop;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;
import com.example.petshop.http.SimpleHttpClient;
import data.Animal;
import data.AnimalManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

	    String lang = settings.getString("language","es");
	    //String lang= "en";
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
	private void updateCurrentValues(){
		//poner un loading o algo
		
		//receive the current temperature
		receiveValue(TEMPERATURE_IDENTIFICATOR, TEMP_ID);
		//editar el cuadro de texto
		
		//receive the current light
		receiveValue(LIGHT_IDENTIFICATOR, LIGHT_ID);
		//editar el cuadro de texto
		
		//quitar el loading o algo
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
		}
		
		private double getValueFromJson(String data){
			double value=0;
			try {
				JSONObject json = new JSONObject(data);
				value = json.getDouble("data_value");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return value;
		}
			
					
	}
}
