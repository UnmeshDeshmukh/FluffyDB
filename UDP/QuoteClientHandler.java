import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

public class QuoteClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {

	@Override
	protected void channelRead0(ChannelHandlerContext paramChannelHandlerContext, DatagramPacket packet)
			throws Exception {
		String response = packet.content().toString(CharsetUtil.UTF_8);
		if (response.startsWith("Quote")) {
			System.out.println("Quote:" + response.substring(5));
			paramChannelHandlerContext.writeAndFlush(
					new DatagramPacket(Unpooled.copiedBuffer("Quote", CharsetUtil.UTF_8), packet.sender()));
		}
	}

}
