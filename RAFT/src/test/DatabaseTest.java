package test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import server.db.PostgreSQL;
import server.db.Record;

public class DatabaseTest {
	protected PostgreSQL postgre;
	protected String url="jdbc:postgresql://localhost:5432/db275";
	protected String username="jagruti";
	protected String password="linux2015";
	protected String dbname="db275";
	protected String ssl="false";
	
	public DatabaseTest() {
		try {
			postgre= new PostgreSQL(url, username, password, ssl);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void put(String key, String value) {
		postgre.put(key, value.getBytes(), System.currentTimeMillis());
	}

	public String post(String value) {
		return postgre.post(value.getBytes() , System.currentTimeMillis());
	}

	public byte[] get(String key) {
		return postgre.get(key);
	}

	public void delete(String key) {
		postgre.delete(key);
	}
	
	public long getCurrentTimeStamp() {
		return postgre.getCurrentTimeStamp();
	}
	
	public List<Record> getNewEntries(long staleTimeStamp) {
		return postgre.getNewEntries(staleTimeStamp);
	}

	public void putEntries(List<Record> list) {
		postgre.putEntries(list);
	}

	public static void main(String args[]) {
		DatabaseTest db = new DatabaseTest();
		String key1 = db.post("abc");
		String key2 = db.post("abc2");
		String key3 = db.post("xyz3");
		
//		byte[] byteArray = db.get(key1);
		
		try {
//			System.out.println("abc == " + new String(byteArray, "UTF-8"));
//			System.out.println("Current Timestamp from database: " + db.getCurrentTimeStamp());
			
//			List<Record> list = db.getNewEntries(1459371912867l);
//			
//			for (Record record : list) {
//				System.out.println(record.getKey() + " " + new String(record.getImage()) + " " + record.getTimestamp());
//			}
			
			List<Record> newEntries = new ArrayList<Record>();
			newEntries.add(new Record(key1, new String("zxc").getBytes(), System.currentTimeMillis()));
			newEntries.add(new Record(key2, new String("zxc1").getBytes(), System.currentTimeMillis()));
			newEntries.add(new Record(key3, new String("zxc2").getBytes(), System.currentTimeMillis()));
			db.putEntries(newEntries);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	
}
