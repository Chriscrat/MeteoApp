package com.example.meteoapp;

import java.util.HashMap;

/*
 * Classe permettant de récupérer un entier correspondant au nom de l'image récupérer depuis l'API et à charger depuis les images en locale
 * Elle permet l'affichage d'images en adéquation aux prévisions météo 
 */
public class WeatherIcon 
{	
	HashMap<String, Object> codeList;
	
	//Constructeur publique
	public WeatherIcon()
	{
		/*
		 * Création d'un HashMap contenant l'ensemble des noms des images 
		 * Elles sont disponibles dans les ressources de l'application (res > Drawable)
		 */
		codeList = new HashMap<String,Object>();
		codeList.put("01d", 1);
		codeList.put("01n", 2);
		codeList.put("02d", 3);
		codeList.put("02n", 4);
		codeList.put("03d", 5);
		codeList.put("03n", 6);
		codeList.put("04d", 7);
		codeList.put("04n", 8);
		codeList.put("09d", 9);
		codeList.put("09n", 10);
		codeList.put("10d", 11);
		codeList.put("10n", 12);
		codeList.put("11d", 13);
		codeList.put("11n", 14);
		codeList.put("13d", 15);
		codeList.put("13n", 16);
		codeList.put("50d", 17);
		codeList.put("50n", 18);
	}
	
	/*
	 * Méthode publique de type Object
	 * Elle permet de retournée l'entier correspondant au nom de l'image à chargée
	 */
	public Object getFlagByIconCode(String code)
	{
		return codeList.get(code);
	}

}
