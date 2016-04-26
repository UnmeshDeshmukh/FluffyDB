package test;

import client.api.impl.ImageClient;

public class DeleteTest {
	public static void main(String args[]) {
		ImageClient client;
		try {
			if (args.length < 2) {
				System.out.println("Usage: queue_conf key_to_image");
			}
			client = new ImageClient(args[0]);
			client.delete(args[1]);
			System.out.println("DELETE DONE");
			System.exit(0);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
