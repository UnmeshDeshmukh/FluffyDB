package gash.router.client;

import gash.router.client.api.ImageClient;

public class DemoClient {	
	
	public static void main(String args[]) {		
		 ImageClient client = new ImageClient();
		 
		 String key = client.post("/home/faisal/image1.jpg");
		 System.out.println("post done");
     	 client.get(key, "/home/src");
		 System.out.println("get done");
		 client.put(key, "/home/faisal/image1.jpg");
		 System.out.println("update done");
		 client.delete(key);
		 System.out.println("delete done");
		 
		 
		
	}
}
