package com.example.meteoapp;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;


public class FavOnglet extends Activity {
    /** Called when the activity is first created. */
	final String CITY_SELECTED ="a_city";
	final String CP_SELECTED = "a_cp";
	SQLiteDatabase db;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fav_onglet);
        
        initDB();
        
        ArrayList<HashMap<String, String>> favList = new ArrayList<HashMap<String, String>>();
        favList = getFavorisList();        
        Log.v("Nombre de favoris", Integer.toString(favList.size()));
    	final ListView listFavoris = (ListView)findViewById(R.id.favListView);
        SimpleAdapter adaptater = new SimpleAdapter (this.getBaseContext(), favList, R.layout.city_item, new String[] {"cityName", "cityPostalCode"}, new int[] {R.id.cityName, R.id.cityPostalCode});
        listFavoris.setAdapter(adaptater);
		
        listFavoris.setOnItemClickListener(new OnItemClickListener() {
			@Override
        	@SuppressWarnings("unchecked")
         	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
        		HashMap<String, String> map = (HashMap<String, String>) listFavoris.getItemAtPosition(position);
        		
        		Intent intent = new Intent(FavOnglet.this, Meteo.class);
        		Bundle extras = new Bundle();
        		
        		String city = map.get("cityName");
        		String cp = map.get("cityPostalCode");
        		
        		extras.putString(CITY_SELECTED, city);
        		extras.putString(CP_SELECTED, cp);
        		intent.putExtras(extras);
        		startActivity(intent);
        	}
         });
    }
    
	private void initDB() 
	{
		DBClass dbHelper = new DBClass(getApplicationContext(), DBClass.DB_NAME, null, DBClass.DB_VERSION);
		
		try {
			db = dbHelper.getWritableDatabase(); // Database en lecture/écriture
		}
		catch (SQLiteException e) {
			db = openOrCreateDatabase(DBClass.DB_NAME, MODE_PRIVATE, null);
		}
	}
	
	public ArrayList<HashMap<String, String>> getFavorisList()
	{
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		if (db != null) 
		{	
			Cursor c = db.rawQuery("SELECT cityName, cityPostalCode FROM favoris ORDER BY cityName ASC", null);
			HashMap<String, String> item;
			Log.v("Nombre de favoris", Integer.toString(c.getCount()));
			while(c.moveToNext())
			{
				item = new HashMap<String, String>();
				item.put("cityName", c.getString(0));
				item.put("cityPostalCode", c.getString(1));
				list.add(item);
			}
		}
		
		
		return list;
	}
}