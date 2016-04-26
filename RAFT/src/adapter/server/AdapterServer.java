package adapter.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

import common.ConfigurationReader;
import deven.monitor.server.MonitorInit;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import router.container.RoutingConf;
import server.MessageServer;
import server.MessageServer.JsonUtil;

public class AdapterServer {
	protected static AdapterServerConf conf;
	protected static ServerBootstrap b;
	private static void init(File cfg) {
		if (!cfg.exists())
			throw new RuntimeException(cfg.getAbsolutePath() + " not found");
		// resource initialization - how message are processed
		BufferedInputStream br = null;
		try {
			byte[] raw = new byte[(int) cfg.length()];
			br = new BufferedInputStream(new FileInputStream(cfg));
			br.read(raw);
			conf = JsonUtil.decode(new String(raw), AdapterServerConf.class);
			if (!verifyConf(conf))
				throw new RuntimeException("verification of configuration failed");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		//LEADER ELECTION
	//	NodeState.getInstance().setState(NodeState.FOLLOWER);
		
		
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		if (args.length == 0) {
			System.out.println("usage: server <config file>");
			System.exit(1);
		}
		
		if (args.length < 2) {
			usage();
			return;
		}
		File adaptercf = new File(args[0]);
		File queueConf = new File(args[1]);
		try {
//			MessageServer svr = new MessageServer(cf ,queueConf);
//			svr.startServer();
			
			init(adaptercf);
			ConfigurationReader.getInstance().loadProperties(queueConf);
			System.out.println("This is the adaper connection:"+AdapterServer.conf.getRouting().get(0).getId());
			System.out.println("This is the Q URL--"+ConfigurationReader.getInstance().getQueueURL());
			AdapterServer.startServer();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("server closing");
		}
	}
	private static void usage() {
		System.out.println("java -jar <jar-path> routing.conf queue.conf");
		
	}
	private static boolean verifyConf(AdapterServerConf conf) {
		return (conf != null);
	}
	public static void startServer() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			b = new ServerBootstrap();

			b.group(bossGroup, workerGroup);
			b.channel(NioServerSocketChannel.class);
			b.option(ChannelOption.SO_BACKLOG, 100);
			b.option(ChannelOption.TCP_NODELAY, true);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			// b.option(ChannelOption.MESSAGE_SIZE_ESTIMATOR);

			boolean compressComm = false;
			b.childHandler(new AdapterInit(compressComm));

			// Start the server.
			System.out.println("Starting monitor server , listening on port = "
					+ conf.getWorkPort());
			ChannelFuture f = b.bind(conf.getWorkPort()).syncUninterruptibly();

			System.out.println(f.channel().localAddress() + " -> open: " + f.channel().isOpen() + ", write: "
					+ f.channel().isWritable() + ", act: " + f.channel().isActive());

			// block until the server socket is closed.
			f.channel().closeFuture().sync();

		} catch (Exception ex) {
			// on bind().sync()
			System.out.println("Failed to setup handler."+ ex.toString());
		} finally {
			// Shut down all event loops to terminate all threads.
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	
	public static class JsonUtil {
		private static JsonUtil instance;

		public static void init(File cfg) {

		}

		public static JsonUtil getInstance() {
			if (instance == null)
				throw new RuntimeException("Server has not been initialized");

			return instance;
		}
		
		
				public static String encode(Object data) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				return mapper.writeValueAsString(data);
			} catch (Exception ex) {
				return null;
			}
		}

		public static <T> T decode(String data, Class<T> theClass) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				return mapper.readValue(data.getBytes(), theClass);
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}
	}
	
	

}
