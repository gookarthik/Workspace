package com.xyram.fkcci.service;

import java.util.List;

import com.xyram.fkcci.model.Comments;
/**
 * 
 * @fileName : CommentsService.java
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
public interface CommentsService {

	Comments saveComments(Comments comments);

	List<Comments> getAllComments();

	Comments getCommentById(Integer id);

	List<Comments> getTappalComments(Integer slNo);

}
