package com.example.meteoapp;
 
import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
 
public class FavorisDB {
 
	private static final int VERSION_BDD = 1;
	private static final String NOM_BDD = "favoris.db"; 
	private static final String TABLE_FAV = "favoris";
	private static final String COL_ID = "Id";
	//private static final int NUM_COL_ID = 0;
	private static final String COL_CITY = "cityName";
	private static final String COL_CP = "postalCode";
	private static final int NUM_COL_CITY = 1;
	private static final int NUM_COL_CP = 2;
 
	private static SQLiteDatabase bdd;
 
	private DBClass maBaseSQLite;
 
	public FavorisDB(Context todayOnglet)
	{
		//On crée la BDD et sa table
		maBaseSQLite = new DBClass(todayOnglet, NOM_BDD, null, VERSION_BDD);
		Log.v("DB","DB créée");
	}
 
	public void open(){
		//on ouvre la BDD en écriture
		bdd = maBaseSQLite.getWritableDatabase();
		Log.v("DB", "Connecté");
	}
 
	public void close(){
		//on ferme l'accès à la BDD
		bdd.close();
	}
 
	public SQLiteDatabase getBDD(){
		return bdd;
	}
 
	public long insertFav(Favoris fav){
		//Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		//on lui ajoute une valeur associée à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
		values.put(COL_CITY, fav.getCity());
		values.put(COL_CP, fav.getCp());
		return bdd.insert(TABLE_FAV, null, values);		
	}
 
	public int updateFav(int id, Favoris fav){
		//La mise à jour d'un favoris dans la BDD fonctionne plus ou moins comme une insertion
		//il faut simplement préciser quel favoris on doit mettre à jour grâce à l'ID
		ContentValues values = new ContentValues();
		values.put(COL_CITY, fav.getCity());
		return bdd.update(TABLE_FAV, values, COL_ID + " = " +id, null);
	}
 
	public int removeFavWithID(int id){
		//Suppression d'un favoris de la BDD grâce à l'ID
		return bdd.delete(TABLE_FAV, COL_ID + " = " +id, null);
	}
 
	public static Favoris getFavWithCityName(String city){
		//Récupère dans un Cursor les valeurs correspondant à un livre contenu dans la BDD (ici on sélectionne le favoris grâce à son titre)
		Cursor c = bdd.query(TABLE_FAV, new String[] {COL_ID, COL_CITY}, COL_CITY + " LIKE \"" + city +"\"", null, null, null, null);
		return cursorToFav(c);
	}
 
	//Cette méthode permet de convertir un cursor en un favoris
	private static Favoris cursorToFav(Cursor c)
	{
		//si aucun élément n'a été retourné dans la requête, on renvoie null
		if (c.getCount() == 0)
			return null;
 
		//Sinon on se place sur le premier élément
		c.moveToFirst();
		//On créé un favoris
		Favoris fav = new Favoris();
		//on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
		//fav.setId(c.getInt(NUM_COL_ID));
		fav.setCity(c.getString(NUM_COL_CITY));
		//On ferme le cursor
		c.close();
 
		//On retourne le favoris
		return fav;
	}
	
	public ArrayList<HashMap<String, String>> getFavCityList()
	{
		ArrayList<HashMap<String, String>> favList=null;
		HashMap<String, String> favItem;
		Log.v("Test select * ", "Requête ok !");
		String[] tableColumns = new String[] {COL_CITY, "SELECT "+COL_CITY+" FROM "+TABLE_FAV};
		Cursor c = bdd.query("table1", tableColumns, null, null, null, null, null);
		if (c.moveToFirst()) 
		{
			Log.v("Result", "ok");
			favList = new ArrayList<HashMap<String, String>>();
            while (c.isAfterLast() == false) 
            {
            	Log.v("Value","Trouvée");
            	favItem = new HashMap<String, String>();
                String city = c.getString(c.getColumnIndex(COL_CITY));
                String cp = c.getString(c.getColumnIndex(COL_CP));
                favItem.put("cityName",city);
                favItem.put("cp",cp);
                favList.add(favItem);
                c.moveToNext();
            }
        }
		
		return favList;
	}
}