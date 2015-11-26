package com.shopping.vindoshop.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "user")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id", unique = true, nullable = false)
	int id;
	@Basic(optional = false)
	private String username;
	@Basic(optional = false)
	private String password;
	@JsonIgnore
	private boolean isActive;
	@JsonIgnore
	private boolean isAdmin;

	public User() {
		super();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public int getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public boolean isActive() {
		return isActive;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
