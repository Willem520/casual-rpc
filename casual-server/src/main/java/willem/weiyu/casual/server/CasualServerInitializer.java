package willem.weiyu.casual.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import willem.weiyu.casual.common.CasualRequest;
import willem.weiyu.casual.common.CasualResponse;
import willem.weiyu.casual.protocol.CasualDecoder;
import willem.weiyu.casual.protocol.CasualEncoder;
import willem.weiyu.casual.protocol.JsonSerializer;
import willem.weiyu.casual.server.handler.CasualServerHandler;

/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/24 10:54
 */
@Component
public class CasualServerInitializer extends ChannelInitializer<SocketChannel> {
    @Autowired
    private CasualServerHandler handler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 4))
        .addLast(new CasualEncoder(CasualResponse.class, new JsonSerializer()))
        .addLast(new CasualDecoder(CasualRequest.class, new JsonSerializer()))
        .addLast(handler);
    }
}
