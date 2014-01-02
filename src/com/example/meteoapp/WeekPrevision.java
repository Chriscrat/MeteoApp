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
import android.widget.ListView;
import android.widget.SimpleAdapter;

/*
 * Classe permettant de constuire l'Activity WeekPrevision
 * Elle permet d'afficher sous forme d'une liste, les d�tails m�t�o pr�visionnels du jour s�lectionn�e
 */
public class WeekPrevision extends Activity 
{
	//Tags permettant de r�cuperer des variables transmis au travers d'objets Intent
	final String CITY_SELECTED="a_city";
	final String DAY_OF_WEEK="a_day";
	
    @SuppressLint("SimpleDateFormat")
	@Override
    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     * M�thode prot�g�e de type void
     * Elle est invoqu�e � la constuction de l'Activity
     */
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week_prevision);
        
        Intent intent = getIntent();
        String city=null, day=null;
		if (intent != null) 
		{
			//R�cup�ration de la ville et de la date s�lectionn�e
			city=intent.getStringExtra(CITY_SELECTED);
			day = intent.getStringExtra(DAY_OF_WEEK);
		}
		
		//Tableau d'images de l'application
        int[] flags = new int[]
		{
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
		
		String previsionWeekUrl = "http://api.openweathermap.org/data/2.5/forecast?q="+city+",fr&mode=json&units=metric&lang=fr";//URL permettant d'obtenir des informations d�taill�es pr�visionnelles des 5 prochains jours 	
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
			previsionArray = json.getJSONArray("list"); //R�cuperation d'un Array JSONArray list
		    for(int i = 0; i <= previsionArray.length()-1; i++)
		    {
		        JSONObject c = previsionArray.getJSONObject(i);     
		        String dt_text = c.getString("dt_txt"); //R�cup�ration de la date au format texte
		        
		        //Conversion de la date r�cup�r�e selon le m�me format que la date r�cup�r�e depuis l'intent
		        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd hh:mm:ss"); 
		        Date d = sdf.parse(dt_text);
		        String jsonDate = formatage.format(d.getTime());
		        
		        if(jsonDate.equals(day))//Contr�le si la date r�cup�r� de l'URL correspond � la date r�cup�r�e de l'intent
		        {		   
		        	previsionItem = new HashMap<String,Object>();
		        	Log.v("Heure", hour+"h");
		        	
		        	JSONObject main = c.getJSONObject("main");
		        	tempOfDay = Integer.toString((int)Math.round(main.getLong("temp"))); //R�cup�ration de la temperature arrondis
		        	Log.v("temperature",tempOfDay);
		        	
		        	JSONArray arrayWeather = c.getJSONArray("weather");
		        	iconWeatherOfDay = arrayWeather.getJSONObject(0).getString("icon"); //R�cup�ration du nom de l'image m�t�o
		        	Log.v("icone", iconWeatherOfDay);
		        	
			        int pos = (Integer) wi.getFlagByIconCode(iconWeatherOfDay); //R�cup�ration de la position de l'image du tableau d'image

		        	previsionItem.put("hour", hour+"h");
		        	previsionItem.put("temperature", tempOfDay+" C�");
		        	previsionItem.put("weather", flags[pos]);
		        	previsionList.add(previsionItem);
		        	hour=hour+3; //Incr�mentation de 3 heures
		        }
		    }
		} 
		catch (JSONException e) 
		{
		    e.printStackTrace();
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
		}
		
		ListView listPrevision = (ListView)findViewById(R.id.weekPrevisionListView);
		SimpleAdapter adaptater = new SimpleAdapter (this.getBaseContext(), previsionList, R.layout.prevision_week_item, new String[] {"hour", "temperature", "weather"}, new int[] {R.id.dayWeekLabel, R.id.temperatureWeekPrevision, R.id.img});			
		listPrevision.setAdapter(adaptater);				
    }
    
    /*
     * M�thode publique de type void
     * Elle permet de revenir � l'Activity pr�c�dente
     */
	public void back(View v) 
	{
		finish();
	}
}