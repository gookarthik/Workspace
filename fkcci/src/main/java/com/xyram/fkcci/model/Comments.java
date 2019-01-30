package com.xyram.fkcci.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.hibernate.annotations.CreationTimestamp;
/**
 * 
 * @fileName : Comments.java
 *
 * @description : 
 *
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
@Table(name = "comments")//Creating Table
public class Comments {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)//The GeneratedValue annotation may be applied to a primary key property or field of an entity or mapped superclass in conjunction with the Id annotation.
														//Provides the strategies for value of primary key 

	@Column(name = "comment_id", unique = true, nullable = false)
	private Integer commentId;

	@CreationTimestamp//To Track create and update Entity
	@Column(name = "comment_date" ,columnDefinition="TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)//Temporal data can have DATE, TIME, or TIMESTAMP precision
	private Date commentDate;
	
	/*@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "user_id")*/
	@Column(name="user_id")
	private Integer userId;
	
	@Column(name="user_name")
	private String userName;
	
	@ManyToOne//If the relationship is unidirectional
	@JsonBackReference//To resolve infinite recursion
	@JoinColumn(name = "tappals_id")//entity is owner of other relationship
	private Tappals tappals;
	
	@Column(name = "comment_details", columnDefinition = "text")
	private String commentDetails;

	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public Date getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Tappals getTappals() {
		return tappals;
	}

	public void setTappals(Tappals tappals) {
		this.tappals = tappals;
	}

	public String getCommentDetails() {
		return commentDetails;
	}

	public void setCommentDetails(String commentDetails) {
		this.commentDetails = commentDetails;
	}
	
}
