package com.xyram.fkcci.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xyram.fkcci.model.Comments;
import com.xyram.fkcci.service.CommentsService;
/**
 * 
 * @fileName : CommentsController.java
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
@Controller//It indicates the class is a String Controller
@RequestMapping("/comments")//Used to Map Web Request into Class and Methods
public class CommentsController {
	@Autowired//It injects the object dependency implicitly
	private CommentsService commentsService;
	/**
	 * 
	 * @function saveComments 
	 * 
	 * @created_date : Nov 18, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @param comments
	 * @parm(s) : @return
	 * 
	 * @return : Comments
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody//Return type should be written straight into Response Body
	public Comments saveComments(@RequestBody Comments comments) {    //Method parameter should bound to HTTP value

		return commentsService.saveComments(comments);
	}
	/**
	 * 
	 * @function getAllComments 
	 * 
	 * @created_date : Nov 18, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @param searchKeyword
	 * @parm(s) : @return
	 * @parm(s) : @throws Exception
	 * 
	 * @return : List<Comments>
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public List<Comments> getAllComments()throws Exception {
		return this.commentsService.getAllComments();
	}
	/**
	 * 
	 * @function getCommentById 
	 * 
	 * @created_date : Nov 18, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @param id
	 * @parm(s) : @return
	 * 
	 * @return : Comments
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Comments getCommentById(@PathVariable Integer id) {  //Used to handle dynamic change in URL
		return this.commentsService.getCommentById(id);
	}
	/**
	 * 
	 * @function getModelClass 
	 * 
	 * @created_date : Nov 18, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @return
	 * 
	 * @return : Comments
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	@RequestMapping(value = "/model", method = RequestMethod.GET)
	@ResponseBody
	public Comments getModelClass() {
		Comments comments = new Comments();
		return comments;
	}
	/**
	 * 
	 * @function getTappalComments 
	 * 
	 * @created_date : Nov 30, 2017
	 * 
	 * @description
	 * 
	 * @parm(s) : @param slNo
	 * @parm(s) : @return
	 * @parm(s) : @throws Exception
	 * 
	 * @return : List<Comments>
	 *
	 * @throws : <Mentioned if any exceptions>
	 *
	 */
	@RequestMapping(value = "/slNo", method = RequestMethod.GET)
	@ResponseBody
	public List<Comments> getTappalComments(@RequestParam(value = "slNo", required = false) Integer slNo) //To retreve URL parameter and map it to method argument.
			throws Exception {
		return this.commentsService.getTappalComments(slNo);
	}
}
