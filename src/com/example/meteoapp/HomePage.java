package com.example.meteoapp;

import android.os.Bundle;
import android.app.TabActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

@SuppressWarnings("deprecation")
public class HomePage extends TabActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homepage);
		
		
		
		
		
		//Création d'une instance de ma classe FavorisDB
        //final FavorisDB favDB = new FavorisDB(this);
		
		Resources res = getResources();
		final TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent = new Intent().setClass(this, SearchOnglet.class);
		//intent.putExtra("favObjectDB", favDB);
		spec = tabHost.newTabSpec("Widget").setIndicator("Recherche", res.getDrawable(android.R.drawable.ic_search_category_default)).setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent().setClass(this, FavOnglet.class);
		//intent.putExtra("favObjectDB", favDB);
		spec = tabHost.newTabSpec("Form").setIndicator("Favoris", res.getDrawable(android.R.drawable.star_big_on)).setContent(intent);
		tabHost.addTab(spec);		
		tabHost.setCurrentTab(0);
		
	    for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
		{
		   tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#dedede")); //unselected
		}
        tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#78c8e3")); // selected
        
        tabHost.setOnTabChangedListener((new OnTabChangeListener(){
		public void onTabChanged(String tabId) {
		    // TODO Auto-generated method stub
		     for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
		        {
		           tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#dedede")); //unselected
		        }
		        tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#78c8e3")); // selected
		}
		}));
	}
}
