package server;

import common.ConfigurationReader;
import router.container.RoutingConf;
import server.edges.EdgeMonitor;

public class ServerState {
	private RoutingConf conf;
	private EdgeMonitor emon;
	private ConfigurationReader queueConf = ConfigurationReader.getInstance();
//	private TaskList tasks;


	
	public RoutingConf getConf() {
		return conf;
	}

	public void setConf(RoutingConf conf) {
		this.conf = conf;
	}

	public EdgeMonitor getEmon() {
		return emon;
	}

	public void setEmon(EdgeMonitor emon) {
		this.emon = emon;
	}
	
	public ConfigurationReader getQueueConf() {
		return queueConf;
	}

	/*public TaskList getTasks() {
		return tasks;
	}*/
/*
	public void setTasks(TaskList tasks) {
		this.tasks = tasks;
	}*/
}
