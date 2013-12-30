package com.example.meteoapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class WeekOnglet extends Activity {
    /** Called when the activity is first created. */
	final String CITY_SELECTED = "a_city";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week_onglet);
        Intent intent = getIntent();
		String citySelected=null;
		if (intent != null) {
			citySelected=intent.getStringExtra(CITY_SELECTED);
			Log.v("city", citySelected);
		}
		Date aujourdhui = new Date();
		Calendar cal = new GregorianCalendar();
		SimpleDateFormat formater = new SimpleDateFormat("EEEE d MMM yyyy", Locale.FRANCE);
        cal.setTime(aujourdhui);
        String nextDay = null;

		ArrayList<HashMap<String, String>> dayList = new  ArrayList<HashMap<String, String>>();
		HashMap<String, String> nextDayItem;		
		
		String previsionWeekUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?q="+citySelected+"&mode=json&units=metric&cnt=7";			
	 	
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(previsionWeekUrl);
		JSONArray previsionArray = null;
		String temperaturePrevision=null;

		try {
		    // Getting Array of Contacts
			previsionArray = json.getJSONArray("list");
		    
		    // looping through All Contacts
		    for(int i = 1; i <= 6; i++){
		        JSONObject c = previsionArray.getJSONObject(i);
			        
		        JSONObject detailWeather = c.getJSONObject("temp");
		        //iconPrevision =  "http://openweathermap.org/img/w/"+detailWeather.getJSONObject(0).getString("icon")+".png";
		        temperaturePrevision =  "M : "+detailWeather.getString("morn")+" C° AM : "+detailWeather.getString("day")+" C° S : "+detailWeather.getString("night")+" C°";
	        
				nextDayItem = new  HashMap<String, String>();
				cal.add(Calendar.DATE, 1);
		        nextDay = formater.format(cal.getTime());
		        nextDayItem.put("day", nextDay);
		        nextDayItem.put("temperature", temperaturePrevision);
		        dayList.add(nextDayItem);
		        //Log.v("date", nextDay);
		    }
		} catch (JSONException e) {
		    e.printStackTrace();
		}
		
		final ListView listViewDay = (ListView)findViewById(R.id.previsionListView);
		SimpleAdapter adaptater = new SimpleAdapter (this.getBaseContext(), dayList, R.layout.prevision_week_item, new String[] {"day", "temperature"}, new int[] {R.id.dayWeekLabel, R.id.temperatureWeekPrevision});			
		listViewDay.setAdapter(adaptater);
		
		ListViewClass.getListViewSize(listViewDay);
		
		Button back_btn = (Button)findViewById(R.id.backButton);
        back_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
        
        final String CITY_SELECTED = "a_city";
        final String A_DAY ="a_day";
        final String currentCity = citySelected;
        listViewDay.setOnItemClickListener(new OnItemClickListener() {
			@Override
        	@SuppressWarnings("unchecked")
         	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
        		HashMap<String, String> map = (HashMap<String, String>) listViewDay.getItemAtPosition(position);
        		Intent intent = new Intent(WeekOnglet.this, WeekPrevision.class);
        		intent.putExtra(CITY_SELECTED, currentCity);
        		intent.putExtra(A_DAY, map.get("day"));
        		startActivity(intent);
        	}
         });
    }

}
