package com.example.meteoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * Classe permettant de cr�er une base de donn�es constitu�es d'une table dans laquelle sera g�rer des favoris
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
	   * M�thode publique de type void
	   * Elle permet de cr�er la base de donn�es
	   */
	  public void onCreate(SQLiteDatabase db) {
	    db.execSQL(REQ_TABLE_CREATE);
	  }
	  
	  @Override
	  /*
	   * (non-Javadoc)
	   * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	   * M�thode publique de type void
	   * Elle permet de mettre � jour la base de donn�es cr��e
	   */
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	  {
	  }
  
}