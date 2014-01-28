package com.example.meteoapp;

import com.example.meteoapp.SearchOnglet.ErrorStatus;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

/*
 * Classe permettant de construire l'Activity HomePage
 * Elle permet d'afficher l'écran d'accueil
 * Cette Activity dispose de deux onglets dont sont contenus les Activitys SearchOnglet(affichée par défaut) et FavOnglet
 */
@SuppressWarnings("deprecation")
public class HomePage extends TabActivity //Héritage permettant l'utilisation des TabHosts pour la création d'onglet
{

	
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
		setContentView(R.layout.activity_homepage);
		mContext = this;

		Resources res = getResources(); //Déclaration d'un objet permettant de récuperer l'ensemble des ressources du projet (images, sons, etc ..)
		final TabHost tabHost = getTabHost();
		
		/*
		 * Création des onglets de l'Activity HomePage
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
	        	compute();
			    for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
		        {
		           tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#dedede")); //Code couleur pour l'onglet non-sélectionné
		        }
		        tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#78c8e3")); //Code couleur pour l'onglet sélectionné
			}
		}));
	}
	
	/*
	 * Méthode privée de type void
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
                
                msg = mHandler.obtainMessage(MSG_CNF, "Chargement terminé !");
                
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
		 * Méthode publique de type void
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
		            //Toast.makeText(mContext, "Info: " + text2display, Toast.LENGTH_LONG).show();
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
