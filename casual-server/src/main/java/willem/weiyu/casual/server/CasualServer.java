package willem.weiyu.casual.server;

import java.net.InetAddress;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.Data;
import willem.weiyu.casual.CasualServiceRegistry;
import willem.weiyu.casual.config.CasualServerConfig;

/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/24 10:43
 */
@Data
@Component
public class CasualServer {
    @Autowired
    private CasualServerConfig config;

    @Autowired
    private CasualServerInitializer initializer;

    @Autowired
    private CasualServiceRegistry registry;

    private ServerBootstrap serverBootstrap;

    private Channel serverChannel;

    public void start() throws Exception {
        String serviceHost = InetAddress.getLocalHost().getHostAddress();
        int port = config.getCasualProperties().getPort();
        if (registry == null) {
            throw new RuntimeException("can not find registry");
        }
        if (config.getHandlerMap() != null && !config.getHandlerMap().isEmpty()) {
            for (String serviceName :
                    config.getHandlerMap().keySet()) {
                registry.register(serviceName, serviceHost + ":" + port);
            }
        }
        serverBootstrap = new ServerBootstrap().group(bossGroup(), workerGroup())
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(initializer);
        serverChannel = serverBootstrap.bind(serviceHost, port).sync().channel().closeFuture().channel();
    }

    @Bean(destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup() {
        return new NioEventLoopGroup();
    }

    @Bean(destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup();
    }

    @PreDestroy
    public void stop() {
        if (serverChannel != null) {
            serverChannel.parent().close();
            serverChannel.close();
        }
        if (serverBootstrap != null) {
            serverBootstrap.clone();
        }
    }
}
