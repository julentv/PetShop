package com.example.petshop;

import java.util.ArrayList;
import data.Animal;
import data.AnimalManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
	public static final int EDIT_ANIMAL = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
      createAnimalList();
     list = (ListView)findViewById(R.id.listAnimals);
     list.setOnItemClickListener(new OnItemClickListener() {
     	@Override
         public void onItemClick(AdapterView<?> parent, View view, int position,
                 long id) {
         	animalClicked(view, position, id);
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
		
		return super.onOptionsItemSelected(item);
	}

	public void createAnimalList(){
		arrAnimals = (new AnimalManager(getApplicationContext())).loadAnimalsFromFile();
    	
    	if(arrAnimals == null){
    		arrAnimals = new ArrayList<Animal>();
    		
    	 	}

    	adpAnimals = new ArrayAdapter<Animal>(this, android.R.layout.simple_list_item_activated_2, android.R.id.text1, arrAnimals);		
    	list = (ListView)findViewById(R.id.listAnimals);
    	list.setAdapter(adpAnimals);
    	
    	adpAnimals.notifyDataSetChanged();
	}

	public void onSearch(String text){
    	
    	this.createAnimalList();
	    ArrayList<Animal> newArrAnimals=new ArrayList<Animal>();    
	    Animal animal;
	    positions = new ArrayList<String>();
	    for(int i=0; i<arrAnimals.size();i++){
	    	if(arrAnimals.get(i).getName().toLowerCase().contains(text.toLowerCase())){
	    		
	    		animal = arrAnimals.get(i);
	    		positions.add(Integer.toString(i));
	    		newArrAnimals.add(new Animal(animal.getName(),animal.getLocation(), animal.getMaxLight(), animal.getMinLight(), animal.getMaxTemp(), animal.getMinTemp()));
	    		
	    	}
	    }
	    adpAnimals = new ArrayAdapter<Animal>(this, android.R.layout.simple_list_item_1, newArrAnimals);
		list = (ListView)findViewById(R.id.listAnimals);
		list.setAdapter(adpAnimals);
		adpAnimals.notifyDataSetChanged();
	}

	public void animalClicked(View v, int position, long id){
		Intent intent = new Intent( this, AnimalDetailsActivity.class );
		animalPosition=position;
		intent.putExtra("animalPosition", animalPosition);
		intent.putExtra("positionsOnSearch", positions);
		Log.i("animalPosition", Integer.toString(animalPosition));
		startActivityForResult(intent,EDIT_ANIMAL);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == ADD_ANIMAL){ // If it was an ADD_ITEM, then add the new item and update the list
			if(resultCode == Activity.RESULT_OK){
				
				//this.createEventList();
				this.createAnimalList();
			}		
		}
	}
}
