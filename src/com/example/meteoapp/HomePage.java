package com.example.meteoapp;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

/*
 * Classe permettant de construire l'Activity HomePage
 * Elle permet d'afficher l'�cran d'accueil
 * Cette Activity dispose de deux onglets dont sont contenus les Activitys SearchOnglet(affich�e par d�faut) et FavOnglet
 */
@SuppressWarnings("deprecation")
public class HomePage extends TabActivity //H�ritage permettant l'utilisation des TabHosts pour la cr�ation d'onglet
{
	@Override
	/*
	 * (non-Javadoc)
	 * @see android.app.ActivityGroup#onCreate(android.os.Bundle)
	 * M�thode prot�g�e de type void
	 * Elle est invoqu�e � la constuction de l'Activity
	 */
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homepage);
        				
		Resources res = getResources(); //D�claration d'un objet permettant de r�cuperer l'ensemble des ressources du projet (images, sons, etc ..)
		final TabHost tabHost = getTabHost();
		
		/*
		 * Cr�ation des onglets de l'Activity HomePage
		 * 1er onglet : Recherche
		 */
		
		TabHost.TabSpec spec;
		Intent intent = new Intent().setClass(this, SearchOnglet.class);		
		spec = tabHost.newTabSpec("Widget").setIndicator("Recherche", res.getDrawable(android.R.drawable.ic_search_category_default)).setContent(intent); //Ajout de l'Activity SearchOnglet dans un premier onglet
		tabHost.addTab(spec);
		
		/*
		 * 2nd onglet : Favoris
		 */
		intent = new Intent().setClass(this, FavOnglet.class);
		spec = tabHost.newTabSpec("Form").setIndicator("Favoris", res.getDrawable(android.R.drawable.star_big_on)).setContent(intent); //Ajout de l'Activity FavOnglet dans un second onglet
		tabHost.addTab(spec);				
		tabHost.setCurrentTab(0); //Focus sur le premier onglet Recherche
	
		//Code pour le design des onglets		 
	    for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
		{
		   tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#dedede")); //Code couleur pour l'onglet non-s�lectionn�
		}
        tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#78c8e3")); //Code couleur pour l'onglet s�lectionn�
        
        //D�claration d'un listener pour un �v�nement de type changement d'onglet
        tabHost.setOnTabChangedListener((new OnTabChangeListener()
        {
        	/*
        	 * (non-Javadoc)
        	 * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
        	 * M�thode publique de type void
        	 * Elle sera invoqu�e lors d'un changement d'onglet
        	 */
			public void onTabChanged(String tabId) 
			{
			     for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
			        {
			           tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#dedede")); //Code couleur pour l'onglet non-s�lectionn�
			        }
			        tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#78c8e3")); //Code couleur pour l'onglet s�lectionn�
			}
		}));
	}
}
