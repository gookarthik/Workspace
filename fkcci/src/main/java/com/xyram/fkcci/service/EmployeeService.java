package com.xyram.fkcci.service;

import java.util.List;
import java.util.Map;


import com.xyram.fkcci.model.Employee;

public interface EmployeeService {
	public Employee addEmployee(Employee employee);
	public List<Employee> getAllEmployee();
	public Employee getEmployeeById(Integer employee_id);
	public Employee updateEmployee(Employee employee);
	public Integer deleteEmployeeById(Integer id);
	public Employee getEmployeeByName(String name);
	public Employee getEmployeeByPhNo(String phone_number);
	public Employee getEmployeeByStAddr(String streetAddress);
	public Employee getEmployeeByZpCode(String zip_code);
	public Employee getEmployeeByCity(String city);
	public Employee getEmployeeByRegion(String region);
	public Employee getEmployeeByCountry(String country);
	public Employee getEmployeeByEmail(String emailID);
	public Map<String,Integer> getTotal();
	public List<Employee> getEmployeeByDate();
	public List<Employee> getEmpByAscDateOrder(String order);
	public List<Employee> getNameInOrder(String order);
	public List<Employee> getFirstCCommonName();
	public List<Employee> getEmployeeSalary();
	public Employee getEmpByNameSalary(String name, Integer salary);
	public Employee getEmpOnColumn(String name, Integer salary);
	public List<Employee> getEmpOnSearch(String searhKey);


}
