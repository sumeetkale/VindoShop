package com.shopping.vindoshop.model;

import javax.persistence.Basic;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {
	@Basic(optional = false)
	private boolean status;
	private String msg;
	private Object data;
	private int lastPageNumber;

	public Result() {
		super();
	}

	public Result(boolean status, String msg) {
		super();
		this.status = status;
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public int getLastPageNumber() {
		return lastPageNumber;
	}

	public String getMsg() {
		return msg;
	}

	public String isMsg() {
		return msg;
	}

	public boolean isStatus() {
		return status;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public void setLastPageNumber(int lastPageNumber) {
		this.lastPageNumber = lastPageNumber;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

}
