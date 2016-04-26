package edu.sjsu.projectfluffy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MainClass {
	Properties configFile = new Properties();
	
	public MainClass(String fileName) throws IOException {
		if (fileName == null) {
			usage();
			return;
		}
		InputStream input = new FileInputStream(new File(fileName));		
		configFile.load(input);
	}	
	
	public static void main(String[] args) {
		try {
			MainClass engine = new MainClass(args[0]);
			engine.start();
		} catch (IOException e) {
			e.printStackTrace();
		}					
	}
	
	public void start() {
		//TODO node creation code
	}
	
	
	public void usage() {
		System.out.println("usage: server <config file>");		
	}
}
