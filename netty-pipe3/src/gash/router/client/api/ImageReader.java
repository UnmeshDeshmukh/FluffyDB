package gash.router.client.api;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.protobuf.ByteString;

public class ImageReader {

	private BufferedImage img;
	private ByteString byteString;

	public ImageReader(String path) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			img = ImageIO.read(new File(path));
			ImageIO.write(img, "jpeg", baos);
			baos.flush();
			byteString = ByteString.copyFrom(baos.toByteArray());
			baos.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the img
	 */
	public BufferedImage getImg() {
		return img;
	}

	/**
	 * @param img
	 *            the img to set
	 */
	public void setImg(BufferedImage img) {
		this.img = img;
	}

	public ByteString getByteString() {
		return byteString;

	}

}
