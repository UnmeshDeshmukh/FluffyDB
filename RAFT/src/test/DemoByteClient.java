package test;

import client.api.impl.ByteClient;

public class DemoByteClient {
	public static void main(String args[]) {			 
		 ByteClient client;
		try {
			client = new ByteClient(args[0]);
			String key = client.post("MMM".getBytes());
			System.out.println("POST DONE : " + key);

			String key1 = client.post("YYY".getBytes());
			System.out.println("POST DONE : " + key1);

			byte[] data = client.get("ZZZ");
			System.out.println(new String(data));
			System.out.println("GET DONE" + data);
			
			String key2 = client.post("TTT".getBytes());
			System.out.println("POST DONE : " + key2);

			byte[] updated = client.get(key);
			System.out.println("Before update" + new String(updated));

//			String key2 = client.post("TTT".getBytes());
//			System.out.println("Key2:" + key2);
//			client.put(key2, "NEWMMM".getBytes());
//			System.out.println("Key2:" + key2);
//			byte[] updated1 = client.get(key2);
//			System.out.println("After update" + new String(updated1));

			System.out.println("PUT DONE : "+ new String(updated));
			
			
			client.put(key2, "TTTX".getBytes());
	     	byte[] updated2 = client.get(key2);
			System.out.println("UPDATE DONE : " + new String(updated2));

	     	client.delete(key2);
	     	byte[] updated1 = client.get(key2);
			System.out.println("DELETE DONE : " + updated1);

		} catch (Exception e) {
			e.printStackTrace();
		}		 		 		 		
		System.exit(0);
	}
}
