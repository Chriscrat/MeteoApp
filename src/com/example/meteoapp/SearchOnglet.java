package com.example.meteoapp;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;


public class SearchOnglet extends Activity {
    /** Called when the activity is first created. */
	private ListView cityListView;
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_onglet);
        ArrayList<HashMap<String, String>> cityList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> cityItem;
        cityItem = new HashMap<String, String>();
        cityItem.put("cityName", "Athis-Mons");
        cityItem.put("cityPostalCode", "91200");
        cityList.add(cityItem);
 
        cityItem = new HashMap<String, String>();
        cityItem.put("cityName", "Ballancourt");
        cityItem.put("cityPostalCode", "91610");
        cityList.add(cityItem);
 
        cityItem = new HashMap<String, String>();
        cityItem.put("cityName", "Corbeil-Essonne");
        cityItem.put("cityPostalCode", "91200");
        cityList.add(cityItem);
 
        cityItem = new HashMap<String, String>();
        cityItem.put("cityName", "Evry");
        cityItem.put("cityPostalCode", "91000");
        cityList.add(cityItem);
        
        cityItem = new HashMap<String, String>();
        cityItem.put("cityName", "Athis-Mons");
        cityItem.put("cityPostalCode", "91200");
        cityList.add(cityItem);
 
        cityItem = new HashMap<String, String>();
        cityItem.put("cityName", "Ballancourt");
        cityItem.put("cityPostalCode", "91610");
        cityList.add(cityItem);
 
        cityItem = new HashMap<String, String>();
        cityItem.put("cityName", "Corbeil-Essonne");
        cityItem.put("cityPostalCode", "91200");
        cityList.add(cityItem);
 
        cityItem = new HashMap<String, String>();
        cityItem.put("cityName", "Evry");
        cityItem.put("cityPostalCode", "91000");
        cityList.add(cityItem);

        cityItem = new HashMap<String, String>();
        cityItem.put("cityName", "Athis-Mons");
        cityItem.put("cityPostalCode", "91200");
        cityList.add(cityItem);
 
        cityItem = new HashMap<String, String>();
        cityItem.put("cityName", "Ballancourt");
        cityItem.put("cityPostalCode", "91610");
        cityList.add(cityItem);
 
        cityItem = new HashMap<String, String>();
        cityItem.put("cityName", "Corbeil-Essonne");
        cityItem.put("cityPostalCode", "91200");
        cityList.add(cityItem);
 
        cityItem = new HashMap<String, String>();
        cityItem.put("cityName", "Evry");
        cityItem.put("cityPostalCode", "91000");
        cityList.add(cityItem);

        cityListView = (ListView) findViewById(R.id.cityListView); 
        SimpleAdapter adaptater = new SimpleAdapter (this.getBaseContext(), cityList, R.layout.city_item, new String[] {"cityName", "cityPostalCode"}, new int[] {R.id.cityName, R.id.cityPostalCode});
        cityListView.setAdapter(adaptater);
        CityList.getListViewSize(cityListView);
        
      //Enfin on met un �couteur d'�v�nement sur notre listView
        cityListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
        	@SuppressWarnings("unchecked")
         	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				//on r�cup�re la HashMap contenant les infos de notre item (titre, description, img)
        		HashMap<String, String> map = (HashMap<String, String>) cityListView.getItemAtPosition(position);
        		//on cr�er une boite de dialogue
        		AlertDialog.Builder adb = new AlertDialog.Builder(SearchOnglet.this);
        		//on attribut un titre � notre boite de dialogue
        		adb.setTitle("S�lection Item");
        		//on ins�re un message � notre boite de dialogue, et ici on affiche le titre de l'item cliqu�
        		adb.setMessage("Votre choix : "+map.get("cityName"));
        		//on indique que l'on veut le bouton ok � notre boite de dialogue
        		adb.setPositiveButton("Ok", null);
        		//on affiche la boite de dialogue
        		adb.show();
        	}
         });
     }
}
