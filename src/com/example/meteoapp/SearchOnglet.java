package com.example.meteoapp;

import java.util.ArrayList;
import java.util.HashMap;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

/*
 * Classe permettant de construire l'Activity SearchOnglet
 * Elle permet d'afficher sous forme d'une liste d�roulante, l'ensemble des villes dont la m�t�o est consultable
 * Cette Activity est appell�e par l'Activity HomePage dans un onglet
 */

@SuppressLint("DefaultLocale")
public class SearchOnglet extends Activity implements LocationListener //Impl�mentation de LocationListener pour effectuer de la g�olocalisation
{
	private ListView cityListView;
	private ArrayList<HashMap<String, String>> cityList;
	
	private LocationManager lm;
    private Location loc;

	private double latitude;
	private double longitude;
	private double altitude;
	private float accuracy;
	
	//Tags permettant de r�cuperer des variables transmis au travers d'objets Intent
    final String CITY_SELECTED = "a_city";
    final String CP_SELECTED = "a_cp";
    
    @SuppressLint("DefaultLocale")
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_onglet);
        
        //Cr�ation d'un ArrayList contenant l'ensemble des villes
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
        cityItem.put("cityName", "Marne-la-Vall�e");
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
        
        final SimpleAdapter adapter = new SimpleAdapter (this.getBaseContext(), cityList, R.layout.city_item, new String[] {"cityName", "cityPostalCode"}, new int[] {R.id.cityName, R.id.cityPostalCode});//Passage de l'ArrayList des villes favoris � l'adapter 
        cityListView.setAdapter(adapter);//Ajout de l'adapter � la listView des villes

		//D�claration d'un listener pour un �v�nement de type click sur un item de la ListView
        cityListView.setOnItemClickListener(new OnItemClickListener() 
        {
			@Override
        	@SuppressWarnings("unchecked")
			/*
			 * (non-Javadoc)
			 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
			 * M�thode publique de type void
			 * Elle sera invoqu�e d�s l'�mission d'un clic sur un �l�ment du composant ListView des villes
			 */
         	public void onItemClick(AdapterView<?> a, View v, int position, long id) 
			{
        		HashMap<String, String> map = (HashMap<String, String>) cityListView.getItemAtPosition(position); //R�cuperation d'un HashMap � la position du clic
        		
        		Intent intent = new Intent(SearchOnglet.this, MeteoPage.class);
        		Bundle extras = new Bundle();
        		
        		String city = map.get("cityName");
        		String cp = map.get("cityPostalCode");
        		
        		//Pr�paration des variables � passer de l'Activity SearchOnglet � MeteoPage
        		extras.putString(CITY_SELECTED, city);
        		extras.putString(CP_SELECTED, cp);
        		intent.putExtras(extras);
        		
        		startActivity(intent);//Appel de l'Activity MeteoPage
        	}
         });
        EditText searchText = (EditText)findViewById(R.id.searchInput);
        
        //D�claration d'un listener pour un �v�nement de type modification du texte
        searchText.addTextChangedListener(new TextWatcher() 
        {
			@SuppressLint("DefaultLocale")
			@Override
			/*
			 * (non-Javadoc)
			 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
			 * M�thode publique de type void
			 * Elle sera invoqu�e apr�s que l'utilisateur modifiera la valeur de l'EditText
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
			 * M�thode publique de type void
			 * Elle sera invoqu�e avant que l'utilisateur modifiera la valeur de l'EditText
			 */
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) 
			{
				
			}

			@Override
			/*
			 * (non-Javadoc)
			 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
			 * M�thode publique de type void
			 * Elle sera invoqu�e d�s lors que la valeur de l'EditText est modifi�e
			 */
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				
			}
        });
        
    	lm = (LocationManager) this.getSystemService(LOCATION_SERVICE); //Permet la cr�ation d'un abonnement pour la g�olocalisation
    }
    
    @Override
    /*
     * (non-Javadoc)
     * @see android.app.Activity#onResume()
     * M�thode prot�g�e de type void
     * Elle est appell�e une fois que la page est construire puis lanc�e et permet de d�finir des actions d�s que l'Activity a le focus
     */
    protected void onResume() 
    {
    	super.onResume();
    	if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
    	{
    		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this); //R�cup�ration de donn�es GPS dans un intervalle donn�
    		Log.v("GPS", "D�marr�");
    	//	Log.v("Latitude", ":"+loc.getAltitude());
    	}
    }

    @Override
    /*
     * (non-Javadoc)
     * @see android.app.Activity#onPause()
     * M�thode prot�g�e de type void
     * Elle permet de mettre en pause l'Activity pour laisser la main � une autre Activity
     */
    protected void onPause() 
    {
    	super.onPause();
    	lm.removeUpdates(this); //Permet le d�sabonnement afin de lib�rer les ressources monopolis�es par la g�olocalisation
    }

	@Override
	/*
	 * (non-Javadoc)
	 * @see android.location.LocationListener#onLocationChanged(android.location.Location)
	 * Elle est appel�e quand la localisation de l�utilisateur est mise � jour
	 */
	public void onLocationChanged(Location arg0) 
	{
		loc = arg0;
		latitude = loc.getLatitude();
		longitude = loc.getLongitude();
		altitude = loc.getAltitude();
		accuracy = loc.getAccuracy();
		String msg = String.format(getResources().getString(R.string.new_location), latitude, longitude, altitude, accuracy);
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();		
	}

	@Override
	public void onProviderDisabled(String provider) 
	{
		String msg = String.format(getResources().getString(R.string.provider_disabled), provider);
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String provider) 
	{
		String msg = String.format(getResources().getString(R.string.provider_enabled), provider);
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		Log.v("ok","ok");
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see android.location.LocationListener#onStatusChanged(java.lang.String, int, android.os.Bundle)
	 * M�thode publique de type void
	 * Elle est appell�e quand le status d�une source change
	 */
	public void onStatusChanged(String provider, int status, Bundle extras) {
		String newStatus = "";
		switch (status) 
		{
			case LocationProvider.OUT_OF_SERVICE:
				newStatus = "OUT_OF_SERVICE";
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				newStatus = "TEMPORARILY_UNAVAILABLE";
				break;
			case LocationProvider.AVAILABLE:
				newStatus = "AVAILABLE";
				break;
		}
		String msg = String.format(getResources().getString(R.string.provider_disabled), provider, newStatus);
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
}
