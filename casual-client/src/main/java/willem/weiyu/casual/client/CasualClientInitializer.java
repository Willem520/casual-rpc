package willem.weiyu.casual.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import willem.weiyu.casual.client.handler.CasualClientHandler;
import willem.weiyu.casual.common.CasualRequest;
import willem.weiyu.casual.common.CasualResponse;
import willem.weiyu.casual.protocol.CasualDecoder;
import willem.weiyu.casual.protocol.CasualEncoder;
import willem.weiyu.casual.protocol.JsonSerializer;

/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/24 16:37
 */
public class CasualClientInitializer extends ChannelInitializer<SocketChannel> {

    private CasualClientHandler handler;

    public CasualClientInitializer(CasualClientHandler handler){
        this.handler = handler;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535,0,4))
                .addLast(new CasualEncoder(CasualRequest.class, new JsonSerializer()))
                .addLast(new CasualDecoder(CasualResponse.class, new JsonSerializer()))
                .addLast(handler);
    }
}
