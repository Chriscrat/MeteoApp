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
 * Elle permet d'affichée des informations concernant la météo courantes (météo, temperatures) ainsi que des prévisions sur la journée
 * Cette Activity est appellée par l'Activity MeteoPage dans un onglet
 */
@SuppressLint("HandlerLeak")
public class TodayOnglet extends Activity 
{
	//Tags permettant de récuperer des variables transmis au travers d'objets Intent
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
     * Méthode protégée de type void
     * Elle est invoquée à la constuction de l'Activity
     */    
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today_onglet);
              
        extras = getIntent().getExtras();
		TextView cityLabel = (TextView)findViewById(R.id.selectedCity);
		if (extras != null) 
		{
			//Récupération et affichage de la ville dans un objet TextView
			cityLabel.setText(extras.getString(CITY_SELECTED));
			Log.v("Ville et CP", extras.getString(CITY_SELECTED)+" "+extras.getString(CP_SELECTED));
		}					
        
        initDB();//Connexion à la base de données si existante, sinon la créer puis se connecte

        Button favButton = (Button)findViewById(R.id.favButton);
        boolean isFavoris = checkFav(extras.getString(CITY_SELECTED)); //Vérifie si la ville est parmis les favoris
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
     * Méthode protégée de type void
     * Elle est appellée lorsque l'Activity est construite et sur le point d'être lancée
     */
    protected void onStart()
    {
		super.onStart();
		
		//Chargement du fichier de préférences.
		prefs = getSharedPreferences(PREF_FILENAME, MODE_PRIVATE);
		if(prefs.getString(KEY, null)!=null)
		{
			Log.v("Cache", "Chargement");
			loadCache();
		}
		else
		{
			editor = prefs.edit();
			Log.v("Cache", "Création du fichier");
			displayToday();
		}
		
		String currentPrevisionCity = "http://api.openweathermap.org/data/2.5/forecast/city?q="+extras.getString(CITY_SELECTED)+"&lang=fr&units=metric"; //URL permettant d'avoir des prévisions météo sur la journée
		Log.v("Prevision url", currentPrevisionCity);
		ArrayList<HashMap<String, Object>> previsionList = new  ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> previsionItem;
        
        //Tableau contenant l'ensemble des images météo existantes
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
			//Récupération d'un ArrayList list
			previsionArray = json.getJSONArray("list");		    
		    for(int i = 0; i < 8; i++)
		    {
		        JSONObject c = previsionArray.getJSONObject(i);
		         
		        long timestamp = Long.parseLong(c.getString("dt")); //Récupération d'un timeStamp, representant la date exacte de prévision
		        Date d = new Date(timestamp * 1000);
		        heureFrance.setTime(d);
		        hourPrevision = Integer.toString(heureFrance.get(Calendar.HOUR)); //Récupération de l'heure			        
		        //Log.v("HourPrevision", hourPrevision);
		        
		        JSONArray detailWeather = c.getJSONArray("weather");//Récupération de la météo
		        iconPrevision =  detailWeather.getJSONObject(0).getString("icon");//Récupération de l'image associée au type de météo
		        //Log.v("IconPrevision", iconPrevision);
		        
		        JSONObject item = c.getJSONObject("main");//Récupération d'un objet JSONObject contenant des informations relatives aux temperatures
		        
		        temperaturePrevision = item.getDouble("temp");		        
		        int roundTemp = (int)Math.round(temperaturePrevision); //Récuperation de la temperature arrondie		        
		        //Log.v("Temperature prevision", Integer.toString(roundTemp));
		        
		        int pos = (Integer) wi.getFlagByIconCode(iconPrevision);//Récupération de l'image en locale
		        
		        previsionItem = new HashMap<String, Object>();
		        previsionItem.put("temperature", Integer.toString(roundTemp)+"C°");
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
     * Méthode publique de type void
     * Elle permet de revenir à l'Activity précédente
     */
    public void back(View v)
    {
	    super.onRestart(); //Rafraichissement de l'Activity HomePage
	    Intent intent = new Intent(TodayOnglet.this, HomePage.class); 
	    startActivity(intent); //Appel de l'Activity HomePage
    	finish(); //Fermeture de l'Activity TodayOnglet
    }
    
    /*
     * Méthode publique de type void
     * Elle permet d'ajouter la ville consultée en favoris dans l'application
     * Si celle-ci est déjà parmis les favoris, cliquer à nouveau sur le bouton supprimera le favori
     */
	public void addFavoris(View v) 
	{
        boolean isFavoris = checkFav(extras.getString(CITY_SELECTED)); //Vérifie si la ville est déjà favoris
        Button favButton = (Button)findViewById(R.id.favButton);
        if(!isFavoris)
        {
    		insertFav(extras.getString(CITY_SELECTED), extras.getString(CP_SELECTED)); //Ajoutée dans la base de donnée
    		favButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fav_btn_on, 0, 0, 0); //Changement de l'image
        }
        else
        {
        	deleteFav(extras.getString(CITY_SELECTED));//Supprimée de la base de données
        	favButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fav_btn_off, 0, 0, 0);        	
        }
		
	}
	
    /*
     * Méthode privée de type void
     * Elle permet de démarrer une connexion avec la base de données de l'application
     * Si elle n'existe pas, elle sera créée
     */
	private void initDB() 
	{
		DBClass dbHelper = new DBClass(getApplicationContext(), DBClass.DB_NAME, null, DBClass.DB_VERSION);	//Déclaraiton d'un objet DBClass
	
		try 
		{
			db = dbHelper.getWritableDatabase(); //Récupération de la base de données en lecture et écriture
		}
		catch (SQLiteException e) 
		{
			db = openOrCreateDatabase(DBClass.DB_NAME, MODE_PRIVATE, null); //Ouverture de connexion ou création de la base de données
		}
	}
	
	/*
	 * Méthode privée de type void
	 * Elle permet l'ajout de favoris
	 */
	private void insertFav(String city, String cp) 
	{
		if (db != null) //Vérifie la connexion à la base de données
		{
			//Chargement d'un objet ContentValues pour la requête d'insertion
			ContentValues values = new ContentValues();
			values.put(DBClass.CITY_COLUMN, city);
			values.put(DBClass.CP_COLUMN, cp);

			db.insert(DBClass.DB_TABLE, null, values); //Ajout du favoris dont la ville est en paramètre de l'objet ContentValues
			Toast.makeText(this, "Ajouter aux favoris: "+city, Toast.LENGTH_SHORT).show(); //Affichage d'un message de confirmation d'ajout en base
		}
		else
		{
			Toast.makeText(this, "Erreur BDD : non-connectée", Toast.LENGTH_SHORT).show(); //Affichage d'un message d'erreur
		}
	}
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 * Méthode protégée de type void
	 * Elle est appellée dès la destruction de l'Activity TodayOnglet
	 */
	protected void onDestroy() 
	{
		super.onDestroy();
		db.close(); //Fermeture de la connexion à la base de données
	}
	
	/*
	 * Méthode privée de type boolean
	 * Elle permet de vérifier si la ville courante est parmis les favoris.
	 * Retourne vrai si favoris, sinon false
	 */
	private boolean checkFav(String currentCity) 
	{		
		boolean isFav=false;
		if (db != null) //Vérifie la connexion à la base de données
		{	
			/*
			 * Création d'un curseur pour l'exécution d'instructions SQL
			 * Ici : Sélection des villes dont le nom exact de la ville est mentionné en paramètre
			 */
			Cursor c = db.rawQuery("SELECT cityName FROM favoris WHERE cityName = '"+currentCity+"'", null);			
			isFav=c.moveToFirst();//Vérifie si un résultat existe en première position du curseur, si oui retourne true, sinon false			
		}
		return isFav;
	}
	
	/*
	 * Méthode privée de type void
	 * Elle permet de supprimée un favoris dont le nom de la ville est en paramètre
	 */
	private void deleteFav(String currentCity) 
	{
		if (db != null) //Vérifie la connexion à la base de données
		{
			int countRows = db.delete(DBClass.DB_TABLE, "cityName = ?", new String[] {currentCity}); //Suppression de la ville dont le nom exacte est en paramètre
			Log.v("Nb ligne supprimé : ", Integer.toString(countRows));
			Toast.makeText(this, "Favoris supprimée : "+currentCity, Toast.LENGTH_SHORT).show(); //Affichage d'un message de confirmation de suppression du favoris
		}
		else
		{
			Toast.makeText(this, "Erreur BDD : non-connectée", Toast.LENGTH_SHORT).show(); //Affichage d'un message d'erreur
		}
	}
	
	public void displayToday()
	{
				
		SimpleDateFormat formater = null;
		Date aujourdhui = new Date();
		formater = new SimpleDateFormat("EEEE d MMM yyyy", Locale.FRANCE);
		String dayOfWeek = formater.format(aujourdhui); //Date du jour française
		Log.v("date", dayOfWeek);
	
		//Url permettant d'obtenir l'ensemble des informations de la météo courante
		String cityUrl = "http://api.openweathermap.org/data/2.5/weather?q="+extras.getString(CITY_SELECTED)+"&mode=json&units=metric&lang=fr";
		Log.v("Activity today : url", cityUrl);

	 	jParser = new JSONParser();
	 
		JSONObject json = jParser.getJSONFromUrl(cityUrl);//Récupération d'un String JSON au travers d'un URL
		JSONArray arrayWeather = null;
		JSONObject objectWeather=null;
		String weatherOfDay=null, iconWeatherOfDay = null;
		long tempOfDay;
		int roundTempOfDay=0;		
		
		try 
		{			
	        arrayWeather = json.getJSONArray("weather"); //Récupération d'un tableau JSONArray contenant des informations relatives à la météo		     
	        String weather = new String(arrayWeather.getJSONObject(0).getString("description").getBytes("ISO-8859-1"), "UTF-8"); //Récupération 
	        weatherOfDay = Html.fromHtml(weather).toString(); //Météo actuelle
	        
        	iconWeatherOfDay = arrayWeather.getJSONObject(0).getString("icon"); //Nom de l'image représentant la météo
        	//Log.v("Icon", iconWeatherOfDay);
        	
        	objectWeather = json.getJSONObject("main"); //Récupération d'un objet JSONObject contenant des informations sur la temperature
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
		
		//Affichage des variables récupérées dans les TextViews
		currentDate.setText(dayOfWeek);
		currentHour.setText(hourOfDay);
		currentWeather.setText(weatherOfDay);
		currentTemp.setText(Integer.toString(roundTempOfDay)+" C°");		
				
		String iconUrl = "http://openweathermap.org/img/w/"+iconWeatherOfDay+".png";//URL permettant de récupérée l'image de la météo actuelle
		Log.v("TodayOnglet", iconUrl);
		
		ImageView meteoView = (ImageView)findViewById(R.id.meteoView);
		URL myFileUrl;
		InputStream is = null;
		try {
			myFileUrl = new URL (iconUrl);
		    HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
		    conn.setDoInput(true);
		    conn.connect();		 
		    is = conn.getInputStream();//Récupération de l'image
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();			
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		meteoView.setImageBitmap(BitmapFactory.decodeStream(is));//Affichage de l'image téléchargée
		
		//Sauvegarde en cache
		editor.putString("currentDate", dayOfWeek);
		editor.putString("currentHour", hourOfDay);
		editor.putString("currentWeather", weatherOfDay);
		editor.putString("currentTemp", Integer.toString(roundTempOfDay)+" C°");
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
		    is = conn.getInputStream();//Récupération de l'image
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();			
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		meteoView.setImageBitmap(BitmapFactory.decodeStream(is));//Affichage de l'image téléchargée
	}
	
}
