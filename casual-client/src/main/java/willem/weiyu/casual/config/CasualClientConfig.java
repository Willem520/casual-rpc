package willem.weiyu.casual.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import willem.weiyu.casual.CasualServiceRegistry;
import willem.weiyu.casual.zookeeper.ZookeeperCasualServiceRegistry;

/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/24 11:24
 */
@Configuration
@EnableConfigurationProperties(CasualClientProp.class)
public class CasualClientConfig{
    @Value("${registry.address:2181}")
    private String zkAddress;

    @Bean
    public CasualServiceRegistry registry(){
        return new ZookeeperCasualServiceRegistry(zkAddress);
    }
}
