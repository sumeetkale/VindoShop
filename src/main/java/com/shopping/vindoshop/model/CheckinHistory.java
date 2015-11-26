package com.shopping.vindoshop.model;

import java.util.Date;

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
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "checkinHistory")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
@Cacheable
public class CheckinHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id", unique = true, nullable = false)
	private int id;

	@NotNull
	@Basic(optional = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "outlet_id")
	private Outlet outlet;

	@NotNull
	@Basic(optional = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	private Date checkinDate;

	public Date getCheckinDate() {
		return checkinDate;
	}

	public int getId() {
		return id;
	}

	public Outlet getOutlet() {
		return outlet;
	}

	public User getUser() {
		return user;
	}

	public void setCheckinDate(Date checkinDate) {
		this.checkinDate = checkinDate;
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
