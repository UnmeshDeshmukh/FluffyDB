package client.api.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.ShutdownSignalException;

import client.api.ImageClientAPI;
import client.queue.ClientQueueService;
import common.ConfigurationReader;
import common.ImageReader;
import raft.proto.ImageTransfer;

public class ImageClient implements ImageClientAPI{
	
	ClientQueueService queue = null;
	
	public ImageClient(String fileName) throws Exception {
		if (fileName == null) {
			throw new Exception("Queue Configurataion file not found");
		}
		
		ConfigurationReader.getInstance().loadProperties(new File(fileName));
		queue = ClientQueueService.getInstance();
	}
	
	@Override
	public void get(String key, String outputPath, String imageName) {
		try {
			byte[] byteArray = queue.getMessage(key);
			if (byteArray == null)
				return;
			BufferedImage imag = ImageIO.read(new ByteArrayInputStream(byteArray));
			ImageIO.write(imag, "jpg", new File(outputPath, imageName));

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
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
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
