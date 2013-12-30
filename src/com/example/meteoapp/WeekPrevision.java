package com.example.meteoapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
//import android.widget.TextView;
 
public class WeekPrevision extends Activity {
    /** Called when the activity is first created. */
	
	final String CITY_SELECTED="a_city";
	final String DAY_OF_WEEK="a_day";
	final String TIMESTAMP = "timestamp";
    @SuppressLint("SimpleDateFormat")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week_prevision);
        
        Intent intent = getIntent();
        String city=null, day=null;
		if (intent != null) 
		{
			city=intent.getStringExtra(CITY_SELECTED);
			day = intent.getStringExtra(DAY_OF_WEEK);
			//TextView previsionLabel = (TextView)findViewById(R.id.previsionLabel);
			//previsionLabel.setText(city);
		}
		
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
		WeatherIcon wi = new WeatherIcon();	
		
		String previsionWeekUrl = "http://api.openweathermap.org/data/2.5/forecast?q="+city+",fr&mode=json&units=metric&lang=fr"; 	
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(previsionWeekUrl);
		JSONArray previsionArray = null;
		SimpleDateFormat formatage = new SimpleDateFormat("EEEE d MMM yyyy", Locale.FRANCE);
		ArrayList<HashMap<String, Object>> previsionList = new  ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> previsionItem;
		String tempOfDay=null, iconWeatherOfDay=null;
		int hour=0;
		try 
		{
			previsionArray = json.getJSONArray("list");
		    for(int i = 0; i <= previsionArray.length()-1; i++)
		    {
		        JSONObject c = previsionArray.getJSONObject(i);     
		        String dt_text = c.getString("dt_txt");
		        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
		        Date d = sdf.parse(dt_text);
		        String jsonDate = formatage.format(d.getTime());
		        //Log.v("Test d'égalité", day+" "+jsonDate);
		        if(jsonDate.equals(day))
		        {		   
		        	Log.v("oui",dt_text);
		        	previsionItem = new HashMap<String,Object>();
		        	/*SimpleDateFormat formatHeure = new SimpleDateFormat("hh");
		        	d = formatHeure.parse(dt_text);
		        	String hourOfDay = formatHeure.format(d.getTime());*/
		        	Log.v("Heure", hour+"h");
		        	JSONObject main = c.getJSONObject("main");
		        	tempOfDay = main.getString("temp");
		        	Log.v("temperature",tempOfDay);
		        	
		        	JSONArray arrayWeather = c.getJSONArray("weather");
		        	iconWeatherOfDay = arrayWeather.getJSONObject(0).getString("icon");
		        	Log.v("icone", iconWeatherOfDay);
			        int pos = (Integer) wi.getFlagByIconCode(iconWeatherOfDay);

		        	previsionItem.put("hour", hour+"h");
		        	previsionItem.put("temperature", tempOfDay+" C°");
		        	previsionItem.put("weather", flags[pos]);
		        	previsionList.add(previsionItem);
		        	hour=hour+3;
		        }
		    }
		} catch (JSONException e) {
		    e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ListView listPrevision = (ListView)findViewById(R.id.weekPrevisionListView);
		SimpleAdapter adaptater = new SimpleAdapter (this.getBaseContext(), previsionList, R.layout.prevision_week_item, new String[] {"hour", "temperature", "weather"}, new int[] {R.id.dayWeekLabel, R.id.temperatureWeekPrevision, R.id.img});			
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

    }
}