package gash.router.client.api;

public interface ClientAPI {

	
	void get(String key, String outputPath);

	void put(String key, String imagePath);

	String post(String imagePath);

	void delete(String key);	
}
