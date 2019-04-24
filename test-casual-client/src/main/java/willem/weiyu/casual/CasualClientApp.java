package willem.weiyu.casual;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/24 15:47
 */
@SpringBootApplication
public class CasualClientApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CasualClientApp.class, args);
        IHelloService helloService = context.getBean(IHelloService.class);
        helloService.say("hello");
    }
}
