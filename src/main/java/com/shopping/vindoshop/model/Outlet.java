package com.shopping.vindoshop.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Spatial;
import org.hibernate.search.annotations.SpatialMode;
import org.hibernate.search.spatial.Coordinates;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Indexed
@Entity
@Table(name = "outlet")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
@Cacheable
public class Outlet {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id", unique = true, nullable = false)
	private int id;

	@Field(analyzer = @Analyzer(definition = "ogram"))
	@Basic(optional = false)
	private String name;

	@Field(analyzer = @Analyzer(definition = "ogram"))
	private String address;
	private int pinCode;
	@Field(analyzer = @Analyzer(definition = "ogram"))
	private String city;
	private String miniAdd;
	private long phone;
	private String type;
	@Transient
	private List<Offer> currentOffers = new ArrayList<Offer>();

	@Transient
	private List<Offer> upcomingOffers = new ArrayList<Offer>();

	/*@JsonIgnore
	@JsonBackReference("brand_tag")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "brand_id")
	private Outlet brand;*/
	/*@JsonBackReference("mall_tag")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mall_id")
	@JsonIgnore
	private Outlet mall;*/
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "images", joinColumns = @JoinColumn(name = "outlet_id"))
	@Column(name = "image")
	private List<String> images = new ArrayList<String>();
	@IndexedEmbedded(targetElement = Category.class)
	@ManyToMany(targetEntity = Category.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "outletcategory", joinColumns = { @JoinColumn(name = "outlet_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "category_id") })
	@Basic(optional = false)
	@JsonIgnore
	private Set<Category> category = new HashSet<Category>();
	private double longitude;
	private double latitude;
	@JsonIgnore
	private int ratingCount;
	@Transient
	private List<Double> newRating;

	@Field
	private double rating;
	private double inventoryRating;
	private double staffRating;
	private double discountRating;

	/*@JsonManagedReference("brand_tag")
	@OneToMany(mappedBy = "brand", fetch = FetchType.EAGER)
	private Set<Outlet> brandOutlets = new HashSet<Outlet>();

	@JsonManagedReference("mall_tag")
	@OneToMany(mappedBy = "mall", fetch = FetchType.EAGER)
	private Set<Outlet> mallOutlets = new HashSet<Outlet>();*/
	@Field(analyzer = @Analyzer(definition = "ogram"))
	private String tags;
	@Transient
	private boolean outletIsFav;

	public String getAddress() {
		return address;
	}

	public Set<Category> getCategory() {
		return category;
	}

	public String getCity() {
		return city;
	}

	public List<Offer> getCurrentOffers() {
		return currentOffers;
	}

	public double getDiscountRating() {
		return discountRating;
	}

	public int getId() {
		return id;
	}

	public List<String> getImages() {
		return images;
	}

	public double getInventoryRating() {
		return inventoryRating;
	}

	public double getLatitude() {
		return latitude;
	}

	@Spatial(spatialMode = SpatialMode.GRID)
	public Coordinates getLocation() {
		return new Coordinates() {
			@Override
			public Double getLatitude() {
				return latitude;
			}

			@Override
			public Double getLongitude() {
				return longitude;
			}
		};
	}

	public double getLongitude() {
		return longitude;
	}

	public String getMiniAdd() {
		return miniAdd;
	}

	public String getName() {
		return name;
	}

	public List<Double> getNewRating() {
		return newRating;
	}

	public long getPhone() {
		return phone;
	}

	public int getPinCode() {
		return pinCode;
	}

	public double getRating() {
		return rating;
	}

	public int getRatingCount() {
		return ratingCount;
	}

	public double getStaffRating() {
		return staffRating;
	}

	public String getTags() {
		return tags;
	}

	public String getType() {
		return type;
	}

	public List<Offer> getUpcomingOffers() {
		return upcomingOffers;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setCategory(Set<Category> category) {
		this.category = category;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCurrentOffers(List<Offer> currentOffers) {
		this.currentOffers = currentOffers;
	}

	public void setDiscountRating(double discountRating) {
		this.discountRating = discountRating > 5 ? 5 : discountRating;
	}

	public boolean isOutletIsFav() {
		return outletIsFav;
	}

	public void setOutletIsFav(boolean outletIsFav) {
		this.outletIsFav = outletIsFav;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public void setInventoryRating(double inventoryRating) {
		this.inventoryRating = inventoryRating > 5 ? 5 : inventoryRating;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setMiniAdd(String miniAdd) {
		this.miniAdd = miniAdd;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNewRating(List<Double> newRating) {
		this.newRating = newRating;
	}

	public void setPhone(long phone) {
		this.phone = phone;
	}

	public void setPinCode(int pinCode) {
		this.pinCode = pinCode;
	}

	public void setRating(double rating) {
		this.rating = rating > 5 ? 5 : rating;
	}

	public void setRatingCount(int ratingCount) {
		this.ratingCount = ratingCount;
	}

	public void setStaffRating(double staffRating) {
		this.staffRating = staffRating > 5 ? 5 : staffRating;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUpcomingOffers(List<Offer> upcomingOffers) {
		this.upcomingOffers = upcomingOffers;
	}

}
