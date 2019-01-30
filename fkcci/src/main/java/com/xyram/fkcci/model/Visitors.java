package com.xyram.fkcci.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
/**
 * 
 * @fileName : Visitors.java
 *
 * @description : Visitors model class
 *
 * @version : 1.0
 *
 * @date: Dec 02, 2017
 *
 * @Author: Pradeep Rana
 *
 * @Reviewer: Sateesh Reddy
 */
@Entity
@Table(name = "visitors")
public class Visitors {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "visitor_id", unique = true, nullable = false)
	private Integer visitorId;

	@Column(name="visitor_name")
	private String visitorName;
	
	@Column(name = "contact_no")
	private String contactNo;
	
	@Column(name="address", columnDefinition="text")
	private String address;
	
	@Column(name = "visit_purpose")
	private String visitPurpose;
	
	@Column(name = "to_meet")
	private String toMeet;
	
	@CreationTimestamp
	@Column(name = "visit_date_time" ,columnDefinition="TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date visitDateTime;

	public Integer getVisitorId() {
		return visitorId;
	}

	public void setVisitorId(Integer visitorId) {
		this.visitorId = visitorId;
	}

	public String getVisitorName() {
		return visitorName;
	}

	public void setVisitorName(String visitorName) {
		this.visitorName = visitorName;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getVisitPurpose() {
		return visitPurpose;
	}

	public void setVisitPurpose(String visitPurpose) {
		this.visitPurpose = visitPurpose;
	}

	public String getToMeet() {
		return toMeet;
	}

	public void setToMeet(String toMeet) {
		this.toMeet = toMeet;
	}

	public Date getVisitDateTime() {
		return visitDateTime;
	}

	public void setVisitDateTime(Date visitDateTime) {
		this.visitDateTime = visitDateTime;
	}
}
