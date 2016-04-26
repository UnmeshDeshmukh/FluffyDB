package adapter.client;

import adapter.server.AdapterInit;
import adapter.server.AdapterUtils;
import adapter.server.proto.Global;
import deven.monitor.server.MonitorInit;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class AdapterClientAPI {
	
	static String host;
	static int port;
	static ChannelFuture channel;
	
	 static EventLoopGroup group;
	

	
	public static void init(String host_received, int port_received)
	{
		host = host_received;
		port = port_received;
		group = new NioEventLoopGroup();
		try {
			AdapterInit si = new AdapterInit(false);
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).handler(si);
			b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
			b.option(ChannelOption.TCP_NODELAY, true);
			b.option(ChannelOption.SO_KEEPALIVE, true);

			// Make the connection attempt.
			 channel = b.connect(host, port).syncUninterruptibly();

			
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

	
	
	
	public static void get(String key){
		
		Global.GlobalCommandMessage  getRequest = AdapterUtils.prepareClusterRouteRequestForGET(0, key);
		
		channel.channel().writeAndFlush(getRequest);
		
		
	}
	
	public static void post(byte[] image){
		Global.GlobalCommandMessage  postRequest = AdapterUtils.prepareClusterRouteRequestForPOST(0, image);
		channel.channel().writeAndFlush(postRequest);
		
	}
	

	public static void main(String[] args) {
		
		String host = "127.0.0.1";
		int port = 4000;
		
		AdapterClientAPI.init(host, port);
		
		AdapterClientAPI.get("ec1a8dfa-5a90-4e13-9101-c92ebe6611f5");
		//AdapterClientAPI.post("vinit_adapter".getBytes());;
	
		while(true){
			
		}
		// TODO Auto-generated method stub
		
	}

}
