package com.example.meteoapp;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
 
public class FavOnglet extends Activity {
    /** Called when the activity is first created. */
	
	SQLiteDatabase db;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fav_onglet);
        
        initDB();
        
        ArrayList<HashMap<String, String>> favList = getFavorisList();        
        
    	ListView listPrevision = (ListView)findViewById(R.id.favListView);
        SimpleAdapter adaptater = new SimpleAdapter (this.getBaseContext(), favList, R.layout.city_item, new String[] {"cityName", "cityPostalCode"}, new int[] {R.id.cityName, R.id.cityPostalCode});
		listPrevision.setAdapter(adaptater);
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
			Cursor c = db.rawQuery("SELECT cityName, cityPostalCode FROM favoris", null);
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