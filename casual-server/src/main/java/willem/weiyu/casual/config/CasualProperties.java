package willem.weiyu.casual.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/24 10:39
 */
@Data
@ConfigurationProperties(prefix = "casual")
public class CasualProperties {
    private int port = 8080;
    private int bossCount = 4;
    private int workerCount = 16;
    private boolean keepAlive = true;
    private int backlog;
}
