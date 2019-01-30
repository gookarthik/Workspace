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
import com.xyram.fkcci.model.Employee;
import com.xyram.fkcci.model.Tappals;
import com.xyram.fkcci.service.EmployeeService;

@Controller
@RequestMapping("/employee")
public class EmployeeController {
	@Autowired
	private EmployeeService employeeService;
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public Employee addEmployee(@RequestBody Employee employee)throws Exception
	{
		return employeeService.addEmployee(employee);
	}
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Employee getEmployeeById(@PathVariable Integer id) throws Exception {
		Employee response = employeeService.getEmployeeById(id);
		return response;
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public List<Employee> getAllEmployee() throws Exception {
		return this.employeeService.getAllEmployee();
	}
	
	@RequestMapping(value = "/", method = RequestMethod.PUT)
	@ResponseBody
	public Employee updateEmployee(@RequestBody Employee employee) {

		return this.employeeService.updateEmployee(employee);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public Integer deleteEmployeeById(@PathVariable Integer id) {
		Integer response = employeeService.deleteEmployeeById(id);
		return response;
	}
	
	@RequestMapping(value = "/name", method = RequestMethod.GET)
	@ResponseBody
	public Employee getEmployeeByName(@RequestParam(value = "name") String name) throws Exception {
		Employee response = employeeService.getEmployeeByName(name);
		return response;
	}
	
	@RequestMapping(value = "/phone_number", method = RequestMethod.GET)
	@ResponseBody
	public Employee getEmployeeByPhNo(@RequestParam(value = "phone_number")String phone_number)throws Exception {
		Employee response = employeeService.getEmployeeByPhNo(phone_number);
		return response;
	}
	
	@RequestMapping(value = "/streetAddress", method = RequestMethod.GET)
	@ResponseBody
	public Employee getEmployeeByStAddr(@RequestParam(value = "streetAddress")String streetAddress)throws Exception {
		Employee response = employeeService.getEmployeeByStAddr(streetAddress);
		return response;
	}
	
	@RequestMapping(value = "/zip_code", method = RequestMethod.GET)
	@ResponseBody
	public Employee getEmployeeByZpCode(@RequestParam(value= "zip_code")String zip_code)throws Exception {
		Employee response = employeeService.getEmployeeByZpCode(zip_code);
		return response;
	}
	
	@RequestMapping(value = "/city", method = RequestMethod.GET)
	@ResponseBody
	public Employee getEmployeeByCity(@RequestParam(value="city")String city)throws Exception {
		Employee response = employeeService.getEmployeeByCity(city);
		return response;
	}
	
	@RequestMapping(value = "/region", method = RequestMethod.GET)
	@ResponseBody
	public Employee getEmployeeByRegion(@RequestParam(value="region")String region)throws Exception {
		Employee response = employeeService.getEmployeeByRegion(region);
		return response;
	}
	
	@RequestMapping(value = "/country", method = RequestMethod.GET)
	@ResponseBody
	public Employee getEmployeeByCountry(@RequestParam(value = "country")String country)throws Exception {
		Employee response = employeeService.getEmployeeByCountry(country);
		return response;
	}
	
	@RequestMapping(value = "/emailID", method = RequestMethod.GET)
	@ResponseBody
	public Employee getEmployeeByEmail(@RequestParam(value = "emailID")String emailID)throws Exception {
		Employee response = employeeService.getEmployeeByEmail(emailID);
		return response;
	}
	
	@RequestMapping(value = "/total/count", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Integer> getTotal()throws Exception {
		Map<String,Integer> response = employeeService.getTotal();
		return response;
	}
	
	@RequestMapping(value = "/date", method = RequestMethod.GET)
	@ResponseBody
	public List<Employee> getEmployeeByDate()throws Exception {
		List<Employee> response = employeeService.getEmployeeByDate();
		return response;
	}
	
	@RequestMapping(value = "/DateInOrder", method = RequestMethod.GET)
	@ResponseBody
	public List<Employee> getEmpByAscDateOrder(@RequestParam(value="order", required=true)String order )throws Exception {
		List<Employee> response = employeeService.getEmpByAscDateOrder(order);
		return response;
	}
	
	@RequestMapping(value = "/NameInOrder", method = RequestMethod.GET)
	@ResponseBody
	public List<Employee> getNameInOrder(@RequestParam(value = "order", required =true)String order) throws Exception {
		List<Employee> response = employeeService.getNameInOrder(order);
		return response;
	}
	
	@RequestMapping(value = "/GroupFirstName", method = RequestMethod.GET)
	@ResponseBody
	public List<Employee> getFirstCCommonName()throws Exception {
		List<Employee> response = employeeService.getFirstCCommonName();
		return response;
	}
	
	@RequestMapping(value = "/SalaryBetween",  method = RequestMethod.GET)
	@ResponseBody
	public List<Employee> getEmployeeSalary()throws Exception {
		List<Employee> response = employeeService.getEmployeeSalary();
		return response;
	}
	
	@RequestMapping(value = "/NameAndSalary", method = RequestMethod.GET)
	@ResponseBody
	public Employee getEmpByNameSalary(
			@RequestParam(value = "name", required = true)String name,
			@RequestParam(value = "salary", required = true)Integer salary)throws Exception{
		Employee response = employeeService.getEmpByNameSalary(name, salary);
		return response;
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	public List<Employee> getEmpOnSearch(@RequestParam(value = "searhKey", required = true) String searhKey)
			throws Exception {
		List<Employee> response = employeeService.getEmpOnSearch(searhKey);
		return response;
	}
	
}






	
	

