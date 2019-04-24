package willem.weiyu.casual.client;

import java.net.InetSocketAddress;

import willem.weiyu.casual.common.CasualRequest;
import willem.weiyu.casual.common.CasualResponse;

/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/24 11:21
 */
public interface IClient {
    CasualResponse send(CasualRequest request);

    void connect(InetSocketAddress address);

    InetSocketAddress getInetSocketAddress();

    void close();
}
