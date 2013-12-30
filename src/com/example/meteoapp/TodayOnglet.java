package com.example.meteoapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class TodayOnglet extends Activity {
    /** Called when the activity is first created. */
final String CITY_SELECTED="a_city";
final String CP_SELECTED = "a_cp";
Intent intent=null;
SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today_onglet);
        intent = getIntent();
		TextView cityLabel = (TextView)findViewById(R.id.selectedCity);
		if (intent != null) 
		{
			cityLabel.setText(intent.getStringExtra(CITY_SELECTED));
		}
		SimpleDateFormat formater = null;
		Date aujourdhui = new Date();
		formater = new SimpleDateFormat("EEEE d MMM yyyy", Locale.FRANCE);
		String dayOfWeek = formater.format(aujourdhui);
		Log.v("date", dayOfWeek);
	
		String cityUrl = "http://api.openweathermap.org/data/2.5/weather?q="+intent.getStringExtra(CITY_SELECTED)+"&mode=json&units=metric&lang=fr";
		Log.v("Activity today : url", cityUrl);

	 	JSONParser jParser = new JSONParser();
	 
		// getting JSON string from URL
		JSONObject json = jParser.getJSONFromUrl(cityUrl);
		JSONArray arrayWeather = null;
		JSONObject objectWeather=null;
		String weatherOfDay=null, iconWeatherOfDay = null, tempOfDay=null;
		initDB();
		try {
			
	        //Log.v("Id ville", id);
	        arrayWeather = json.getJSONArray("weather");		     
	        String weather = new String(arrayWeather.getJSONObject(0).getString("description").getBytes("ISO-8859-1"), "UTF-8");
	        weatherOfDay = Html.fromHtml(weather).toString();
        	//Log.v("Description", weatherOfDay);
        	iconWeatherOfDay = arrayWeather.getJSONObject(0).getString("icon");
        	//Log.v("Icon", iconWeatherOfDay);
        	
        	objectWeather = json.getJSONObject("main");
        	tempOfDay = objectWeather.getString("temp");
        	
        	//Log.v("Temperature", tempOfDay);
	        	
		} catch (JSONException e) {
		    e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		Calendar heureFrance = Calendar.getInstance();		 
	    heureFrance.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
		String hourOfDay  = heureFrance.get(Calendar.HOUR_OF_DAY)+":"+heureFrance.get(Calendar.MINUTE);
	

		TextView currentDate, currentHour, currentWeather, currentTemp;
		currentDate = (TextView)findViewById(R.id.dateLabel);
		currentHour = (TextView)findViewById(R.id.hourLabel);
		currentWeather = (TextView)findViewById(R.id.weatherLabel);
		currentTemp = (TextView)findViewById(R.id.currentTemperature);
		
		currentDate.setText(dayOfWeek);
		currentHour.setText(hourOfDay);
		currentWeather.setText(weatherOfDay);
		currentTemp.setText(tempOfDay+" C°");
		
		ImageView meteoView = (ImageView)findViewById(R.id.meteoView);
		String iconUrl = "http://openweathermap.org/img/w/"+iconWeatherOfDay+".png";
		Log.v("TodayOnglet", iconUrl);
		
		URL myFileUrl;
		InputStream is = null;
		try {
			myFileUrl = new URL (iconUrl);
		    HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
		    conn.setDoInput(true);
		    conn.connect();		 
		    is = conn.getInputStream();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		meteoView.setImageBitmap(BitmapFactory.decodeStream(is));
		
		
		String currentPrevisionCity = "http://api.openweathermap.org/data/2.5/forecast/city?q="+intent.getStringExtra(CITY_SELECTED)+"&lang=fr&units=metric";
		Log.v("Prevision url", currentPrevisionCity);
		ArrayList<HashMap<String, Object>> previsionList = new  ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> previsionItem;
        
        int[] flags = new int[]{
                R.drawable.icon_01d,
                R.drawable.icon_01n,
                R.drawable.icon_02d,
                R.drawable.icon_02n,
                R.drawable.icon_03d,
                R.drawable.icon_03n,
                R.drawable.icon_04d,
                R.drawable.icon_04n,
                R.drawable.icon_09d,
                R.drawable.icon_09n,
                R.drawable.icon_10d,
                R.drawable.icon_10n,
                R.drawable.icon_11d,
                R.drawable.icon_11n,
                R.drawable.icon_13d,
                R.drawable.icon_13n,
                R.drawable.icon_50d,
                R.drawable.icon_50n

    };
		json = jParser.getJSONFromUrl(currentPrevisionCity);
		JSONArray previsionArray = null;
		String hourPrevision=null, temperaturePrevision=null, iconPrevision=null;
		WeatherIcon wi = new WeatherIcon();
			try {
			    // Getting Array of Contacts
				previsionArray = json.getJSONArray("list");
			    
			    // looping through All Contacts
			    for(int i = 0; i < 8; i++){
			        JSONObject c = previsionArray.getJSONObject(i);
			         
			        long timestamp = Long.parseLong(c.getString("dt"));
			        Date d = new Date(timestamp * 1000);
			        heureFrance.setTime(d);
			        hourPrevision = Integer.toString(heureFrance.get(Calendar.HOUR));			        
			        //Log.v("HourPrevision", hourPrevision);
			        
			        JSONArray detailWeather = c.getJSONArray("weather");
			        iconPrevision =  detailWeather.getJSONObject(0).getString("icon");
			        //Log.v("IconPrevision", iconPrevision);
			        
			        JSONObject item = c.getJSONObject("main");
			        temperaturePrevision = item.getString("temp");
			        //Log.v("TemperaturePrevision", temperaturePrevision);
			        
			        //Bitmap bm = BitmapFactory.decodeStream((InputStream) new URL(iconPrevision).getContent());
			        int pos = (Integer) wi.getFlagByIconCode(iconPrevision);
			        previsionItem = new HashMap<String, Object>();
			        previsionItem.put("temperature", temperaturePrevision+"C°");
			        previsionItem.put("weather", flags[pos]);
			        previsionItem.put("hour", hourPrevision+"h");
			        previsionList.add(previsionItem);
			    }
			} catch (JSONException e) {
			    e.printStackTrace();
			}

			//Log.v("Taille array", Integer.toString(previsionList.size()));
			
			ListView listPrevision = (ListView)findViewById(R.id.todayPrevisionListView);
			SimpleAdapter adaptater = new SimpleAdapter (this.getBaseContext(), previsionList, R.layout.prevision_today_item, new String[] {"hour", "temperature", "weather"}, new int[] {R.id.hourPrevision, R.id.temperaturePrevision, R.id.weatherPrevisionImage});			
			listPrevision.setAdapter(adaptater);
			
			ListViewClass.getListViewSize(listPrevision);
			
			Button back_btn = (Button)findViewById(R.id.backButton);
	        back_btn.setOnClickListener(new OnClickListener() {
			
				@Override
				public void onClick(View v) {
					// TODO Auto-generated m;ethod stub
					finish();
				}
			});
	        
	        Button favButton = (Button)findViewById(R.id.favButton);
	        displayData(null);
    }
    
	public void add(View v) 
	{
		insertDB(intent.getStringExtra(CITY_SELECTED));
		Button favButton = (Button)findViewById(R.id.favButton);
		favButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fav_btn_on, 0, 0, 0);				
	}
	
	private void initDB() {
		DBClass dbHelper = new DBClass(getApplicationContext(), DBClass.DB_NAME, null, DBClass.DB_VERSION);
		
		try {
			db = dbHelper.getWritableDatabase(); // Database en lecture/Ã©criture
		}
		catch (SQLiteException e) {
			db = openOrCreateDatabase(DBClass.DB_NAME, MODE_PRIVATE, null);
		}
	}
	
	private void insertDB(String value) 
	{
		if (db != null) 
		{
			Log.v("Ajout DB", value);
			ContentValues values = new ContentValues();
			values.put(DBClass.CITY_COLUMN, value);	
			db.insert(DBClass.DB_TABLE, null, values);
			Toast.makeText(this, "Ajouter aux favoris: "+value, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onStart() 
	{
		super.onDestroy();

	}

	@Override
	protected void onDestroy() 
	{
		super.onDestroy();

		db.close();
	}
	
	public void displayData(View v) 
	{		
		if (db != null) {
			StringBuilder data = new StringBuilder();
			
			Cursor cursor = db.query(DBClass.DB_TABLE, null, null, null, null, null, null);
			Log.v("Nombre de lignes", Integer.toString(cursor.getCount()));
			while (cursor.moveToNext()) 
			{
				String cityName = cursor.getString(cursor.getColumnIndex(DBClass.CITY_COLUMN));
//				Log.v(TAG, "student name: " + studentName);
				data.append(cityName);
			}
			cursor.close();
			Log.v("Resultat : ", data.toString());
		}
	}
}
