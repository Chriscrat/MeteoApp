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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

/*
 * Classe permettant la construction de l'Activity WeekOnglet
 * Elle permet d'afficher sous forme de liste, un r�sum� des pr�visions des cinq prochains jours � compter de la date du jour
 * Cette Activity est appel�e par l'Activity MeteoPage
 */
public class WeekOnglet extends Activity 
{
	//Tags permettant de r�cuperer des variables transmis au travers d'objets Intent
	final String CITY_SELECTED = "a_city";
    final String A_DAY ="a_day";
	
    @Override
    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     * M�thode publique de type void
     * Elle est invoqu�e � la constuction de l'Activity
     */
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week_onglet);
        Intent intent = getIntent();
		String citySelected=null;
		if (intent != null) 
			citySelected=intent.getStringExtra(CITY_SELECTED);//R�cup�ration de la ville

		Date aujourdhui = new Date();
		Calendar cal = new GregorianCalendar();
		SimpleDateFormat formater = new SimpleDateFormat("EEEE d MMM yyyy", Locale.FRANCE);
        cal.setTime(aujourdhui);//Date du jour
        String nextDay = null;

		ArrayList<HashMap<String, String>> dayList = new  ArrayList<HashMap<String, String>>();
		HashMap<String, String> nextDayItem;		
		
		String previsionWeekUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?q="+citySelected+"&mode=json&units=metric&cnt=7"; //URL permettant de r�cup�r�e l'ensemble des informations pr�visionnelles m�t�os synth�tis�es			
	 	
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(previsionWeekUrl);
		JSONArray previsionArray = null;
		String temperaturePrevision=null;

		try 
		{
			previsionArray = json.getJSONArray("list");//R�cup�ration d'un Array JSONArray list
		    
		    for(int i = 1; i <= 5; i++)// Limite fix�e � 5 car l'API fournis uniquement des pr�visions m�t�os sur 5 jours d'avances
		    {
		        JSONObject c = previsionArray.getJSONObject(i);			        
		        JSONObject detailWeather = c.getJSONObject("temp");//R�cup�ration d'un Objet JSONObject contenant des informations relatives � la temperature

		        //Affichage d�taill� de la temperature pr�vue le matin, apr�s-midi et soir
		        temperaturePrevision =  "Matin : "+ Math.round(detailWeather.getLong("morn"))+" C� Apr�s-Midi : "+Math.round(detailWeather.getLong("day"))+" C� Soir : "+Math.round(detailWeather.getLong("night"))+" C�";
	        
				nextDayItem = new  HashMap<String, String>();
				cal.add(Calendar.DATE, 1); //Incr�mente la date d'un jour
		        nextDay = formater.format(cal.getTime());
		        nextDayItem.put("day", nextDay);
		        nextDayItem.put("temperature", temperaturePrevision);
		        dayList.add(nextDayItem);
		        //Log.v("date", nextDay);
		    }
		} 
		catch (JSONException e) 
		{
		    e.printStackTrace();
		}
		
		final ListView listViewDay = (ListView)findViewById(R.id.previsionListView);
		SimpleAdapter adaptater = new SimpleAdapter (this.getBaseContext(), dayList, R.layout.prevision_week_item, new String[] {"day", "temperature"}, new int[] {R.id.dayWeekLabel, R.id.temperatureWeekPrevision});			
		listViewDay.setAdapter(adaptater);	
        
        
        final String currentCity = citySelected;
        //D�claration d'un listener pour un �v�nement de type clic sur un �l�ment du ListView
        listViewDay.setOnItemClickListener(new OnItemClickListener() 
        {
			@Override
        	@SuppressWarnings("unchecked")
			/*
			 * (non-Javadoc)
			 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
			 * M�thode publique de type void
			 * Elle est invoqu�e lors d'un clic sur un des �l�ments du listView
			 */
         	public void onItemClick(AdapterView<?> a, View v, int position, long id) 
			{
        		HashMap<String, String> map = (HashMap<String, String>) listViewDay.getItemAtPosition(position); //R�cup�ration d'un HashMap � a position cliqu�e
        		
        		Intent intent = new Intent(WeekOnglet.this, WeekPrevision.class);
        		//Chargement des donn�es � envoyer � l'Activity WeekPrevision
        		intent.putExtra(CITY_SELECTED, currentCity);
        		intent.putExtra(A_DAY, map.get("day"));
        		
        		startActivity(intent);//Appel de l'Activity WeekPrevision
        	}
         });
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
