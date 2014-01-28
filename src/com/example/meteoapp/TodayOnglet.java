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
import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/*
 * Classe permettant de construire l'Activity TodayOnglet
 * Elle permet d'affich�e des informations concernant la m�t�o courantes (m�t�o, temperatures) ainsi que des pr�visions sur la journ�e
 * Cette Activity est appell�e par l'Activity MeteoPage dans un onglet
 */
@SuppressLint("HandlerLeak")
public class TodayOnglet extends Activity 
{
	//Tags permettant de r�cuperer des variables transmis au travers d'objets Intent
	final String CITY_SELECTED="a_city";
	final String CP_SELECTED = "a_cp";
	public static final String PREF_FILENAME = "cache.xml";
	public static final String KEY = "my_data";
	public SharedPreferences prefs;
	private Calendar heureFrance;
	private JSONParser jParser;	
	private Bundle extras=null;
	private SQLiteDatabase db;
	private Editor editor=null;
	
    @Override
    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     * M�thode prot�g�e de type void
     * Elle est invoqu�e � la constuction de l'Activity
     */    
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today_onglet);
              
        extras = getIntent().getExtras();
		TextView cityLabel = (TextView)findViewById(R.id.selectedCity);
		if (extras != null) 
		{
			//R�cup�ration et affichage de la ville dans un objet TextView
			cityLabel.setText(extras.getString(CITY_SELECTED));
			Log.v("Ville et CP", extras.getString(CITY_SELECTED)+" "+extras.getString(CP_SELECTED));
		}					
        
        initDB();//Connexion � la base de donn�es si existante, sinon la cr�er puis se connecte

        Button favButton = (Button)findViewById(R.id.favButton);
        boolean isFavoris = checkFav(extras.getString(CITY_SELECTED)); //V�rifie si la ville est parmis les favoris
        if(isFavoris)
        {	    	
        	favButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fav_btn_on, 0, 0, 0); //Changement de l'image
        	Log.v("City", "est favoris");
        }
    }
    
    @Override
    /*
     * (non-Javadoc)
     * @see android.app.Activity#onStart()
     * M�thode prot�g�e de type void
     * Elle est appell�e lorsque l'Activity est construite et sur le point d'�tre lanc�e
     */
    protected void onStart()
    {
		super.onStart();
		
		//Chargement du fichier de pr�f�rences.
		prefs = getSharedPreferences(PREF_FILENAME, MODE_PRIVATE);
		if(prefs.getString(KEY, null)!=null)
		{
			Log.v("Cache", "Chargement");
			loadCache();
		}
		else
		{
			editor = prefs.edit();
			Log.v("Cache", "Cr�ation du fichier");
			displayToday();
		}
		
		String currentPrevisionCity = "http://api.openweathermap.org/data/2.5/forecast/city?q="+extras.getString(CITY_SELECTED)+"&lang=fr&units=metric"; //URL permettant d'avoir des pr�visions m�t�o sur la journ�e
		Log.v("Prevision url", currentPrevisionCity);
		ArrayList<HashMap<String, Object>> previsionList = new  ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> previsionItem;
        
        //Tableau contenant l'ensemble des images m�t�o existantes
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
        R.drawable.icon_50n};
        
        JSONObject json = jParser.getJSONFromUrl(currentPrevisionCity);
		JSONArray previsionArray = null;
		String hourPrevision=null, iconPrevision=null;
		double temperaturePrevision;
		WeatherIcon wi = new WeatherIcon();
		try 
		{
			//R�cup�ration d'un ArrayList list
			previsionArray = json.getJSONArray("list");		    
		    for(int i = 0; i < 8; i++)
		    {
		        JSONObject c = previsionArray.getJSONObject(i);
		         
		        long timestamp = Long.parseLong(c.getString("dt")); //R�cup�ration d'un timeStamp, representant la date exacte de pr�vision
		        Date d = new Date(timestamp * 1000);
		        heureFrance.setTime(d);
		        hourPrevision = Integer.toString(heureFrance.get(Calendar.HOUR)); //R�cup�ration de l'heure			        
		        //Log.v("HourPrevision", hourPrevision);
		        
		        JSONArray detailWeather = c.getJSONArray("weather");//R�cup�ration de la m�t�o
		        iconPrevision =  detailWeather.getJSONObject(0).getString("icon");//R�cup�ration de l'image associ�e au type de m�t�o
		        //Log.v("IconPrevision", iconPrevision);
		        
		        JSONObject item = c.getJSONObject("main");//R�cup�ration d'un objet JSONObject contenant des informations relatives aux temperatures
		        
		        temperaturePrevision = item.getDouble("temp");		        
		        int roundTemp = (int)Math.round(temperaturePrevision); //R�cuperation de la temperature arrondie		        
		        //Log.v("Temperature prevision", Integer.toString(roundTemp));
		        
		        int pos = (Integer) wi.getFlagByIconCode(iconPrevision);//R�cup�ration de l'image en locale
		        
		        previsionItem = new HashMap<String, Object>();
		        previsionItem.put("temperature", Integer.toString(roundTemp)+"C�");
		        previsionItem.put("weather", flags[pos]);
		        previsionItem.put("hour", hourPrevision+"h");
		        previsionList.add(previsionItem);//Chargement de l'ArrayList
		    }
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}

		ListView listPrevision = (ListView)findViewById(R.id.todayPrevisionListView);
		SimpleAdapter adaptater = new SimpleAdapter (this.getBaseContext(), previsionList, R.layout.prevision_today_item, new String[] {"hour", "temperature", "weather"}, new int[] {R.id.hourPrevision, R.id.temperaturePrevision, R.id.weatherPrevisionImage});			
		listPrevision.setAdapter(adaptater);			
    }
    
    /*
     * M�thode publique de type void
     * Elle permet de revenir � l'Activity pr�c�dente
     */
    public void back(View v)
    {
	    super.onRestart(); //Rafraichissement de l'Activity HomePage
	    Intent intent = new Intent(TodayOnglet.this, HomePage.class); 
	    startActivity(intent); //Appel de l'Activity HomePage
    	finish(); //Fermeture de l'Activity TodayOnglet
    }
    
    /*
     * M�thode publique de type void
     * Elle permet d'ajouter la ville consult�e en favoris dans l'application
     * Si celle-ci est d�j� parmis les favoris, cliquer � nouveau sur le bouton supprimera le favori
     */
	public void addFavoris(View v) 
	{
        boolean isFavoris = checkFav(extras.getString(CITY_SELECTED)); //V�rifie si la ville est d�j� favoris
        Button favButton = (Button)findViewById(R.id.favButton);
        if(!isFavoris)
        {
    		insertFav(extras.getString(CITY_SELECTED), extras.getString(CP_SELECTED)); //Ajout�e dans la base de donn�e
    		favButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fav_btn_on, 0, 0, 0); //Changement de l'image
        }
        else
        {
        	deleteFav(extras.getString(CITY_SELECTED));//Supprim�e de la base de donn�es
        	favButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fav_btn_off, 0, 0, 0);        	
        }
		
	}
	
    /*
     * M�thode priv�e de type void
     * Elle permet de d�marrer une connexion avec la base de donn�es de l'application
     * Si elle n'existe pas, elle sera cr��e
     */
	private void initDB() 
	{
		DBClass dbHelper = new DBClass(getApplicationContext(), DBClass.DB_NAME, null, DBClass.DB_VERSION);	//D�claraiton d'un objet DBClass
	
		try 
		{
			db = dbHelper.getWritableDatabase(); //R�cup�ration de la base de donn�es en lecture et �criture
		}
		catch (SQLiteException e) 
		{
			db = openOrCreateDatabase(DBClass.DB_NAME, MODE_PRIVATE, null); //Ouverture de connexion ou cr�ation de la base de donn�es
		}
	}
	
	/*
	 * M�thode priv�e de type void
	 * Elle permet l'ajout de favoris
	 */
	private void insertFav(String city, String cp) 
	{
		if (db != null) //V�rifie la connexion � la base de donn�es
		{
			//Chargement d'un objet ContentValues pour la requ�te d'insertion
			ContentValues values = new ContentValues();
			values.put(DBClass.CITY_COLUMN, city);
			values.put(DBClass.CP_COLUMN, cp);

			db.insert(DBClass.DB_TABLE, null, values); //Ajout du favoris dont la ville est en param�tre de l'objet ContentValues
			Toast.makeText(this, "Ajouter aux favoris: "+city, Toast.LENGTH_SHORT).show(); //Affichage d'un message de confirmation d'ajout en base
		}
		else
		{
			Toast.makeText(this, "Erreur BDD : non-connect�e", Toast.LENGTH_SHORT).show(); //Affichage d'un message d'erreur
		}
	}
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 * M�thode prot�g�e de type void
	 * Elle est appell�e d�s la destruction de l'Activity TodayOnglet
	 */
	protected void onDestroy() 
	{
		super.onDestroy();
		db.close(); //Fermeture de la connexion � la base de donn�es
	}
	
	/*
	 * M�thode priv�e de type boolean
	 * Elle permet de v�rifier si la ville courante est parmis les favoris.
	 * Retourne vrai si favoris, sinon false
	 */
	private boolean checkFav(String currentCity) 
	{		
		boolean isFav=false;
		if (db != null) //V�rifie la connexion � la base de donn�es
		{	
			/*
			 * Cr�ation d'un curseur pour l'ex�cution d'instructions SQL
			 * Ici : S�lection des villes dont le nom exact de la ville est mentionn� en param�tre
			 */
			Cursor c = db.rawQuery("SELECT cityName FROM favoris WHERE cityName = '"+currentCity+"'", null);			
			isFav=c.moveToFirst();//V�rifie si un r�sultat existe en premi�re position du curseur, si oui retourne true, sinon false			
		}
		return isFav;
	}
	
	/*
	 * M�thode priv�e de type void
	 * Elle permet de supprim�e un favoris dont le nom de la ville est en param�tre
	 */
	private void deleteFav(String currentCity) 
	{
		if (db != null) //V�rifie la connexion � la base de donn�es
		{
			int countRows = db.delete(DBClass.DB_TABLE, "cityName = ?", new String[] {currentCity}); //Suppression de la ville dont le nom exacte est en param�tre
			Log.v("Nb ligne supprim� : ", Integer.toString(countRows));
			Toast.makeText(this, "Favoris supprim�e : "+currentCity, Toast.LENGTH_SHORT).show(); //Affichage d'un message de confirmation de suppression du favoris
		}
		else
		{
			Toast.makeText(this, "Erreur BDD : non-connect�e", Toast.LENGTH_SHORT).show(); //Affichage d'un message d'erreur
		}
	}
	
	public void displayToday()
	{
				
		SimpleDateFormat formater = null;
		Date aujourdhui = new Date();
		formater = new SimpleDateFormat("EEEE d MMM yyyy", Locale.FRANCE);
		String dayOfWeek = formater.format(aujourdhui); //Date du jour fran�aise
		Log.v("date", dayOfWeek);
	
		//Url permettant d'obtenir l'ensemble des informations de la m�t�o courante
		String cityUrl = "http://api.openweathermap.org/data/2.5/weather?q="+extras.getString(CITY_SELECTED)+"&mode=json&units=metric&lang=fr";
		Log.v("Activity today : url", cityUrl);

	 	jParser = new JSONParser();
	 
		JSONObject json = jParser.getJSONFromUrl(cityUrl);//R�cup�ration d'un String JSON au travers d'un URL
		JSONArray arrayWeather = null;
		JSONObject objectWeather=null;
		String weatherOfDay=null, iconWeatherOfDay = null;
		long tempOfDay;
		int roundTempOfDay=0;		
		
		try 
		{			
	        arrayWeather = json.getJSONArray("weather"); //R�cup�ration d'un tableau JSONArray contenant des informations relatives � la m�t�o		     
	        String weather = new String(arrayWeather.getJSONObject(0).getString("description").getBytes("ISO-8859-1"), "UTF-8"); //R�cup�ration 
	        weatherOfDay = Html.fromHtml(weather).toString(); //M�t�o actuelle
	        
        	iconWeatherOfDay = arrayWeather.getJSONObject(0).getString("icon"); //Nom de l'image repr�sentant la m�t�o
        	//Log.v("Icon", iconWeatherOfDay);
        	
        	objectWeather = json.getJSONObject("main"); //R�cup�ration d'un objet JSONObject contenant des informations sur la temperature
        	tempOfDay = objectWeather.getLong("temp"); //Temperature actuelle
        	roundTempOfDay = (int)Math.round(tempOfDay); //Temperature actuelle arrondie
        	//Log.v("Temperature", tempOfDay);	        	
		} 
		catch (JSONException e) 
		{
		    e.printStackTrace();		    
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
				
		heureFrance = Calendar.getInstance();		 
	    heureFrance.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
		String hourOfDay  = heureFrance.get(Calendar.HOUR_OF_DAY)+":"+heureFrance.get(Calendar.MINUTE); //Heure du jour	

		TextView currentDate, currentHour, currentWeather, currentTemp;
		currentDate = (TextView)findViewById(R.id.dateLabel);
		currentHour = (TextView)findViewById(R.id.hourLabel);
		currentWeather = (TextView)findViewById(R.id.weatherLabel);
		currentTemp = (TextView)findViewById(R.id.currentTemperature);
		
		//Affichage des variables r�cup�r�es dans les TextViews
		currentDate.setText(dayOfWeek);
		currentHour.setText(hourOfDay);
		currentWeather.setText(weatherOfDay);
		currentTemp.setText(Integer.toString(roundTempOfDay)+" C�");		
				
		String iconUrl = "http://openweathermap.org/img/w/"+iconWeatherOfDay+".png";//URL permettant de r�cup�r�e l'image de la m�t�o actuelle
		Log.v("TodayOnglet", iconUrl);
		
		ImageView meteoView = (ImageView)findViewById(R.id.meteoView);
		URL myFileUrl;
		InputStream is = null;
		try {
			myFileUrl = new URL (iconUrl);
		    HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
		    conn.setDoInput(true);
		    conn.connect();		 
		    is = conn.getInputStream();//R�cup�ration de l'image
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();			
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		meteoView.setImageBitmap(BitmapFactory.decodeStream(is));//Affichage de l'image t�l�charg�e
		
		//Sauvegarde en cache
		editor.putString("currentDate", dayOfWeek);
		editor.putString("currentHour", hourOfDay);
		editor.putString("currentWeather", weatherOfDay);
		editor.putString("currentTemp", Integer.toString(roundTempOfDay)+" C�");
		editor.putString("iconUrl", iconUrl);		
		editor.commit();
	}
	
	public void loadCache()
	{
		TextView currentDate, currentHour, currentWeather, currentTemp;
		currentDate = (TextView)findViewById(R.id.dateLabel);
		currentHour = (TextView)findViewById(R.id.hourLabel);
		currentWeather = (TextView)findViewById(R.id.weatherLabel);
		currentTemp = (TextView)findViewById(R.id.currentTemperature);
		String iconUrl = prefs.getString("iconUrl","");
		
		currentDate.setText(prefs.getString("currentDate", ""));
		currentHour.setText(prefs.getString("currentHour", ""));
		currentWeather.setText(prefs.getString("currentWeather",""));
		currentTemp.setText(prefs.getString("currentTemp",""));
		
		ImageView meteoView = (ImageView)findViewById(R.id.meteoView);
		URL myFileUrl;
		InputStream is = null;
		try {
			myFileUrl = new URL (iconUrl);
		    HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
		    conn.setDoInput(true);
		    conn.connect();		 
		    is = conn.getInputStream();//R�cup�ration de l'image
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();			
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		meteoView.setImageBitmap(BitmapFactory.decodeStream(is));//Affichage de l'image t�l�charg�e
	}
	
}
