package com.xyram.fkcci.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * 
 * @fileName : Tappals.java
 *
 * @description :
 *
 * @version : 1.0
 *
 * @date: Nov 18, 2017
 *
 * @Author: Pradeep Rana
 *
 * @Reviewer: Sateesh Reddy
 */
@Entity
@Table(name = "tappals")
public class Tappals {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sl_no", unique = true, nullable = false)
	private Integer slNo;

	// Receptionist
	@CreationTimestamp
	@Column(name = "date" ,columnDefinition="TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@Column(name = "courier_name")
	private String courierName;

	@Column(name = "courier_rigno", unique=true,nullable = false)
	private String courierRigNo;

	@Column(name = "from_name")
	private String fromName;

	@Column(name = "to_role")
	private String toRole;

	// Librarian
	@CreationTimestamp
	@Column(name = "dispatched_date" ,columnDefinition="TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dispatchedDate;

	@CreationTimestamp
	@Column(name = "received_date" ,columnDefinition="TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date receivedDate;

	@Column(name = "particulars_details", columnDefinition = "text")
	private String particularsDetails;

	@Column(name = "file_path")
	private String filePath;

	@Column(name = "file_name")
	private String fileName;
	
	@Column(name = "comments")
	private String comments;

	@Column(name = "user_Editing")
	private boolean userEditing = false;
	
	@JsonManagedReference//Forward part of @JsonBackReference
	@OneToMany(mappedBy = "tappals", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Comments> allcomments = new ArrayList<Comments>();

	public Integer getSlNo() {
		return slNo;
	}

	public void setSlNo(Integer slNo) {
		this.slNo = slNo;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getCourierName() {
		return courierName;
	}

	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}

	public String getCourierRigNo() {
		return courierRigNo;
	}

	public void setCourierRigNo(String courierRigNo) {
		this.courierRigNo = courierRigNo;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getToRole() {
		return toRole;
	}

	public void setToRole(String toRole) {
		this.toRole = toRole;
	}

	public Date getDispatchedDate() {
		return dispatchedDate;
	}

	public void setDispatchedDate(Date dispatchedDate) {
		this.dispatchedDate = dispatchedDate;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getParticularsDetails() {
		return particularsDetails;
	}

	public void setParticularsDetails(String particularsDetails) {
		this.particularsDetails = particularsDetails;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public boolean isUserEditing() {
		return userEditing;
	}

	public void setUserEditing(boolean userEditing) {
		this.userEditing = userEditing;
	}

	public List<Comments> getAllcomments() {
		return allcomments;
	}

	public void setAllcomments(List<Comments> allcomments) {
		this.allcomments = allcomments;
	}
}
