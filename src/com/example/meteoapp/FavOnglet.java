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

/*
 * Classe permettant de construire l'Activity FavOnglet
 * Elle permet d'afficher sous forme de liste les villes favories
 * Cette Activity est appellée par l'Activity HomePage dans un onglet
 */
public class FavOnglet extends Activity 
{
	//Tags permettant de récuperer des variables transmis au travers d'objets Intent
	final String CITY_SELECTED ="a_city";
	final String CP_SELECTED = "a_cp";
	
	SQLiteDatabase db;
	
    @Override
    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     * Méthode publique de type void
     * Elle est invoquée à la constuction de l'Activity
     */
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fav_onglet);        
        
        initDB();//Connexion à la base de données si existante, sinon la créer puis se connecte
        
        ArrayList<HashMap<String, String>> favList = new ArrayList<HashMap<String, String>>();
        favList = getFavorisList(); //Récuperation de l'ensemble des favoris dans la BDD    
        Log.v("Nombre de favoris", Integer.toString(favList.size()));        

    	final ListView listFavoris = (ListView)findViewById(R.id.favListView);	
        SimpleAdapter adaptater = new SimpleAdapter (this.getBaseContext(), favList, R.layout.city_item, new String[] {"cityName", "cityPostalCode"}, new int[] {R.id.cityName, R.id.cityPostalCode});//Passage de l'ArrayList des villes favoris à l'adapter 
        listFavoris.setAdapter(adaptater);//Ajout de l'adapter à la listView des favoris

		//Déclaration d'un listener pour un évènement de type click sur un item de la ListView
        listFavoris.setOnItemClickListener(new OnItemClickListener() {
			@Override
        	@SuppressWarnings("unchecked")
			/*
			 * (non-Javadoc)
			 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
			 * Méthode publique de type void
			 * Elle sera invoquée dés l'émission d'un clic sur un élément du composant ListView des favoris
			 */
         	public void onItemClick(AdapterView<?> a, View v, int position, long id) 
			{
        		HashMap<String, String> map = (HashMap<String, String>) listFavoris.getItemAtPosition(position); //Récuperation d'un HashMap à la position du clic
        		
        		Intent intent = new Intent(FavOnglet.this, MeteoPage.class); 
        		Bundle extras = new Bundle();        		
        		
        		String city = map.get("cityName");
        		String cp = map.get("cityPostalCode");
        		
        		//Préparation des variables à passer de l'Activity FavOnglet à MeteoPage
        		extras.putString(CITY_SELECTED, city);
        		extras.putString(CP_SELECTED, cp);
        		intent.putExtras(extras);
        		
        		startActivity(intent);//Appel de l'Activity MeteoPage
        	}
         });
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
	 * Méthode publique de type ArrayList<HashMap<String, String>>
	 * Elle permet de récupérer l'ensemble des favoris existant dans la base de données
	 */
	public ArrayList<HashMap<String, String>> getFavorisList()
	{
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		if (db != null) //Test de la connexion à la base de données
		{	
			/*
			 * Création d'un curseur permettant d'éxecuter des instructions SQL
			 * Ici : L'ensemble des villes et code postal triés par ordre alphabétique des villes
			 */
			Cursor c = db.rawQuery("SELECT cityName, cityPostalCode FROM favoris ORDER BY cityName ASC", null);
			HashMap<String, String> item;
			Log.v("Nombre de favoris", Integer.toString(c.getCount())); //Comptabilisation des favoris
			while(c.moveToNext()) //Tant que le curseur est non-vide
			{
				item = new HashMap<String, String>();
				item.put("cityName", c.getString(0));
				item.put("cityPostalCode", c.getString(1));
				list.add(item);	//Chargement d'un ArrayList contenant des HashMaps
			}
		}	
		return list;
	}
}