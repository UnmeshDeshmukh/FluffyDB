package client.api;

public interface ImageClientAPI {
	
	void get(String key, String outputPath, String inamgeName);

	void put(String key, String imagePath);

	String post(String imagePath);

	void delete(String key);	
}
