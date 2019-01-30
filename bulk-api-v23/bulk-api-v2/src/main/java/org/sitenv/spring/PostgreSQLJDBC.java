package org.sitenv.spring;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class PostgreSQLJDBC {
	public static void main(String[] args) {
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test", "postgres", "postgres");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			/*
			 * String sqll = "CREATE TABLE ITCOMPANY " + "(ID INT PRIMARY KEY     NOT NULL,"
			 * + " NAME           TEXT    NOT NULL, " + " AGE            INT     NOT NULL, "
			 * + " ADDRESS        CHAR(50), " + " SALARY         REAL)";
			 */
			String sqll = "INSERT INTO SOFTWARE (ID, NAME, AGE, ADDRESS, SALARY)"
					+ "VALUES(1, 'KARTHIK', 24, 'MYSURU', 15000);";
			stmt.executeUpdate(sqll);
			sqll = "INSERT INTO SOFTWARE (ID,NAME,AGE,ADDRESS,SALARY) "
					+ "VALUES (2, 'Allen', 25, 'Texas', 15000.00 );";
			stmt.executeUpdate(sqll);
			sqll = "INSERT INTO SOFTWARE (ID,NAME,AGE,ADDRESS,SALARY) "
					+ "VALUES (3, 'Teddy', 23, 'Norway', 20000.00 );";
			stmt.executeUpdate(sqll);

			sqll = "INSERT INTO SOFTWARE (ID,NAME,AGE,ADDRESS,SALARY) "
					+ "VALUES (4, 'Mark', 25, 'Rich-Mond ', 65000.00 );";
			stmt.executeUpdate(sqll);
			stmt.close();
			c.commit();
			c.close();

		} catch (Exception e) {
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Records created successfully");
	}
}