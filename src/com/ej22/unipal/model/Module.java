package com.ej22.unipal.model;

public class Module {
	private long id;
	private String module, abbreviation, color;
	
	public long setId(long id){
		return this.id = id;
	}
	
	public String setModule(String module){
		return this.module = module;
	}
	
	public String setAbbreviation(String abbreviation){
		return this.abbreviation = abbreviation;
	}
	
	public String setColor(String color)
	{
		return this.color = color;
	}
	
	public Long getID(){
		return id;
	}
	
	public String getModule(){
		return module;
	}
	
	public String getAbbreviation(){
		return abbreviation;
	}
	
	public String getColor(){
		return color;
	}
	
	
}
