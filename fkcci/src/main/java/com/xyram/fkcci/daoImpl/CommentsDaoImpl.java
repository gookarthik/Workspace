package com.xyram.fkcci.daoImpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.xyram.fkcci.dao.AbstractDao;
import com.xyram.fkcci.dao.CommentsDao;
import com.xyram.fkcci.model.Comments;
/**
 * 
 * @fileName : CommentsDaoImpl.java
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
@Repository
public class CommentsDaoImpl extends AbstractDao implements CommentsDao {

	public Comments saveComments(Comments comments) {
		getSession().saveOrUpdate(comments);
		return comments;
	}

	public List<Comments> getAllComments() {
		Criteria criteria = getSession().createCriteria(Comments.class);
		@SuppressWarnings("unchecked")
		List<Comments> list = criteria.list();
		return list;
	}

	public Comments getCommentById(Integer id) {
		Criteria criteria = getSession().createCriteria(Comments.class);
		criteria.add(Restrictions.eq("commentId", id));
		return (Comments)criteria.uniqueResult();
	}

	public List<Comments> getTappalComments(Integer slNo) {
		Criteria criteria = getSession().createCriteria(Comments.class);
		if(slNo != 0) {
			criteria.add(Restrictions.eq("tappals.slNo", slNo));
		}
		criteria.addOrder(Order.desc("commentDate"));
		@SuppressWarnings("unchecked")
		List<Comments> list = criteria.list();
		return list;
	}

	
}
