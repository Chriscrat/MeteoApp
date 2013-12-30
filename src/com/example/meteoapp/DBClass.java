package com.example.meteoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBClass extends SQLiteOpenHelper 
{
	  private static final String KEY = "Id";
	  private static final String CITY = "cityName";    
	  private static final String REQ_TABLE_CREATE =  "CREATE TABLE favoris (" + KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CITY + " TEXT); ";


  public DBClass(Context todayOnglet, String name, CursorFactory factory,int version) 
  {
		super((Context) todayOnglet, name, factory, version);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(REQ_TABLE_CREATE);
  }

  public static final String REQ_TABLE_DROP = "DROP TABLE IF EXISTS favoris;";

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL(REQ_TABLE_DROP);
    onCreate(db);
  }
}