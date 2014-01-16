package com.example.petshop.http;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

public class WifiCommunication {
	
	public void sendValues(Activity activity, String variable, String value){
		// First check if there is connectivity
		ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

	    if (networkInfo != null && networkInfo.isConnected()) {
			// OK -> Access the Internet
	    	String ruta="json.internetdelascosas.es/arduino/add.php?device_id=8&data_name="+variable+"&data_value="+value;
	    	SimpleHttpClient shc = new SimpleHttpClient(ruta);
			shc.start();	
	    } else {
			// No -> Display error message
	        //Toast.makeText(this, R.string.msg_error_no_connection, Toast.LENGTH_SHORT).show();
	    }		
	}
	public void receiveValues(Activity activity, String variable){
		// First check if there is connectivity
		ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

	    if (networkInfo != null && networkInfo.isConnected()) {
			// OK -> Access the Internet
	    	String ruta="http://json.internetdelascosas.es/arduino/getlast.php?device_id=8&data_name="+variable+"&nitems=1";
			new RefreshLocation().execute(ruta);
	    } else {
			// No -> Display error message
	        //Toast.makeText(activity, "", Toast.LENGTH_SHORT).show();
	    }		
	}
	
	
	// Convenience class to access the Internet and update UI elements
	private class RefreshLocation extends AsyncTask<String, Integer, ArrayList<Float>>{				
		
		@Override
		protected ArrayList<Float> doInBackground(String... params) {
			String url = params[0];
			SimpleHttpClient shc = new SimpleHttpClient(url);
	    	publishProgress(40);
			String result = shc.doGet();
			if(result != null){
				ArrayList<Float> resultado= new ArrayList<Float>();
				getValueFromJson(result);
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
		protected void onPostExecute(ArrayList<Float> result) {
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
