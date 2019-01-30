package com.xyram.fkcci.daoImpl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xyram.fkcci.dao.EmployeeDao;
import com.xyram.fkcci.model.Employee;

@SuppressWarnings("unused")
@Repository
@Transactional
public class EmployeeDaoImpl implements EmployeeDao {

	@Autowired
	private SessionFactory sessionFactory;
	private String name;

	public Employee addEmployee(Employee employee) {
		Session session = this.sessionFactory.getCurrentSession();
		session.saveOrUpdate(employee);
		return employee;
	}

	public Employee getEmployeeById(Integer employee_id) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Employee.class);
		criteria.add(Restrictions.eq("employeeId", employee_id));
		return (Employee) criteria.uniqueResult();

	}

	public List<Employee> getAllEmployee() {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Employee.class);
		@SuppressWarnings("unchecked")
		List<Employee> list = criteria.list();
		return list;

	}

	public Employee updateEmployee(Employee employee) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(employee);
		return employee;
	}

	public Integer deleteEmployeeById(Integer employee_id) {
		Session session = this.sessionFactory.getCurrentSession();
		Employee employee = (Employee) session.get(Employee.class, employee_id);
		if (null != employee) {
			session.delete(employee);
		}
		return employee.getEmployeeId();

	}

	public Employee getEmployeeByName(String name) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Employee.class);
		criteria.add(Restrictions.eq("name",name).ignoreCase());
		return (Employee) criteria.uniqueResult();
	}
	
	public Employee getEmployeeByPhNo(String phone_number) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Employee.class);
		criteria.add(Restrictions.eq("phoneNumber", phone_number));
		return (Employee) criteria.uniqueResult();
	}
	
	public Employee getEmployeeByStAddr(String streetAddress) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Employee.class);
		criteria.add(Restrictions.eq("streetAddress", streetAddress));
		return (Employee) criteria.uniqueResult();
	}
	
	public Employee getEmployeeByZpCode(String zip_code) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Employee.class);
		criteria.add(Restrictions.eq("zipCode", zip_code));
		return (Employee) criteria.uniqueResult();
	}
	
	public Employee getEmployeeByCity(String city) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Employee.class);
		criteria.add(Restrictions.eq("city", city));
		return (Employee) criteria.uniqueResult();
		}
	
	public Employee getEmployeeByRegion(String region) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Employee.class);
		criteria.add(Restrictions.eq("region", region));
		return (Employee) criteria.uniqueResult();
	}
	
	public Employee getEmployeeByCountry(String country) {
		Session session = this.sessionFactory.getCurrentSession();
		String hql = "FROM Employee WHERE country = '"+country+"'";
		Query query = session.createQuery(hql);
		return (Employee) query.uniqueResult();
		
		
		
		
		/*Criteria criteria = session.createCriteria(Employee.class);
		criteria.add(Restrictions.eq("country", country));
		return (Employee) criteria.uniqueResult();*/
	}
	
	public Employee getEmployeeByEmail(String emailID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "FROM Employee WHERE emailID ='"+emailID+"'";
		Query query = session.createQuery(hql);
		Employee employee = (Employee) query.uniqueResult();
		return employee;
		
		
		/*Criteria criteria = session.createCriteria(Employee.class);
		criteria.add(Restrictions.eq("emailID", emailID));
		return (Employee) criteria.uniqueResult();*/
	}
	
	public Map<String,Integer> getTotal() {
		Map<String,Integer> mapObj = new HashMap<String,Integer>();
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Employee.class);
		int cr = criteria.list().size();
		mapObj.put("TotalID",cr);
		return mapObj;
		
	}
	
	public List<Employee> getEmployeeByDate() {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Employee.class);
		criteria.addOrder(Order.desc("date"));
		List<Employee> list = criteria.list();
		return list;
	
	}
	
	public List<Employee> getEmpByAscDateOrder(String order) {
		Session session = sessionFactory.getCurrentSession();
		System.out.println("order: "+order);
		StringBuffer hql = new StringBuffer();
		hql.append("From Employee order by date "+order+"");
		Query query = session.createQuery(hql.toString());
		System.out.println("hql: "+ order);
		List <Employee> list = query.list();
		return list;
		
	}

	public List<Employee> getNameInOrder(String order) {
		Session session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer();
		hql.append("From Employee order by name "+order+"");
		Query query = session.createQuery(hql.toString());
		List<Employee>list = query.list();
		return list;
	}
	
	public List<Employee> getFirstCCommonName(){
		Session session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer();
		hql.append("select name from Employee group by name");
		System.out.println("query: "+hql);
		Query query = session.createQuery(hql.toString());
		List<Employee>list = query.list();
		return list;
	}
	
	public List<Employee> getEmployeeSalary(){
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Employee.class);
		criteria.add(Restrictions.between("salary", 25000,60000));	
		List<Employee> list = criteria.list();
		return list;
	}
	public Employee getEmpByNameSalary(String name, Integer salary) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Employee.class);
		criteria.add(Restrictions.eq("name", name).ignoreCase());
		criteria.add(Restrictions.eq("salary", salary));
		return (Employee) criteria.uniqueResult();
	}
	
	public Employee getEmpOnColumn(String name, Integer salary) {
		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Employee.class);
		criteria.add(Restrictions.eq("name", name).ignoreCase());
		criteria.add(Restrictions.eq("salary", salary));
		return (Employee) criteria.uniqueResult();
	}
	
	public List<Employee> getEmpOnSearch(String searhKey) {
		Session session = this.sessionFactory.getCurrentSession();
		StringBuffer hql = new StringBuffer();
		System.out.println("search:" + searhKey);
		hql.append("FROM Employee");
		if (searhKey != null) {
			hql.append(" where name like '" + searhKey + "'OR country like '" + searhKey + "'OR emailID like '"+ searhKey + "'");
		}
		else if(searhKey == null)
		{}
		Query query = session.createQuery(hql.toString());
		List<Employee> list = query.list();
		return list;
		/*Criteria criteria = session.createCriteria(Employee.class);
		Criterion  a = Restrictions.eq("name", searhKey);
		Criterion  b = Restrictions.eq("country", searhKey);
		Criterion  c = Restrictions.eq("emailID", searhKey);
		LogicalExpression orExp = Restrictions.or(a, b);
		LogicalExpression orExp1 = Restrictions.or(orExp,c);
		criteria.add(orExp1);
		return (Employee) criteria.uniqueResult();*/
	}
}
