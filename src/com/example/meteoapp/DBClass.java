package com.example.meteoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * Classe permettant de créer une base de données constituées d'une table dans laquelle sera gérer des favoris
 */
public class DBClass extends SQLiteOpenHelper 
{
	  public static final String DB_NAME = "meteo_db";
	  public static final int DB_VERSION = 1;
	  public static final String DB_TABLE = "favoris";
	  public static final String CITY_COLUMN = "cityName";
	  public static final String CP_COLUMN = "cityPostalCode";

	  private static final String KEY = "Id";
	  private static final String REQ_TABLE_CREATE =  "CREATE TABLE "+DB_TABLE+"(" + KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CITY_COLUMN + " TEXT, "+CP_COLUMN+" TEXT); ";


	  /*
	   * Constructeur public
	   */
	  public DBClass(Context todayOnglet, String name, CursorFactory factory,int version) 
	  {
			super((Context) todayOnglet, name, factory, version);
	  }
	
	  @Override
	  /*
	   * (non-Javadoc)
	   * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	   * Méthode publique de type void
	   * Elle permet de créer la base de données
	   */
	  public void onCreate(SQLiteDatabase db) {
	    db.execSQL(REQ_TABLE_CREATE);
	  }
	  
	  @Override
	  /*
	   * (non-Javadoc)
	   * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	   * Méthode publique de type void
	   * Elle permet de mettre à jour la base de données créée
	   */
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	  {
	  }
  
}