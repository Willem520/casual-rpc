package willem.weiyu.casual;

import willem.weiyu.casual.client.CasualClient;
import willem.weiyu.casual.client.IClient;
import willem.weiyu.casual.common.CasualRequest;
import willem.weiyu.casual.common.CasualResponse;

/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/24 11:39
 */
public class Transporter {

    public static CasualResponse send(CasualRequest request){
        IClient client = new CasualClient("127.0.0.1", 8080);
        client.connect(client.getInetSocketAddress());
        return client.send(request);
    }
}
