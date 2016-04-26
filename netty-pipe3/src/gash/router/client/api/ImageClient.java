package gash.router.client.api;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.ShutdownSignalException;

import gash.Image.ImageTransfer;
import gash.router.client.api.impl.ClientQueueService;

public class ImageClient implements ClientAPI{
	
	ClientQueueService queue = ClientQueueService.getInstance();
	
	@Override
	public void get(String key, String outputPath) {
		try {
			byte[] byteArray = queue.getMessage(key);
			BufferedImage imag = ImageIO.read(new ByteArrayInputStream(byteArray));
			ImageIO.write(imag, "jpg", new File(outputPath, "snap3.jpg"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void put(String key, String imagePath) {
		try {
			ImageReader imageReader = new ImageReader(imagePath);
			ImageTransfer.ImageMsg.Builder imageMsg = ImageTransfer.ImageMsg.newBuilder();
			imageMsg.setVersion(1);
			imageMsg.setImageData(imageReader.getByteString());
			queue.putMessage(key, imageMsg.build());
		} catch (IOException e) {
			e.printStackTrace();
		}catch (ConsumerCancelledException e) {
			e.printStackTrace();
		} catch (ShutdownSignalException e) {
			e.printStackTrace();
		}  
	}

	@Override
	public String post(String imagePath) {
		try {
			ImageReader imageReader = new ImageReader(imagePath);

			ImageTransfer.ImageMsg.Builder imageMsg = ImageTransfer.ImageMsg.newBuilder();
			imageMsg.setVersion(1);
			imageMsg.setImageData(imageReader.getByteString());
			return queue.postMessage(imageMsg.build());
		} catch (ShutdownSignalException e) {
			e.printStackTrace();
		} catch (ConsumerCancelledException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void delete(String key) {
		try {
			queue.deleteMessage(key);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
