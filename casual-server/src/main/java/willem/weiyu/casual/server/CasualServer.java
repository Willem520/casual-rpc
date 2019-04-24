package willem.weiyu.casual.server;

import java.net.InetSocketAddress;

import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import lombok.Data;

/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/24 10:43
 */
@Data
@Component
public class CasualServer {
    private final ServerBootstrap serverBootstrap;

    private final InetSocketAddress address;

    private Channel serverChannel;

    public CasualServer(ServerBootstrap serverBootstrap, InetSocketAddress address){
        this.serverBootstrap = serverBootstrap;
        this.address = address;
    }

    public void start() throws InterruptedException {
        serverChannel = serverBootstrap.bind(address).sync().channel().closeFuture().channel();
    }

    @PreDestroy
    public void stop(){
        if (serverChannel != null){
            serverChannel.close();
            serverChannel.parent().close();
        }
    }
}
