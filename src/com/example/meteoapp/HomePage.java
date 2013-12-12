package com.example.meteoapp;

import android.os.Bundle;
import android.app.TabActivity;
import android.view.Menu;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class HomePage extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homepage);
		Resources res = getResources();
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;
		
		intent = new Intent().setClass(this, SearchOnglet.class);
		spec = tabHost.newTabSpec("Widget").setIndicator("Recherche", res.getDrawable(android.R.drawable.ic_search_category_default
)).setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent().setClass(this, FavOnglet.class);
		spec = tabHost.newTabSpec("Form").setIndicator("Favoris", res.getDrawable(android.R.drawable.star_big_on)).setContent(intent);
		tabHost.addTab(spec);
		
		tabHost.setCurrentTab(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_city, menu);
		return true;
	}

}
