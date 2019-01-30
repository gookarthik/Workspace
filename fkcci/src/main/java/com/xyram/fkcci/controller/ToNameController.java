package com.xyram.fkcci.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xyram.fkcci.model.Tappals;
import com.xyram.fkcci.model.ToName;
import com.xyram.fkcci.service.ToNameService;
/**
 * 
 * @fileName : ToNameController.java
 *
 * @description : 
 *
 * @version : 1.0
 *
 * @date: Nov 23, 2017
 *
 * @Author: Pradeep Rana
 *
 * @Reviewer: Sateesh Reddy
 */
@Controller
@RequestMapping("/toName")
public class ToNameController {
	
	
	@Autowired
	private ToNameService toNameService;
	
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public ToName saveToName(@RequestBody ToName toName) throws Exception {

		return toNameService.saveToName(toName);
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public List<ToName> getAllToName()throws Exception {
		return this.toNameService.getAllToName();
	}
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ToName getToNameById(@PathVariable Integer id) throws Exception {
		return this.toNameService.getToNameById(id);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteToNamerById(@PathVariable Integer id) {

		String response = toNameService.deleteToNameById(id);
		return response;
	}
	
	@RequestMapping(value = "/model", method = RequestMethod.GET)
	@ResponseBody
	public ToName getModelClass() {
		ToName comments = new ToName();
		return comments;
	}
	
	@RequestMapping(value = "/mypost", method = RequestMethod.GET)
	@ResponseBody
	public List<Tappals> getMyPost(
			@RequestParam(value = "toName", required = false) String toName,
			@RequestParam(value = "offset", required = false) String offsetStr,
			@RequestParam(value = "limit", required = false) String limitStr)
			throws Exception {
		int offset = 0, limit = 10;
		if (offsetStr != null) {
			offset = Integer.parseInt(offsetStr);
		}
		if (limitStr != null) {
			limit = Integer.parseInt(limitStr);
		}
		return (List<Tappals>) this.toNameService.getMyPost(toName, offset, limit);
	}
	
	@RequestMapping(value = "/mypost/today", method = RequestMethod.GET)
	@ResponseBody
	public List<Tappals> getMyTodayPost(@RequestParam(value = "toName", required = true) String toName)
			throws Exception {
	
		return (List<Tappals>) this.toNameService.getMyTodayPost(toName);
	}
	
	@RequestMapping(value = "/mypost/yesterday", method = RequestMethod.GET)
	@ResponseBody
	public List<Tappals> getMyYesterdayPost(@RequestParam(value = "toName", required = true) String toName)
			throws Exception {
	
		return (List<Tappals>) this.toNameService.getMyYesterdayPost(toName);
	}
	
	@RequestMapping(value = "/mypost/lastWeek", method = RequestMethod.GET)
	@ResponseBody
	public List<Tappals> getMyLastWeekPost(@RequestParam(value = "toName", required = true) String toName)
			throws Exception {
	
		return (List<Tappals>) this.toNameService.getMyLastWeekPost(toName);
	}
	
	@RequestMapping(value = "/mypost/lastMonth", method = RequestMethod.GET)
	@ResponseBody
	public List<Tappals> getMyLastMonthPost(@RequestParam(value = "toName", required = true) String toName)
			throws Exception {
	
		return (List<Tappals>) this.toNameService.getMyLastMonthPost(toName);
	}
	
	@RequestMapping(value = "/account", method = RequestMethod.GET)
	@ResponseBody
	public List<Tappals> getAccountsPost() throws Exception {
		return (List<Tappals>) this.toNameService.getAccountsPost();
	}

	@RequestMapping(value = "/management", method = RequestMethod.GET)
	@ResponseBody
	public List<Tappals> getManagementPost() throws Exception {
		return (List<Tappals>) this.toNameService.getManagementPost();
	}

	@RequestMapping(value = "/others", method = RequestMethod.GET)
	@ResponseBody
	public List<Tappals> getOthersPost() throws Exception {
		return (List<Tappals>) this.toNameService.getOthersPost();
	}
	
	@RequestMapping(value = "/hitCount", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getTotalHitcount(@RequestParam(value = "roleName", required = true) String roleName) throws Exception {
		return (List<Map<String, Object>>) this.toNameService.getTotalHitcount(roleName);
	}
}

