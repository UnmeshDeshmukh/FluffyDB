package gash.router.server.db;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.sql.*;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

public class PostgreSQL implements DatabaseClient {

	Connection conn = null;

	public PostgreSQL(String url, String username, String password, String dbname, String ssl) throws SQLException {
		
		Properties props = new Properties();
		props.setProperty("user", username);
		props.setProperty("password", password);
		props.setProperty("ssl", ssl);
		conn = DriverManager.getConnection(url, props);
	}

	@Override
	public byte[] get(String key) {
		Statement stmt = null;
		byte[] image=null; 
		System.out.println(key);
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("Select * FROM testtable WHERE \"key\" LIKE '"+key+"'");
			
			while (rs.next()) {
				image=rs.getBytes(1);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// TODO connection handling
		}
		return image;
		
	}

	@Override
	public String post(byte[] image){
		Statement stmt = null;
		String key = UUID.randomUUID().toString();
		try {
			stmt = conn.createStatement();
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO testtable VALUES ( '");
			sql.append(key + "' , '" + image + "' );");
			
			stmt.executeUpdate(sql.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// TODO close connection, decide if need to keep open all time or
			// initiate new everytime
		}
		return key;
	}
	
	@Override
	public void put(String key, byte[] image){
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE testtable SET image= '");
			sql.append(image + "' WHERE key LIKE '"+key+"';");
			
			stmt.executeUpdate(sql.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// TODO close connection, decide if need to keep open all time or
			// initiate new everytime
		}
	}
	
	@Override
	public void delete(String key){
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM testtable WHERE key LIKE '"+key+"';");
			
			stmt.executeUpdate(sql.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// TODO close connection, decide if need to keep open all time or
			// initiate new everytime
		}
	}

}
