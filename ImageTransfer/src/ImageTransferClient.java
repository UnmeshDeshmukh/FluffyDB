import java.awt.Image;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ImageTransferClient {

	public static final String host = "localhost";
	public static final int port = 8082;

	public static void main(String[] args) {

		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();

		b.group(group).channel(NioSocketChannel.class).handler(new ImageInit());
		b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
		b.option(ChannelOption.TCP_NODELAY, true);
		b.option(ChannelOption.SO_KEEPALIVE, true);

		ChannelFuture cf = b.connect(host, port).syncUninterruptibly();
		ImageReader imageReader = new ImageReader("/home/vinit/workspace/ImageTransfer/src/image1.jpeg");

		ImageTransfer.ImageMsg.Builder imageMsg = ImageTransfer.ImageMsg.newBuilder();
		imageMsg.setVersion(1);
		imageMsg.setImageData(imageReader.getByteString());

		cf.channel().writeAndFlush(imageMsg);
		cf.channel().closeFuture();
	}

}
