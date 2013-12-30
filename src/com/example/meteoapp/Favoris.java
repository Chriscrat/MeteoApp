package com.example.meteoapp;

public class Favoris {
	
	private int id;
	private String city;
	private String cp;
	
	public Favoris(){}
	
	public Favoris(int id, String city, String cp)
	{
		this.id = id;
		this.city = city;
		this.cp = cp;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getCity()
	{
		return city;
	}
	
	public void setCity(String city)
	{
		this.city = city;
	}

	public String getCp()
	{
		return cp;
	}
	
	public void setCP(String cp)
	{
		this.cp = cp;
	}
	public String toString(){
		return "ID : "+id+"\n city : "+city+"\n cp : "+cp+"\n";
	}
}
