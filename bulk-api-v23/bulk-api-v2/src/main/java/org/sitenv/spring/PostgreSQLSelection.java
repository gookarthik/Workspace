package org.sitenv.spring;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.jsp.jstl.sql.Result;

public class PostgreSQLSelection {

	public static void main(String[] args) {
		Connection c = null;
		Statement stmt = null;
		try {
			
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test", "postgres", "postgres");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");
			
			stmt = c.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM SOFTWARE");
			while(rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String address = rs.getString("address");
				int salary = rs.getInt("salary");
				System.out.println("ID = " + id);
				System.out.println("Name =" + name);
				System.out.println("Age = " + age);
				System.out.println("Address = " + address);
				System.out.println("Salary = "+ salary);
				System.out.println();
			}
			
		}catch(Exception e) {
			System.out.println(e.getClass().getName()+": "+ e.getMessage());
			System.exit(0);
		}
		System.out.println("Records created successfully");
	}
}
