package com.example.petshop;

import java.util.ArrayList;
import java.util.Locale;

import com.example.petshop.data.Animal;
import com.example.petshop.data.AnimalManager;
import com.example.settings.MySettingsActivity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {
	public static final int ADD_ANIMAL = 0;
	private ArrayList<Animal> arrAnimals;
	private ArrayAdapter <Animal> adpAnimals;
	private ListView list;
	private ArrayList<String> positions ;
	private int animalPosition;
	private ActionMode mActionMode = null;
	public static final int EDIT_ANIMAL = 1;
	private Locale locale = null;
	private int position=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);               
	    Configuration config = getBaseContext().getResources().getConfiguration();

	    String lang = settings.getString("pref_set_language", "Spanish");
	    //String lang= "en";
	    if (lang.equals("Español")){
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
	    arrAnimals = (new AnimalManager(getApplicationContext())).loadAnimalsFromFile();
     list = (ListView)findViewById(R.id.listAnimals);
     createAnimalList();
		// Important: to select single mode
	 list.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
     list.setOnItemClickListener(new OnItemClickListener() {
     	@Override
         public void onItemClick(AdapterView<?> parent, View view, int position,
                 long id) {
         	MainActivity.this.animalClicked(position);
         }
     });
     
     list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
		    // Called when the user long-clicks an item on the list
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View row, int position, long rowid) {
		        if (mActionMode != null) {
		            return false;
		        }
		        animalPosition=position;
		        // Important: to marked the editing row as activated
		        list.setItemChecked(position, true);

		        // Start the CAB using the ActionMode.Callback defined above
		        mActionMode = MainActivity.this.startActionMode(mActionModeCallback);
		        return true;
		    }
		});
     
     Button btnSearch = (Button)findViewById(R.id.btnSearch);
     btnSearch.setOnClickListener(new View.OnClickListener() {
		    @Override
			public void onClick(View v) {
		    	EditText searchAnimal = (EditText) findViewById(R.id.searchAnimal);
				String text= searchAnimal.getText().toString();
				if(text!=null){
					onSearch(text);
				}else{
					createAnimalList();
				}
		      }
		 });
     PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.mnu_add_animal) {
			Intent intent = new Intent( this, NewAnimalActivity.class );
			startActivityForResult(intent, ADD_ANIMAL);
			
		} 
		if (item.getItemId()== R.id.action_settings){
			
			Intent i= new Intent(this, MySettingsActivity.class);
			startActivity(i);
		}
		
		return super.onOptionsItemSelected(item);
	}

	public void createAnimalList(){
    	if(arrAnimals == null){
    		arrAnimals = new ArrayList<Animal>();
    		
    	}
    	adpAnimals = new ArrayAdapter<Animal>(this, android.R.layout.simple_list_item_activated_2, android.R.id.text1, arrAnimals);		
    	list.setAdapter(adpAnimals);
    	adpAnimals.notifyDataSetChanged();
	}

	public void onSearch(String text){
    	
    	//this.createAnimalList();
	    ArrayList<Animal> newArrAnimals=new ArrayList<Animal>();
	    positions = new ArrayList<String>();
	    for(int i=0; i<arrAnimals.size();i++){
	    	if(arrAnimals.get(i).getName().toLowerCase().contains(text.toLowerCase())){	    		
	    		positions.add(Integer.toString(i));
	    		newArrAnimals.add(arrAnimals.get(i));	    		
	    	}
	    }
	    adpAnimals = new ArrayAdapter<Animal>(this, android.R.layout.simple_list_item_activated_2, android.R.id.text1, newArrAnimals);
		list.setAdapter(adpAnimals);
		adpAnimals.notifyDataSetChanged();
	}

	public void animalClicked(int position){
		this.position=position;
		Intent intent = new Intent( this, AnimalDetailsActivity.class );
		animalPosition=position;
		intent.putExtra("animalPosition", animalPosition);
		intent.putExtra("positionsOnSearch", positions);
		Log.i("animalPosition", Integer.toString(animalPosition));
		startActivityForResult(intent,EDIT_ANIMAL);
		list.setItemChecked(position, false);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == ADD_ANIMAL || requestCode == EDIT_ANIMAL){ // If it was an ADD_ITEM, then add the new item and update the list
			if(resultCode == Activity.RESULT_OK){
				
				//this.createEventList();
				this.createAnimalList();
			}		
		}
	}
	
	public void deleteAnimal(){
		
    	(new AnimalManager(getApplicationContext())).deleteAnimal(animalPosition);
    	this.createAnimalList();
    	
    	Log.i("Stop", "Saving data");
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
private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
		
	    // Called when the action mode is created; startActionMode() was called
	    @Override
	    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	        // Inflate a menu resource providing context menu items
	        MenuInflater inflater = mode.getMenuInflater();
	        inflater.inflate(R.menu.animal_action, menu);
	        return true;
	    }

	    // Called when the user enters the action mode
	    @Override
	    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
	    	// Disable the list to avoid selecting other elements while editing one
	    	MainActivity.this.list.setEnabled(false);
	        return true; // Return false if nothing is done
	    }
	    
	    // Called when the user selects a contextual menu item
	    @Override
	    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
	        switch (item.getItemId()) {
	            case R.id.mnu_animal_edit:
	                mode.finish(); // Action picked, so close the CAB and execute action
	                // Edit event
	                Intent intent = new Intent(MainActivity.this, NewAnimalActivity.class );
	                	            	
	            	intent.putExtra("animalPosition", animalPosition);
	            	intent.putExtra("positionsOnSearch", positions);
	            	startActivityForResult(intent,EDIT_ANIMAL);
	                return true;
	            case R.id.mnu_animal_delete:
	                mode.finish(); // Action picked, so close the CAB and execute action
	                // Delete event
	                deleteAnimal();
	                return true;
//	            
	            default:
	                return false;
	        }
	    }

	    // Called when the user exits the action mode
	    @Override
	    public void onDestroyActionMode(ActionMode mode) {
	    	// Re-enable the list after edition
	    	MainActivity.this.list.setEnabled(true);
	    	MainActivity.this.list.setItemChecked(MainActivity.this.position, false);
	        mActionMode = null;
	    }
	};
}
