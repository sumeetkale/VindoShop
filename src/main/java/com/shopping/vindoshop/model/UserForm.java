package com.shopping.vindoshop.model;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@Cacheable
public class UserForm {
	private String firstName;
	private String lastName;
	private long phone;
	private int pinCode;
	private String city;
	private String state;
	private String country;
	private String username;
	private String password;
	private String referral;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateOfBirth;
	private boolean subscribe;

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPassword() {
		return password;
	}

	public long getPhone() {
		return phone;
	}

	public int getPinCode() {
		return pinCode;
	}

	public String getReferral() {
		return referral;
	}

	public String getState() {
		return state;
	}

	public String getUsername() {
		return username;
	}

	public boolean isSubscribe() {
		return subscribe;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPhone(long phone) {
		this.phone = phone;
	}

	public void setPinCode(int pinCode) {
		this.pinCode = pinCode;
	}

	public void setReferral(String referral) {
		this.referral = referral;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setSubscribe(boolean subscribe) {
		this.subscribe = subscribe;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
