package com.example.settings;
import com.example.petshop.R;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class MySettingsFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		 // Register for changes in preferences
        PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener((OnSharedPreferenceChangeListener)this.getActivity());
        setSummaries();
	}
	
	private void setSummaries(){
		String key;
		SharedPreferences sharedPreference= PreferenceManager.getDefaultSharedPreferences(getActivity());
			
		key=getString(R.string.pref_contact_name);
		findPreference(key).setSummary(sharedPreference.getString(key, findPreference(key).getSummary().toString()));
		
		key=getString(R.string.pref_contact_email);
		findPreference(key).setSummary(sharedPreference.getString(key, findPreference(key).getSummary().toString()));
		
		key=getString(R.string.pref_set_language);
		findPreference(key).setSummary(sharedPreference.getString(key, findPreference(key).getSummary().toString()));
		
	
	
	}
}
