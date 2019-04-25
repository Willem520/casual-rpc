package willem.weiyu.casual;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import willem.weiyu.casual.proxy.CasualProxy;

/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/25 17:50
 */
@Component
public class CasualClientExecutor implements ApplicationRunner {
    private ApplicationContext context;
    @Autowired
    private CasualProxy proxy;

    public void run(ApplicationArguments args) throws Exception {
        IHelloService service = proxy.create(IHelloService.class);
        service.say("casual-client");
    }
}
