package test;

import client.api.impl.ImageClient;

public class PutTest {
	public static void main(String args[]) {
		ImageClient client;
		try {
			if (args.length < 3) {
				System.out.println("queue_conf key Input_image_path");
				System.exit(0);
			}
			client = new ImageClient(args[0]);
			client.put(args[1], args[2]);
			System.out.println("PUT DONE");
			System.exit(0);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
