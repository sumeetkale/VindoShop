package com.shopping.vindoshop.model;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "bookmark")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Cacheable
public class Bookmark {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id", unique = true, nullable = false)
	private int id;

	@JsonBackReference
	@Basic(optional = false)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "outlet_id")
	private Outlet outlet;

	@JsonBackReference("user")
	@Basic(optional = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	public int getId() {
		return id;
	}

	public Outlet getOutlet() {
		return outlet;
	}

	public User getUser() {
		return user;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setOutlet(Outlet outlet) {
		this.outlet = outlet;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
