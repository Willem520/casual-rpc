package willem.weiyu.casual.server;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import willem.weiyu.casual.IHelloService;
import willem.weiyu.casual.annotation.CasualService;

/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/24 15:24
 */
@CasualService(IHelloService.class)
@Service
@Slf4j
public class ServerHelloService implements IHelloService {

    public String say(String message) {
        return "server received";
    }
}
