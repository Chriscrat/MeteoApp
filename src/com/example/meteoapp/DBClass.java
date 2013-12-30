package com.example.meteoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBClass extends SQLiteOpenHelper 
{
	  public static final String DB_NAME = "METEO_APP";
	  public static final int DB_VERSION = 1;
	  public static final String DB_TABLE = "favoris";
	  public static final String CITY_COLUMN = "cityName";

	  private static final String KEY = "Id";
	  private static final String REQ_TABLE_CREATE =  "CREATE TABLE "+DB_TABLE+"(" + KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CITY_COLUMN + " TEXT); ";


  public DBClass(Context todayOnglet, String name, CursorFactory factory,int version) 
  {
		super((Context) todayOnglet, name, factory, version);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(REQ_TABLE_CREATE);
  }

  //private static final String REQ_TABLE_DROP = "DROP TABLE IF EXISTS favoris;";

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }
}