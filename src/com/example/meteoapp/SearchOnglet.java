package com.example.meteoapp;

import java.util.ArrayList;
import java.util.HashMap;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

/*
 * Classe permettant de construire l'Activity SearchOnglet
 * Elle permet d'afficher sous forme d'une liste déroulante, l'ensemble des villes dont la météo est consultable
 * Cette Activity est appellée par l'Activity HomePage dans un onglet
 */

@SuppressLint("DefaultLocale")
public class SearchOnglet extends Activity 
{
    /** Called when the activity is first created. */
	private ListView cityListView;
	private ArrayList<HashMap<String, String>> cityList;
	
	//Tags permettant de récuperer des variables transmis au travers d'objets Intent
    final String CITY_SELECTED = "a_city";
    final String CP_SELECTED = "a_cp";
    
    @SuppressLint("DefaultLocale")
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_onglet);
        
        //Création d'un ArrayList contenant l'ensemble des villes
        cityList = new ArrayList<HashMap<String, String>>();
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
        cityItem.put("cityName", "Bretigny");
        cityItem.put("cityPostalCode", "91103");
        cityList.add(cityItem);
        
        cityItem = new HashMap<String, String>();
        cityItem.put("cityName", "Creteil");
        cityItem.put("cityPostalCode", "94010");
        cityList.add(cityItem);
        
        cityItem = new HashMap<String, String>();
        cityItem.put("cityName", "Cergy-Pontoise");
        cityItem.put("cityPostalCode", "95027");
        cityList.add(cityItem);
           
        cityItem = new HashMap<String, String>();
        cityItem.put("cityName", "Draveil");
        cityItem.put("cityPostalCode", "91201");
        cityList.add(cityItem);
        
        cityItem = new HashMap<String, String>();
        cityItem.put("cityName", "Evry");
        cityItem.put("cityPostalCode", "91000");
        cityList.add(cityItem);
        
        cityItem = new HashMap<String, String>();
        cityItem.put("cityName", "Fontainebleau");
        cityItem.put("cityPostalCode", "77300");
        cityList.add(cityItem);
       
        cityItem = new HashMap<String, String>();
        cityItem.put("cityName", "Grigny");
        cityItem.put("cityPostalCode", "91350");
        cityList.add(cityItem);
        
        cityItem = new HashMap<String, String>();
        cityItem.put("cityName", "Ivry-sur-Seine");
        cityItem.put("cityPostalCode", "94205");
        cityList.add(cityItem);
        
        cityItem = new HashMap<String, String>();
        cityItem.put("cityName", "Lardy");
        cityItem.put("cityPostalCode", "91510");
        cityList.add(cityItem);
        
        cityItem = new HashMap<String, String>();
        cityItem.put("cityName", "Massy-palaiseau");
        cityItem.put("cityPostalCode", "91300");
        cityList.add(cityItem);
        
        cityItem = new HashMap<String, String>();
        cityItem.put("cityName", "Marne-la-Vallée");
        cityItem.put("cityPostalCode", "77448");
        cityList.add(cityItem);
        
        cityItem = new HashMap<String, String>();
        cityItem.put("cityName", "Noisiel");
        cityItem.put("cityPostalCode", "77186");
        cityList.add(cityItem);
        
        cityItem = new HashMap<String, String>();
        cityItem.put("cityName", "Nanterre");
        cityItem.put("cityPostalCode", "92000");
        cityList.add(cityItem);
        
        cityItem = new HashMap<String, String>();
        cityItem.put("cityName", "Neuilly Plaisance");
        cityItem.put("cityPostalCode", "91000");
        cityList.add(cityItem);
        
        cityItem = new HashMap<String, String>();
        cityItem.put("cityName", "Aubervilliers");
        cityItem.put("cityPostalCode", "93300");
        cityList.add(cityItem);
        
        cityItem = new HashMap<String, String>();
        cityItem.put("cityName", "Mennecy");
        cityItem.put("cityPostalCode", "91540");
        cityList.add(cityItem);
        
        cityItem = new HashMap<String, String>();
        cityItem.put("cityName", "Melun");
        cityItem.put("cityPostalCode", "77000");
        cityList.add(cityItem);
        
        cityItem = new HashMap<String, String>();
        cityItem.put("cityName", "Malesherbes");
        cityItem.put("cityPostalCode", "45330");
        cityList.add(cityItem);
        
        cityListView = (ListView) findViewById(R.id.cityListView); 
        
        final SimpleAdapter adapter = new SimpleAdapter (this.getBaseContext(), cityList, R.layout.city_item, new String[] {"cityName", "cityPostalCode"}, new int[] {R.id.cityName, R.id.cityPostalCode});//Passage de l'ArrayList des villes favoris à l'adapter 
        cityListView.setAdapter(adapter);//Ajout de l'adapter à la listView des villes

		//Déclaration d'un listener pour un évènement de type click sur un item de la ListView
        cityListView.setOnItemClickListener(new OnItemClickListener() 
        {
			@Override
        	@SuppressWarnings("unchecked")
			/*
			 * (non-Javadoc)
			 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
			 * Méthode publique de type void
			 * Elle sera invoquée dés l'émission d'un clic sur un élément du composant ListView des villes
			 */
         	public void onItemClick(AdapterView<?> a, View v, int position, long id) 
			{
        		HashMap<String, String> map = (HashMap<String, String>) cityListView.getItemAtPosition(position); //Récuperation d'un HashMap à la position du clic
        		
        		Intent intent = new Intent(SearchOnglet.this, MeteoPage.class);
        		Bundle extras = new Bundle();
        		
        		String city = map.get("cityName");
        		String cp = map.get("cityPostalCode");
        		
        		//Préparation des variables à passer de l'Activity SearchOnglet à MeteoPage
        		extras.putString(CITY_SELECTED, city);
        		extras.putString(CP_SELECTED, cp);
        		intent.putExtras(extras);
        		
        		startActivity(intent);//Appel de l'Activity MeteoPage
        	}
         });
        
        EditText searchText = (EditText)findViewById(R.id.searchInput);
        
        //Déclaration d'un listener pour un évènement de type modification du texte
        searchText.addTextChangedListener(new TextWatcher() 
        {
			@SuppressLint("DefaultLocale")
			@Override
			/*
			 * (non-Javadoc)
			 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
			 * Méthode publique de type void
			 * Elle sera invoquée après que l'utilisateur modifiera la valeur de l'EditText
			 */
			public void afterTextChanged(Editable arg0) 
			{
				EditText searchText = (EditText)findViewById(R.id.searchInput);
				String charSequence = searchText.getText().toString();
				adapter.notifyDataSetChanged();
				if(charSequence!=null)				
					adapter.getFilter().filter(charSequence);
									
			}

			@Override
			/*
			 * (non-Javadoc)
			 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
			 * Méthode publique de type void
			 * Elle sera invoquée avant que l'utilisateur modifiera la valeur de l'EditText
			 */
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) 
			{
				
			}

			@Override
			/*
			 * (non-Javadoc)
			 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
			 * Méthode publique de type void
			 * Elle sera invoquée dés lors que la valeur de l'EditText est modifiée
			 */
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				
			}
        });
        }
}
