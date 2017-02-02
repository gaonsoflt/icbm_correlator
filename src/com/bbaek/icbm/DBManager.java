package com.bbaek.icbm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {
	static private String URL = "jdbc:postgresql://192.168.205.152:5432/ICBM";
	static private String USER = "icbm";
	static private String PASSWORD = "GaonIcbm@0805!*";
	
	Connection conn = null;
	
	public DBManager() {
		System.out.println("-------- PostgreSQL " + "JDBC Connection ------------");
		try {
			Class.forName("org.postgresql.Driver");
			System.out.println("PostgreSQL JDBC Driver Registered!");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your PostgreSQL JDBC Driver? " + "Include in your library path!");
			e.printStackTrace();
			return;
		}
		
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			System.out.println("Successed PostgreSQL Connection!");
		} catch (SQLException e) {
			e.printStackTrace();
			conn = null;
			System.out.println("Failed PostgreSQL Connection!");
		}
	}
	
	public void updateInsert(String value) {
		String data[] = value.split(",");
		String dt = data[0];
		String tm = data[1];
		float corrTH = Float.parseFloat(data[2]);
		float corrTV = Float.parseFloat(data[3]);
		float corrTN = Float.parseFloat(data[4]);
		float corrHV = Float.parseFloat(data[5]);
		float corrHN = Float.parseFloat(data[6]);
		float corrVN = Float.parseFloat(data[7]);

		Statement stmt = null;
		try {
			if (conn != null) {
				stmt = conn.createStatement();
				String updateSql = "UPDATE tb_correlation "
						+ "SET corrTH = " + corrTH 
						+ ", corrTV = " + corrTV
						+ ", corrTN = " + corrTN
						+ ", corrHV = " + corrHV
						+ ", corrHN = " + corrHN
						+ ", corrVN = " + corrVN
						+ " WHERE measure_date = '" + dt + "'"
						+ " AND measure_time = '" + tm + "'"; 
				System.out.println(updateSql);
				int executeCnt = stmt.executeUpdate(updateSql);
				System.out.println("update affected rows count: " + executeCnt);
				// 기존 데이터가 없으면 insert
				if(executeCnt < 1) {
					String insertSql = "INSERT INTO tb_correlation("
							+ "measure_date, measure_time, corrTH, corrTV, corrTN, corrHV, corrHN, corrVN) "
							+ "VALUES ("
							+ "'" + dt + "', '" + tm + "', " + corrTH + ", " + corrTV + ", " + corrTN + ", " + corrHV + ", " + corrHN + ", " + corrVN + ")";
					System.out.println(insertSql);
					executeCnt = stmt.executeUpdate(insertSql);
					System.out.println("insert affected rows count: " + executeCnt);
				}
			} else {
				System.out.println("Failed to make connection!");
			}
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		} finally {
			try {
				if(stmt != null) { stmt.close(); }
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean closeConnection() {
		try {
			if(conn != null) { 
				conn.close();
				System.out.println("Successed close PostgreSQL Connection!");
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Failed close PostgreSQL Connection!");
		} 
		return false;
	}
}
