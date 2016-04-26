package test;

import client.api.impl.ImageClient;

public class TestClientAPI {
	
	public static void main(String args[]) {
		try {
			ImageClient client = new ImageClient(args[0]);
			
			if (args[1].equalsIgnoreCase("GET")) {
				client.get(args[2], args[3], args[4]);
				System.out.println("GET DONE");
			} else if (args[1].equalsIgnoreCase("PUT")) {
				client.put(args[2], args[3]);
				System.out.println("PUT DONE");
			} else if (args[1].equalsIgnoreCase("POST")) {
				String key = client.post(args[2]);
				System.out.println("POST DONE, Reference Key : " + key);
			}else if (args[1].equalsIgnoreCase("DELETE")) {
				client.delete(args[2]);
				System.out.println("DELETE DONE");
			}
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
