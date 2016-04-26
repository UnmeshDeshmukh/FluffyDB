package adapter.server;

import adapter.server.proto.Global;
import deven.monitor.client.MonitorClient.ClientClosedListener;
import deven.monitor.server.MonitorInit;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class AdapterClient {

	private static EventLoopGroup group;

	public static void sendGlobalCommandMessage(Global.GlobalCommandMessage globalCommandMessage, String host,
			int port) {

		System.out.println("--> initializing connection to " + host + ":" + port);

		group = new NioEventLoopGroup();
		try {
			AdapterInit si = new AdapterInit(false);
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).handler(si);
			b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
			b.option(ChannelOption.TCP_NODELAY, true);
			b.option(ChannelOption.SO_KEEPALIVE, true);

			// Make the connection attempt.
			ChannelFuture channel = b.connect(host, port).syncUninterruptibly();

			channel.channel().writeAndFlush(globalCommandMessage);
			// want to monitor the connection to the server s.t. if we loose the
			// connection, we can try to re-establish it.
			// ClientClosedListener ccl = new ClientClosedListener(this);
			// channel.channel().closeFuture().addListener(ccl);

			System.out.println(channel.channel().localAddress() + " -> open: " + channel.channel().isOpen()
					+ ", write: " + channel.channel().isWritable() + ", reg: " + channel.channel().isRegistered());

		} catch (Throwable ex) {
			System.out.println("failed to initialize the client connection " + ex.toString());
			ex.printStackTrace();
		}

	}

}
