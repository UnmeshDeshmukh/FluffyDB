
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

public final class QuoteClient {

    static final int PORT = Integer.parseInt(System.getProperty("port", "5000"));

    public static void main(String[] args) throws Exception {

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioDatagramChannel.class)
             .handler(new QuoteClientHandler())
             .option(ChannelOption.SO_BROADCAST, true)
             .option(ChannelOption.SO_REUSEADDR, true)
             .option(ChannelOption.IP_MULTICAST_LOOP_DISABLED, false)
             .option(ChannelOption.SO_RCVBUF, 2048)
             .option(ChannelOption.IP_MULTICAST_TTL, 255);
          

            Channel ch = b.bind(0).sync().channel();
            
            // Broadcast the QOTM request to port 8080.
            ch.writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer("Quote 3333", CharsetUtil.UTF_8),
                    new InetSocketAddress("255.255.255.255", PORT))).sync();

            
            // QuoteOfTheMomentClientHandler will close the DatagramChannel when a
            // response is received.  If the channel is not closed within 5 seconds,
            // print an error message and quit.
           
            if (!ch.closeFuture().await(5000)) {
                System.err.println("Quote request timed out.");
            }
        } finally {
            group.shutdownGracefully();
        }
    }
}