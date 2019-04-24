package willem.weiyu.casual.config;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import willem.weiyu.casual.server.CasualServerInitializer;
import willem.weiyu.casual.server.ChannelRepository;

/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/24 10:41
 */
@Configuration
@EnableConfigurationProperties(CasualProperties.class)
public class CasualConfig {
    @Autowired
    private CasualProperties casualProperties;

    @Autowired
    private CasualServerInitializer initializer;

    @Bean(name = "serverBootstrap")
    public ServerBootstrap bootstrap(){
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup(), workerGroup())
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(initializer);

        Map<ChannelOption<?>, Object> channelOptionMap = channelOptions();
        Set<ChannelOption<?>> keySet = channelOptionMap.keySet();
        for (ChannelOption option : keySet){
            serverBootstrap.option(option, channelOptionMap.get(option));
        }
        return serverBootstrap;
    }

    @Bean(destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup(){
        return new NioEventLoopGroup();
    }

    @Bean(destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workerGroup(){
        return new NioEventLoopGroup();
    }

    @Bean
    public Map<ChannelOption<?>, Object> channelOptions(){
        Map<ChannelOption<?>, Object> options = new HashMap();
        options.put(ChannelOption.SO_BACKLOG, casualProperties.getBacklog());
        return options;
    }

    @Bean
    public InetSocketAddress socketAddress(){
        return new InetSocketAddress(casualProperties.getPort());
    }

    @Bean
    public ChannelRepository channelRepository(){
        return new ChannelRepository();
    }
}
