package com.xyram.fkcci.serviceImpl;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xyram.fkcci.dao.EmployeeDao;
import com.xyram.fkcci.model.Employee;
import com.xyram.fkcci.service.EmployeeService;

@Service
@Transactional

public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeDao employeeDao;

	public Employee addEmployee(Employee employee) {
		return this.employeeDao.addEmployee(employee);
	}

	
	public List<Employee> getAllEmployee() {
		return this.employeeDao.getAllEmployee();
	}

	public Employee getEmployeeById(Integer employee_id) {
		return this.employeeDao.getEmployeeById(employee_id);
	}
	
	public Employee updateEmployee(Employee employee) {
		return employeeDao.updateEmployee(employee);
	}
	
	public Integer deleteEmployeeById(Integer employee_id) {
		return employeeDao.deleteEmployeeById(employee_id);
	}
	public Employee getEmployeeByName(String name) {
		return employeeDao.getEmployeeByName(name);
	}
	
	public Employee getEmployeeByPhNo(String phone_number) {
		return employeeDao.getEmployeeByPhNo(phone_number);
	}
	
	public Employee getEmployeeByStAddr(String streetAddress) {
		return employeeDao.getEmployeeByStAddr(streetAddress);
	}
	
	public Employee getEmployeeByZpCode(String zip_code) {
		return employeeDao.getEmployeeByZpCode(zip_code);
	}
	
	public Employee getEmployeeByCity(String city) {
		return employeeDao.getEmployeeByCity(city);
	}
	
	public Employee getEmployeeByRegion(String region) {
		return employeeDao.getEmployeeByRegion(region);
	}
	
	public Employee getEmployeeByCountry(String country) {
		return employeeDao.getEmployeeByCountry(country);
	}
	
	public Employee getEmployeeByEmail(String emailID) {
		return employeeDao.getEmployeeByEmail(emailID);
	}
	
	public Map<String,Integer> getTotal() {
		return employeeDao.getTotal();
	}
	
	public List<Employee> getEmployeeByDate() {
		 return employeeDao.getEmployeeByDate();
	}
	
	public List<Employee> getEmpByAscDateOrder(String order){
		return employeeDao.getEmpByAscDateOrder(order);
	}
	
	public List<Employee> getNameInOrder(String order){
		return employeeDao.getNameInOrder(order);
	}
	
	public List<Employee> getFirstCCommonName() {
		return employeeDao.getFirstCCommonName();
	}
	
	public List<Employee> getEmployeeSalary() {
		return employeeDao.getEmployeeSalary();
	}
	
	public Employee getEmpByNameSalary(String name, Integer salary)  {
		return employeeDao.getEmpByNameSalary(name, salary);
	}
	
	public Employee getEmpOnColumn(String name, Integer salary) {
		return employeeDao.getEmpOnColumn(name, salary);
	}
	
	public List<Employee> getEmpOnSearch(String searhKey) {
		return employeeDao.getEmpOnSearch(searhKey);
	}
}
