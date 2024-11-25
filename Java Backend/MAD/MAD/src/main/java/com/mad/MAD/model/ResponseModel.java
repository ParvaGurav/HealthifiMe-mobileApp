package com.mad.MAD.model;

import lombok.Getter;
import lombok.Setter;


public class ResponseModel {

	private String message;
	private String code;
	private Object data;
	
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCode() {
		return code;
	}
	

	public void setCode(String code) {
		this.code = code;
	}
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public ResponseModel() {
		super();
	}
}
