package com.example.meteoapp;

import android.os.Bundle;
import android.app.TabActivity;
import android.util.Log;
import android.view.Menu;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

@SuppressWarnings("deprecation")
public class Meteo extends TabActivity {
	final String CITY_SELECTED = "a_city";
	final String CP_SELECTED = "a_cp";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meteo);
		String city="", cp="";
		Intent intent = getIntent();
		Bundle extras = getIntent().getExtras();
		if (intent != null) {
			city = extras.getString(CITY_SELECTED);
			cp = extras.getString(CP_SELECTED);
			Log.v("Activity meteo : ville : ", city);
		}	

		Resources res = getResources();
		final TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent2 = new Intent().setClass(this, TodayOnglet.class);
		extras.putString(CITY_SELECTED, city);
		extras.putString(CP_SELECTED, cp);
		intent2.putExtras(extras);

		spec = tabHost.newTabSpec("Widget").setIndicator("Aujourd'hui", res.getDrawable(android.R.drawable.ic_menu_day)).setContent(intent2);
		tabHost.addTab(spec);
		
		intent2 = new Intent().setClass(this, WeekOnglet.class);
		intent2.putExtras(extras);

		spec = tabHost.newTabSpec("Form").setIndicator("Semaine", res.getDrawable(android.R.drawable.ic_menu_week)).setContent(intent2);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_city, menu);
		return true;
	}

}
