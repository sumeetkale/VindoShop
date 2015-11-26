package com.shopping.vindoshop.model;

import java.sql.Date;

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

import org.apache.solr.analysis.LowerCaseFilterFactory;
import org.apache.solr.analysis.NGramFilterFactory;
import org.apache.solr.analysis.StandardFilterFactory;
import org.apache.solr.analysis.StandardTokenizerFactory;
import org.apache.solr.analysis.StopFilterFactory;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Indexed
@Entity
@Table(name = "offer")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
@AnalyzerDef(name = "ogram", tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class), filters = {
		@TokenFilterDef(factory = StandardFilterFactory.class),
		@TokenFilterDef(factory = LowerCaseFilterFactory.class),
		@TokenFilterDef(factory = StopFilterFactory.class),
		@TokenFilterDef(factory = NGramFilterFactory.class, params = {
				@Parameter(name = "minGramSize", value = "3"),
				@Parameter(name = "maxGramSize", value = "3") }) })
@Cacheable
public class Offer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id", unique = true, nullable = false)
	private int id;

	@IndexedEmbedded(targetElement = Outlet.class)
	@Basic(optional = false)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "outlet_id")
	private Outlet outlet;
	private String image;
	@Basic(optional = false)
	private Date startDate;

	@Basic(optional = false)
	private Date endDate;

	@Field(analyzer = @Analyzer(definition = "ogram"))
	private String detail;
	private String couponCode;
	@Field
	private int discount;
	private boolean exclusive;
	private boolean trending;
	private String shortDesc;

	public String getCouponCode() {
		return couponCode;
	}

	public String getDetail() {
		return detail;
	}

	public int getDiscount() {
		return discount;
	}

	public Date getEndDate() {
		return endDate;
	}

	public int getId() {
		return id;
	}

	public String getImage() {
		return image;
	}

	public Outlet getOutlet() {
		return outlet;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public Date getStartDate() {
		return startDate;
	}

	public boolean isExclusive() {
		return exclusive;
	}

	public boolean isTrending() {
		return trending;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setExclusive(boolean exclusive) {
		this.exclusive = exclusive;
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

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setTrending(boolean trending) {
		this.trending = trending;
	}

}
