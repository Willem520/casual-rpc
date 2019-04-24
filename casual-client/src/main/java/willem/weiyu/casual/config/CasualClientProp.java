package willem.weiyu.casual.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/24 17:20
 */
@Data
@ConfigurationProperties(prefix = "casual.client")
public class CasualClientProp {
    private String host;
    private int port;
}
