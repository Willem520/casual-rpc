package willem.weiyu.casual.client;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
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
 * @Date 2019/4/24 11:42
 */
public class CasualClient implements IClient{
    private EventLoopGroup eventLoopGroup;
    private Channel channel;
    private CasualClientInitializer initializer;
    private CasualClientHandler handler;
    private String host;
    private int port;

    public CasualClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    @Override
    public CasualResponse send(CasualRequest request) {
        try {
            channel.writeAndFlush(request).await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return handler.getRpcResponse(request.getRequestId());
    }

    @Override
    public void connect(InetSocketAddress address) {
        handler = new CasualClientHandler();
        initializer = new CasualClientInitializer(handler);
        eventLoopGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(initializer);
        try {
            channel = bootstrap.connect(address).sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public InetSocketAddress getInetSocketAddress() {
        return new InetSocketAddress(host,port);
    }

    @Override
    public void close() {
        eventLoopGroup.shutdownGracefully();
        channel.closeFuture().syncUninterruptibly();
    }
}
