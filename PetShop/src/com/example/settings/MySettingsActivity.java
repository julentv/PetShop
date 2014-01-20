package com.example.settings;

import java.util.Locale;
import com.example.petshop.R;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;

public class MySettingsActivity extends Activity implements OnSharedPreferenceChangeListener{
	private PreferenceFragment fragment;
	private Locale locale = null;
	private Configuration config;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragment=new MySettingsFragment();
		getFragmentManager().beginTransaction()
		.replace(android.R.id.content, fragment)
		.commit();
		
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);               
	     config = getBaseContext().getResources().getConfiguration();

	    String lang = settings.getString("pref_set_language", "Spanish");
	    //String lang= "en";
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.my_settings, menu);
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
	
	

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		
		if(key.equals(this.getString(R.string.pref_contact_name)))
			fragment.findPreference(key).setSummary(sharedPreferences.getString(key, fragment.findPreference(key).getSummary().toString()));
		
		if(key.equals(this.getString(R.string.pref_contact_email)))
			fragment.findPreference(key).setSummary(sharedPreferences.getString(key, fragment.findPreference(key).getSummary().toString()));
			
		if(key.equals(this.getString(R.string.pref_set_language))){
			
			fragment.findPreference(key).setSummary(sharedPreferences.getString(key, ""));
			String language=sharedPreferences.getString("pref_set_language", "Spanish");
			if (language.equals("Español")||language.equals("Spanish")){
				language="es";
			}else{
				language="en";
			}
			if (! "".equals(language) && ! config.locale.getLanguage().equals(language))
		    {
		        locale = new Locale(language);
		        Locale.setDefault(locale);
		        config.locale = locale;
		        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
		    }
			Log.i("language", language);
			
		
		}
		
	}

}
