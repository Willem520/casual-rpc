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
        eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(65535,0,4))
                                .addLast(new CasualEncoder(CasualRequest.class, new JsonSerializer()))
                                .addLast(new CasualDecoder(CasualResponse.class, new JsonSerializer()))
                                .addLast(handler);
                    }
                });
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
