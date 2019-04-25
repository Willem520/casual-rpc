package willem.weiyu.casual.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import willem.weiyu.casual.CasualServiceRegistry;
import willem.weiyu.casual.annotation.CasualService;
import willem.weiyu.casual.zookeeper.ZookeeperCasualServiceRegistry;

/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/24 10:41
 */
@Data
@Configuration
@EnableConfigurationProperties(CasualServerProp.class)
public class CasualServerConfig implements ApplicationContextAware {
    @Value("${registry.address:localhost:2181}")
    private String zkAddress;
    @Autowired
    private CasualServerProp casualProperties;

    /**
     * 存放 服务名 与 服务对象 之间的映射关系
     */
    private Map<String, Object> handlerMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String,Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(CasualService.class);
        if (serviceBeanMap != null && !serviceBeanMap.isEmpty()){
            for (Object serviceBean :
                    serviceBeanMap.values()) {
                CasualService casualService = serviceBean.getClass().getAnnotation(CasualService.class);
                String serviceName = casualService.value().getName();
                String serviceVersion = casualService.version();
                if (serviceVersion != null && serviceVersion.replace(" ","").length() > 0) {
                    serviceName += "-" + serviceVersion;
                }
                handlerMap.put(serviceName, serviceBean);
            }
        }
    }

    @Bean
    public CasualServiceRegistry registry(){
        return new ZookeeperCasualServiceRegistry(zkAddress);
    }
}
