package com.coolweather.app.model;

public class AreaInfo {
	private int id;
	private String name;
	private String code;
	private int level;
	
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id=id;
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name=name;
	}
	public String getCode(){
		return code;
	}
	public void setCode(String code){
		this.code=code;
	}
	public int getLevel(){
		return level;
	}
	public void setLevel(int level){
		this.level=level;
	}
	
}
