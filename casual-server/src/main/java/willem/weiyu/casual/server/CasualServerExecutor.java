package willem.weiyu.casual.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/25 16:02
 */
@Component
public class CasualServerExecutor implements ApplicationRunner {
    @Autowired
    private CasualServer casualServer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        casualServer.start();
    }
}
