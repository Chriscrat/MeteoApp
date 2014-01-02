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
 * Cette Activity est appell�e par l'Activity HomePage dans un onglet
 */
public class FavOnglet extends Activity 
{
	//Tags permettant de r�cuperer des variables transmis au travers d'objets Intent
	final String CITY_SELECTED ="a_city";
	final String CP_SELECTED = "a_cp";
	
	SQLiteDatabase db;
	
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
        setContentView(R.layout.fav_onglet);        
        
        initDB();//Connexion � la base de donn�es si existante, sinon la cr�er puis se connecte
        
        ArrayList<HashMap<String, String>> favList = new ArrayList<HashMap<String, String>>();
        favList = getFavorisList(); //R�cuperation de l'ensemble des favoris dans la BDD    
        Log.v("Nombre de favoris", Integer.toString(favList.size()));        

    	final ListView listFavoris = (ListView)findViewById(R.id.favListView);	
        SimpleAdapter adaptater = new SimpleAdapter (this.getBaseContext(), favList, R.layout.city_item, new String[] {"cityName", "cityPostalCode"}, new int[] {R.id.cityName, R.id.cityPostalCode});//Passage de l'ArrayList des villes favoris � l'adapter 
        listFavoris.setAdapter(adaptater);//Ajout de l'adapter � la listView des favoris

		//D�claration d'un listener pour un �v�nement de type click sur un item de la ListView
        listFavoris.setOnItemClickListener(new OnItemClickListener() {
			@Override
        	@SuppressWarnings("unchecked")
			/*
			 * (non-Javadoc)
			 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
			 * M�thode publique de type void
			 * Elle sera invoqu�e d�s l'�mission d'un clic sur un �l�ment du composant ListView des favoris
			 */
         	public void onItemClick(AdapterView<?> a, View v, int position, long id) 
			{
        		HashMap<String, String> map = (HashMap<String, String>) listFavoris.getItemAtPosition(position); //R�cuperation d'un HashMap � la position du clic
        		
        		Intent intent = new Intent(FavOnglet.this, MeteoPage.class); 
        		Bundle extras = new Bundle();        		
        		
        		String city = map.get("cityName");
        		String cp = map.get("cityPostalCode");
        		
        		//Pr�paration des variables � passer de l'Activity FavOnglet � MeteoPage
        		extras.putString(CITY_SELECTED, city);
        		extras.putString(CP_SELECTED, cp);
        		intent.putExtras(extras);
        		
        		startActivity(intent);//Appel de l'Activity MeteoPage
        	}
         });
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
	 * M�thode publique de type ArrayList<HashMap<String, String>>
	 * Elle permet de r�cup�rer l'ensemble des favoris existant dans la base de donn�es
	 */
	public ArrayList<HashMap<String, String>> getFavorisList()
	{
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		if (db != null) //Test de la connexion � la base de donn�es
		{	
			/*
			 * Cr�ation d'un curseur permettant d'�xecuter des instructions SQL
			 * Ici : L'ensemble des villes et code postal tri�s par ordre alphab�tique des villes
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