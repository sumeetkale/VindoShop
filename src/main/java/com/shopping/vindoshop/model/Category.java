package com.shopping.vindoshop.model;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Indexed
@Entity
@Table(name = "category")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
@Cacheable
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id", unique = true, nullable = false)
	private int id;

	@Field(analyze = Analyze.NO)
	@Basic(optional = false)
	private String name;

	private String imageUrl;
	private String imageClass;

	public String getImageUrl() {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		HttpServletRequest req = sra.getRequest();
		if (imageUrl != null)
			return req.getRequestURL().toString()
					.replace(req.getRequestURI(), req.getContextPath())
					+ imageUrl;
		else
			return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public boolean equals(Object categoryName) {
		return this.name.equalsIgnoreCase(categoryName.toString());
	}

	public int getId() {
		return id;
	}

	public String getImageClass() {
		return imageClass;
	}

	public String getName() {
		return name;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setImageClass(String imageClass) {
		this.imageClass = imageClass;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
