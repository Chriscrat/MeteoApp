package com.example.meteoapp;

import android.os.Bundle;
import android.app.TabActivity;
import android.util.Log;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

/*
 * Classe permettant de construire l'Activity MeteoPage, permettant d'afficher les informations météo
 * Cette Activity dispose de deux onglets dont sont contenus les Activitys TodayOnglet(affichée par défaut) et WeekOnglet
 */
@SuppressWarnings("deprecation")
public class MeteoPage extends TabActivity //Héritage permettant l'utilisation des TabHosts pour la création d'onglet
{
	//Tags permettant de récuperer des variables transmis au travers d'objets Intent
	final String CITY_SELECTED = "a_city";
	final String CP_SELECTED = "a_cp";
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see android.app.ActivityGroup#onCreate(android.os.Bundle)
	 * Méthode protégée de type void
	 * Elle est invoquée à la constuction de l'Activity
	 */
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meteo); 
				
		Intent intent = getIntent();
		Bundle extras = getIntent().getExtras();
		
		String city="", cp="";
		if (intent != null) 
		{
			//Récupération des variables issues de l'objet Intent
			city = extras.getString(CITY_SELECTED);
			cp = extras.getString(CP_SELECTED);
			Log.v("Activity meteo : ville : ", city);
		}	
		Resources res = getResources();	//Déclaration d'un objet pour récupérer l'ensemble des ressources du projets (images, sons, etc...)
		
		final TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		
		Intent intent2 = new Intent().setClass(this, TodayOnglet.class);
		extras.putString(CITY_SELECTED, city);
		extras.putString(CP_SELECTED, cp);
		intent2.putExtras(extras); //Passage de variables à l'Activity TodayOnglet
		
		/*
		 * 
		 * Création des onglets de l'Activity HomePage
		 * 1er onglet : Aujourd'hui
		 */
		 
		spec = tabHost.newTabSpec("Widget").setIndicator("Aujourd'hui", res.getDrawable(android.R.drawable.ic_menu_day)).setContent(intent2);
		tabHost.addTab(spec);
		
		intent2 = new Intent().setClass(this, WeekOnglet.class);
		intent2.putExtras(extras); //Passage de variables à l'Activity WeekOnglet
		
		/*
		 * 2nd onglet : Semaine
		 */
		spec = tabHost.newTabSpec("Form").setIndicator("Semaine", res.getDrawable(android.R.drawable.ic_menu_week)).setContent(intent2);
		tabHost.addTab(spec);
		
		tabHost.setCurrentTab(0); //Focus sur le premier onglet Aujourd'hui
		
		//Code pour le design des onglets		 
	    for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
		{
		   tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#dedede")); //Code couleur pour l'onglet non-sélectionné
		}
        tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#78c8e3")); //Code couleur pour l'onglet sélectionné
        
        //Déclaration d'un listener pour un évènement de type changement d'onglet
        tabHost.setOnTabChangedListener((new OnTabChangeListener()
        {
        	/*
        	 * (non-Javadoc)
        	 * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
        	 * Méthode publique de type void
        	 * Elle sera invoquée lors d'un changement d'onglet
        	 */
			public void onTabChanged(String tabId) 
			{
			     for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
			        {
			           tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#dedede")); //unselected
			        }
			        tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#78c8e3")); // selected
			}
		}));
	}
}
