package com.shopping.vindoshop.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "business")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class Business {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id", unique = true, nullable = false)
	private int id;

	@Basic(optional = false)
	private String name;
	private String storeName;
	private String address;
	private int pinCode;
	private String city;
	private String state;
	private long phone;

	public String getAddress() {
		return address;
	}

	public String getCity() {
		return city;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public long getPhone() {
		return phone;
	}

	public int getPinCode() {
		return pinCode;
	}

	public String getState() {
		return state;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhone(long phone) {
		this.phone = phone;
	}

	public void setPinCode(int pinCode) {
		this.pinCode = pinCode;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

}
