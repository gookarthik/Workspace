package com.xyram.fkcci.serviceImpl;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xyram.fkcci.dao.CommentsDao;
import com.xyram.fkcci.model.Comments;
import com.xyram.fkcci.service.CommentsService;
/**
 * 
 * @fileName : CommentsServiceImpl.java
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
@Service
@Transactional
public class CommentsServiceImpl implements CommentsService {
	
	@Autowired
	private CommentsDao commentsDao;

	public Comments saveComments(Comments comments) {
		return commentsDao.saveComments(comments);
	}

	public List<Comments> getAllComments() {
		return commentsDao.getAllComments();
	}

	public Comments getCommentById(Integer id) {
		return commentsDao.getCommentById(id);
	}

	public List<Comments> getTappalComments(Integer slNo) {
		return commentsDao.getTappalComments(slNo);
	}
}
