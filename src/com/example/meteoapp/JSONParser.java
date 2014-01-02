package com.example.meteoapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.util.Log;

/*
 * Classe permettant de parser les URL issue de l'API OpenWeatherMap, fournissant des informations météo au format JSON
 */
public class JSONParser 
{ 
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
 
    //Constructeur public
    public JSONParser() {
    }
 
    /*
     * Méthode publique de type JSONObject
     * Elle permet de récupérée au format JSON, des informations fournies par une URL renseignée en paramètre
     */
    public JSONObject getJSONFromUrl(String url) {
 
        //Fabrication d'une requête HTTP
        try 
        {
            //DefaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
 
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();       
        } 
        catch (UnsupportedEncodingException e) 
        {
            e.printStackTrace();
        } 
        catch (ClientProtocolException e) 
        {
            e.printStackTrace();
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
         
        try 
        {        	
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8); //Récuperation du flux encodé en ISO pour les caractères spéciaux
            StringBuilder sb = new StringBuilder();
            String line = null;
            //Chargement du contenu du flux dans un variable de type String
            while ((line = reader.readLine()) != null) 
                sb.append(line + "\n");
            
            is.close();
            json = sb.toString();
        } 
        catch (Exception e) 
        {
            Log.e("Erreur du Buffer", "Erreur dans la conversion du résultat : " + e.toString());
        }
 
        // Tentative de conversion de String à JSONObject
        try 
        {
            jObj = new JSONObject(json);
        } 
        catch (JSONException e) 
        {
            Log.e("Parser JSON", "Erreur parsage de données : " + e.toString());
        } 
        return jObj; 
    }
}
