package test;

import client.api.impl.ImageClient;

public class GetTest {
	
	public static void main(String[] args) {
		try {
			if (args.length < 4) {
				System.out.println("Arguments: queue_conf key output_folder imagename");
				System.exit(0);
			}
			ImageClient client = new ImageClient(args[0]);
			client.get(args[1], args[2], args[3]);
			System.out.println("GET DONE");
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
