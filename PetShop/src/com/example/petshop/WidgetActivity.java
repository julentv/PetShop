package com.example.petshop;

import java.util.ArrayList;
import data.Animal;
import data.AnimalManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RemoteViews;

public class WidgetActivity extends Activity {
	private ArrayList<Animal> arrAnimals;
	private ArrayAdapter <Animal> adpAnimals;
	private ListView list;
	
	int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_widget);
		createAnimalList();
	     list = (ListView)findViewById(R.id.lstWidgetAnimal);
	     list.setOnItemClickListener(new OnItemClickListener() {
	     	@Override
	         public void onItemClick(AdapterView<?> parent, View view, int position,
	                 long id) {
	         	animalClicked(view, position, id);
	         }
	     });
	     Intent intent = getIntent();
	        Bundle extras = intent.getExtras();
	        if (extras != null) {
	 
	            mAppWidgetId = extras.getInt(
	                    AppWidgetManager.EXTRA_APPWIDGET_ID,
	                    AppWidgetManager.INVALID_APPWIDGET_ID);
	        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.widget, menu);
		return true;
	}

	public void createAnimalList(){
		arrAnimals = (new AnimalManager(getApplicationContext())).loadAnimalsFromFile();
    	
    	if(arrAnimals == null){
    		arrAnimals = new ArrayList<Animal>();
    		
    	 	}

    	adpAnimals = new ArrayAdapter<Animal>(this, android.R.layout.simple_list_item_1,  arrAnimals);		
    	list = (ListView)findViewById(R.id.lstWidgetAnimal);
    	list.setAdapter(adpAnimals);
    	
    	adpAnimals.notifyDataSetChanged();
	}
	
	public String searchName(int position){
		String animalName= arrAnimals.get(position).getName();
		return animalName;
	}
	
	public void animalClicked(View v, int position, long id){
		
		 String animalName= searchName(position);
             // Create an Intent to launch WidgetConfigurationActivity screen
             Intent intent = new Intent(getBaseContext(), WidgetActivity.class);

             intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
             // Creating a pending intent, which will be invoked when the user
             // clicks on the widget
             PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0,
                                             intent, PendingIntent.FLAG_UPDATE_CURRENT);

             // Getting an instance of WidgetManager
             AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getBaseContext());

             // Instantiating the class RemoteViews with widget_layout
             RemoteViews views = new RemoteViews(getBaseContext().getPackageName(), R.layout.layout_widget);

             // Setting the background color of the widget
             views.setTextViewText(R.id.txtFavourite, animalName);
             
             Intent intentOpen = new Intent(Intent.ACTION_VIEW,null, getBaseContext(), AnimalDetailsActivity.class);
                intentOpen.putExtra("animalPosition", position);
				PendingIntent pendingIntentOpen = PendingIntent.getActivity(getBaseContext(), 0, intentOpen, PendingIntent.FLAG_CANCEL_CURRENT);
				views.setOnClickPendingIntent(R.id.layoutWidget, pendingIntentOpen);
             // Tell the AppWidgetManager to perform an update on the app widget
             appWidgetManager.updateAppWidget(mAppWidgetId, views);

             // Return RESULT_OK from this activity
             Intent resultValue = new Intent();
             resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
             setResult(RESULT_OK, resultValue);
             finish();
	}
}
