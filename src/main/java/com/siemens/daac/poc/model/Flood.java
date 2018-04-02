package com.siemens.daac.poc.model;

public class Flood {
	private String name;
	private boolean type;
	
	
	public String getName() {
		return name;
	}
	public boolean isType() {
		return type;
	}
	
	
	public Flood(String name, boolean type) {
		this.name = name;
		this.type = type;
	}
	public Flood() {
	}
}
