package test;

import client.api.impl.ImageClient;

public class DemoClient {	
	
	public static void main(String args[]) {			 
		 ImageClient client;
		try {
			client = new ImageClient(args[0]);
			 String key = client.post("/home/jagruti/Pictures/i3.png");
			 System.out.println("POST DONE");
	     	 
			 client.get(key, "/home/jagruti/Pictures/Output", "jagruti1.png");
			 System.out.println("GET DONE");
			 client.put(key, "/home/jagruti/Pictures/200_radis.png");
			 System.out.println("UPDATE DONE");
			 client.get(key, "/home/jagruti/Pictures/Output" , "updated1.png");
//			 client.delete(key);
			 System.out.println("delete done");

		} catch (Exception e) {
			e.printStackTrace();
		}		 		 		 		
	}
}
