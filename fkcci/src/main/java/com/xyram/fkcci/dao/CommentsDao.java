package com.xyram.fkcci.dao;

import java.util.List;

import com.xyram.fkcci.model.Comments;
/**
 * 
 * @fileName : CommentsDao.java
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
public interface CommentsDao {
	
	public Comments saveComments(Comments comments);

	public List<Comments> getAllComments();

	public Comments getCommentById(Integer id);

	public List<Comments> getTappalComments(Integer slNo);

}
