package test;

import client.api.impl.ImageClient;

public class PostTest {
	public static void main(String args[]) {
		ImageClient client;
		try {
			if (args.length < 2) {
				System.out.println("Arguements: queue_conf key");
				System.exit(0);
			}
			
			client = new ImageClient(args[0]);
			String key = client.post(args[1]);
			System.out.println("POST DONE, Reference Key : " + key);
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
