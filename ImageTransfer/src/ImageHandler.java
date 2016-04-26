
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.imageio.ImageIO;
import javax.imageio.stream.IIOByteBuffer;
import javax.imageio.stream.ImageInputStream;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ImageHandler extends SimpleChannelInboundHandler<ImageTransfer.ImageMsg> {

	public ByteBuffer byteBuffer;

	@Override
	public void exceptionCaught(ChannelHandlerContext arg0, Throwable arg1) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void handlerAdded(ChannelHandlerContext arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void handlerRemoved(ChannelHandlerContext arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ImageTransfer.ImageMsg imageMsg) throws Exception {
		try {
			System.out.println("Handler -- channelRead0 --- start");

			// System.out.write(imageMsg.getImageData().toByteArray());

			BufferedImage imag = ImageIO.read(new ByteArrayInputStream(imageMsg.getImageData().toByteArray()));

			// System.out.println(byteBuffer.array());

			ImageIO.write(imag, "jpg", new File("/home/vinit/workspace/ImageTransfer/src", "snap3.jpg"));

			System.out.println("Handler -- channelRead---- ended");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized ByteBuffer getByteBuffer(ImageTransfer.ImageMsg imageMsg) {
		imageMsg.getImageData().copyTo(byteBuffer);
		return byteBuffer;
	}
}
