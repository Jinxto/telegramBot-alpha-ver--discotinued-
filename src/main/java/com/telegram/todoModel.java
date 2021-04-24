package com.telegram;

public class todoModel {
	private String id;
	private String data;
	private String date;
	public todoModel(String id, String data, String date) {
		super();
		this.id = id;
		this.data = data;
		this.date = date;
	}
	public todoModel() {
		// TODO Auto-generated constructor stub
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

}
