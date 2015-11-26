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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "images")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
@Cacheable
public class Images {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id", unique = true, nullable = false)
	private int id;
	private String image;
	@JsonBackReference("image_tag")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "outlet_id")
	private Outlet outlet;

	public int getId() {
		return id;
	}

	public String getImage() {
		return image;
	}

	public Outlet getOutlet() {
		return outlet;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setOutlet(Outlet outlet) {
		this.outlet = outlet;
	}

}
