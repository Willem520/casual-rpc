package willem.weiyu.casual;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import willem.weiyu.casual.server.CasualServer;

/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/24 14:17
 */
@SpringBootApplication
public class CasualServerApp {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(CasualServerApp.class, args);
        CasualServer server = context.getBean(CasualServer.class);
        server.start();
    }
}
