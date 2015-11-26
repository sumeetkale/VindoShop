package com.shopping.vindoshop.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;

@JsonAutoDetect
public class ErrorHandler {
	String msg;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
