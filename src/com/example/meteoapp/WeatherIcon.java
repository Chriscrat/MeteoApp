package com.example.meteoapp;

import java.util.HashMap;

/*
 * Classe permettant de r�cup�rer un entier correspondant au nom de l'image r�cup�rer depuis l'API et � charger depuis les images en locale
 * Elle permet l'affichage d'images en ad�quation aux pr�visions m�t�o 
 */
public class WeatherIcon 
{	
	HashMap<String, Object> codeList;
	
	//Constructeur publique
	public WeatherIcon()
	{
		/*
		 * Cr�ation d'un HashMap contenant l'ensemble des noms des images 
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
	 * M�thode publique de type Object
	 * Elle permet de retourn�e l'entier correspondant au nom de l'image � charg�e
	 */
	public Object getFlagByIconCode(String code)
	{
		return codeList.get(code);
	}

}
