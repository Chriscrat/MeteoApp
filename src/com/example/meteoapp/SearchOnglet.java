package com.example.meteoapp;

import java.util.ArrayList;
import java.util.HashMap;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;


/*
 * Classe permettant de construire l'Activity SearchOnglet
 * Elle permet d'afficher sous forme d'une liste d�roulante, l'ensemble des villes dont la m�t�o est consultable
 * Cette Activity est appell�e par l'Activity HomePage dans un onglet
 */

@SuppressLint({ "DefaultLocale", "HandlerLeak" })
public class SearchOnglet extends Activity implements LocationListener //Impl�mentation de LocationListener pour effectuer de la g�olocalisation
{
	private ListView cityListView;
	private ArrayList<HashMap<String, String>> cityList;
		
	public LocationManager lm;
    public Location loc;

	private double latitude;
	private double longitude;
	private double altitude;
	private float accuracy;
	
	protected ProgressDialog mProgressDialog;
	private Context mContext;
	 
	public static final int MSG_ERR = 0;
	public static final int MSG_CNF = 1;
	public static final int MSG_IND = 2;
	
	
	enum ErrorStatus
	{
		NO_ERROR, 
		ERROR_1, 
		ERROR_2
	};
	
	private ErrorStatus status;
	
	//Tags permettant de r�cuperer des variables transmis au travers d'objets Intent
    final String CITY_SELECTED = "a_city";
    final String CP_SELECTED = "a_cp";
    
    @SuppressLint("DefaultLocale")
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_onglet);
        
		mContext = this;

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
                compute();
        		
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
    	Log.v("onResume","ok");
    	super.onResume();
    	loc = getLocation();
    	HashMap<String,String> currentPos = new HashMap<String,String>();
    	currentPos.put("cityName", "Ma position");
    	currentPos.put("cityPostalCode", "test");
    	cityList.add(currentPos);
    	Log.v("lat", " :" +latitude);
    }

    /*
     * 
     */
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

    /*
     * 
     */
   	@Override
	/*
	 * (non-Javadoc)
	 * @see android.location.LocationListener#onLocationChanged(android.location.Location)
	 * Elle est appel�e quand la localisation de l�utilisateur est mise � jour
	 */
	public void onLocationChanged(Location location) 
	{
		Log.v("onLocationChanged"," ok");
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		altitude = location.getAltitude();
		accuracy = location.getAccuracy();

		String msg = String.format(getResources().getString(R.string.new_location), latitude, longitude, altitude, accuracy);
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

	}

   	/*
   	 * (non-Javadoc)
   	 * @see android.location.LocationListener#onProviderDisabled(java.lang.String)
   	 */
	@Override
	public void onProviderDisabled(String provider) 
	{
		Log.v("onProviderDisabled","Service(s) desactive(s)");
		String msg = String.format(getResources().getString(R.string.provider_disabled), provider);
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

	}

	/*
	 * (non-Javadoc)
	 * @see android.location.LocationListener#onProviderEnabled(java.lang.String)
	 */
	@Override
	public void onProviderEnabled(String provider) 
	{
		Log.v("onProviderEnabled","Service active");
		String msg = String.format(getResources().getString(R.string.provider_enabled), provider);
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	/*
	 * 
	 */
	@Override
	/*
	 * (non-Javadoc)
	 * @see android.location.LocationListener#onStatusChanged(java.lang.String, int, android.os.Bundle)
	 * M�thode publique de type void
	 * Elle est appell�e quand le status d�une source change
	 */
	public void onStatusChanged(String provider, int status, Bundle extras) 
	{
		Log.v("onStatusChanged", ":"+status);

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
	
	/*
	 * M�thode publique de type void
	 */
	public Location getLocation() 
	{
	    try 
	    {	    	
	    	lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	        // getting GPS status
	        boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

	        // getting network status
	        boolean isNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

	        if (isGPSEnabled && isNetworkEnabled) 
	        {
	        	onProviderEnabled("Internet et GPS");
	        }
	        else if(isGPSEnabled)
	        {
	        	onProviderEnabled("GPS");
	        }
	        else if(isNetworkEnabled)
	        {
	        	onProviderEnabled("Internet");
	        }       
	        	        
            if (isNetworkEnabled) 
            {
                Log.v("Network", "Internet OK");
                if (lm != null) 
                {
                	lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, this);
                    loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (loc != null) 
                    {
                    	Log.v("Location ",": non null");
                        latitude = loc.getLatitude();
                        longitude = loc.getLongitude();
                    }
                    else
                    {
                    	Log.v("Location ",": null");
                    }
                }
            }
            
            // if GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled) 
            {
                if (loc == null) 
                {                	
                    Log.v("GPS", "GPS ok");
                    if (lm != null) 
                    {
                    	lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, this);
                        loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc != null) 
                        {
	                    	Log.v("Location ",": non null");
                            latitude = loc.getLatitude();
                            longitude = loc.getLongitude();
                        }
	                    else
	                    {
	                    	Log.v("Location ",": null");
	                    }
                    }
                }
            }      
	    } 
	    catch (Exception e) 
	    {
	        e.printStackTrace();
	    }	    
	    return loc;
	}
	
	/*
	 * M�thode priv�e de type void
	 */
	private void compute() 
	{
		mProgressDialog = ProgressDialog.show(this, "Informations", "Traitements en cours ...", true);		
	    // useful code, variables declarations...
	    new Thread((new Runnable() 
	    {
	        @Override
	        public void run() 
	        {
	            Message msg = null;
	 
                String progressBarData = "Traitements en cours ...";
                mProgressDialog.setMessage(progressBarData);
 
                // populates the message
                msg = mHandler.obtainMessage(MSG_IND, (Object) progressBarData);
 
                // sends the message to our handler
                mHandler.sendMessage(msg);
                
                msg = mHandler.obtainMessage(MSG_CNF, "Chargement termin� !");
                
                // sends the message to our handler
                mHandler.sendMessage(msg);
	        }
	    })).start();
	}
	
	final Handler mHandler = new Handler() 
	{
		/*
		 * (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 * M�thode publique de type void
		 */
	    public void handleMessage(Message msg) {
	        String text2display = null;
	        switch (msg.what) 
	        {
		        case MSG_IND:
		            if (mProgressDialog.isShowing()) 
		            {
		                mProgressDialog.setMessage(((String) msg.obj));
		            }
		            break;
		            
		        case MSG_ERR:
		            text2display = (String) msg.obj;
		            Toast.makeText(mContext, "Error: " + text2display, Toast.LENGTH_LONG).show();
		            if (mProgressDialog.isShowing()) 
		            {
		                mProgressDialog.dismiss();
		            }
		            break;
		            
		        case MSG_CNF:
		            text2display = (String) msg.obj;
		            Toast.makeText(mContext, "Info: " + text2display, Toast.LENGTH_LONG).show();
		            if (mProgressDialog.isShowing()) 
		            {
		                mProgressDialog.dismiss();
		            }
		            break;
		            
		        default: // should never happen
		            break;
	        }
	    }
	};	
}
