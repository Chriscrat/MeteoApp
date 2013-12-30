package com.example.meteoapp;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
 
public class FavOnglet extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fav_onglet);
        FavorisDB fb =  new FavorisDB(this);
        
        ArrayList<HashMap<String,String>> favList = fb.getFavCityList();
        Log.v("Array", Integer.toString(favList.size()));
    }
}