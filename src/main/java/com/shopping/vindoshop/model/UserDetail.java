package com.shopping.vindoshop.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "userdetail")
@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Cacheable
public class UserDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id", unique = true, nullable = false)
	int id;
	@Basic(optional = false)
	private String firstName;
	private String lastName;
	private Long phone;
	private int pinCode;
	private String city;
	private String state;
	private String country;
	private int points;
	private int otp;
	@Transient
	private List<Bookmark> bookmarks = new ArrayList<Bookmark>();
	@Transient
	private List<CheckinHistory> checkins = new ArrayList<CheckinHistory>();
	@Transient
	private int rank;
	String location;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;
	@JsonIgnore
	private Date updatedAt;
	private String referral;
	private boolean subscribe;
	private byte[] profileImage;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateofBirth;

	public UserDetail() {
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
		UserDetail other = (UserDetail) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public List<Bookmark> getBookmarks() {
		return bookmarks;
	}

	public List<CheckinHistory> getCheckins() {
		return checkins;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public Date getDateofBirth() {
		return dateofBirth;
	}

	public String getFirstName() {
		return firstName;
	}

	public int getId() {
		return id;
	}

	public String getLastName() {
		return lastName;
	}

	public String getLocation() {
		return location;
	}

	public int getOtp() {
		return otp;
	}

	public Long getPhone() {
		return phone;
	}

	public int getPinCode() {
		return pinCode;
	}

	public int getPoints() {
		return points;
	}

	public byte[] getProfileImage() {
		return profileImage;
	}

	public int getRank() {
		return rank;
	}

	public String getReferral() {
		return referral;
	}

	public String getState() {
		return state;
	}

	public java.util.Date getUpdatedAt() {
		return updatedAt;
	}

	public User getUser() {
		return user;
	}

	public boolean isSubscribe() {
		return subscribe;
	}

	public void setBookmarks(List<Bookmark> bookmarks) {
		this.bookmarks = bookmarks;
	}

	public void setCheckins(List<CheckinHistory> checkins) {
		this.checkins = checkins;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setDateofBirth(Date dateofBirth) {
		this.dateofBirth = dateofBirth;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setOtp(int otp) {
		this.otp = otp;
	}

	public void setPhone(Long phone) {
		this.phone = phone;
	}

	public void setPinCode(int pinCode) {
		this.pinCode = pinCode;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public void setProfileImage(byte[] profileImage) {
		this.profileImage = profileImage;
	}

	public void setRank(int rank) {
		this.rank = rank;
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

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
