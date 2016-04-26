package common;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import server.db.DatabaseService;

public class ConfigurationReader {
		
	private String queueURL = null;
	private String databaseURL = null;
	private String username = null;
	private String password = null;
	private String dbType = null;
	private String monitor_host = null;
	private Integer monitor_port = null;
	
	private int queuePort = 0;
	private Properties props = new Properties();
	
	private static ConfigurationReader instance = null;
	
	public static ConfigurationReader getInstance() {
		if (instance == null) {
			instance = new ConfigurationReader();
		}
		return instance;
	}
	
	private ConfigurationReader() {
		
	}

	public void loadProperties(File file) {
		InputStream is;
		try {
			is = new FileInputStream(file);
			props.load(is);			
			queueURL = (String) props.get(SystemConstants.QUEUE_URL);
			if (props.get(SystemConstants.QUEUE_PORT) != null) {
				queuePort = Integer.parseInt((String)props.get(SystemConstants.QUEUE_PORT));
			}
			
			if (props.get(SystemConstants.DB_TYPE) != null) {
				dbType = (String) props.get(SystemConstants.DB_TYPE);
				username = (String) props.get(SystemConstants.DB_USERNAME);
				password = (String) props.get(SystemConstants.DB_PASSWORD);
				databaseURL = (String) props.get(SystemConstants.DATABASE_URL);
				DatabaseService.getInstance().dbConfiguration(dbType, databaseURL, username, password);
			}
			
			if (props.get(SystemConstants.MONITOR_HOST) != null)  {
				monitor_host = (String) props.get(SystemConstants.MONITOR_HOST);
				if (props.get(SystemConstants.MONITOR_PORT) != null) {
					monitor_port = Integer.parseInt((String)props.get(SystemConstants.MONITOR_PORT));
				}
			};
			

			
		} catch (IOException e) {
			e.printStackTrace();
		}	    
	}

	public String getMonitorHost() {
		return monitor_host;
	}

	public Integer getMonitorPort() {
		return monitor_port;
	}

	public String getQueueURL() {
		return queueURL;
	}

	public int getQueuePort() {
		return queuePort;
	}
	
}
