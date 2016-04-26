package client.api;

public interface ByteClientAPI {
	
	byte[] get(String key);

	void put(String key, byte[] image);

	String post(byte[] image);

	void delete(String key);	
}
