package willem.weiyu.casual.client.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;
import willem.weiyu.casual.DefaultFuture;
import willem.weiyu.casual.common.CasualRequest;
import willem.weiyu.casual.common.CasualResponse;

/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/24 11:44
 */
@Slf4j
public class CasualClientHandler extends ChannelDuplexHandler {

    private final Map<String, DefaultFuture> futureMap = new ConcurrentHashMap();

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof CasualRequest) {
            CasualRequest request = (CasualRequest) msg;
            futureMap.putIfAbsent(request.getRequestId(), new DefaultFuture());
        }
        super.write(ctx, msg, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("receive response:{}",msg);
        if (msg instanceof CasualResponse) {
            CasualResponse response = (CasualResponse) msg;
            DefaultFuture defaultFuture = futureMap.get(response.getRequestId());
            defaultFuture.setResponse(response);
        }
        super.channelRead(ctx, msg);
    }

    public CasualResponse getRpcResponse(String requestId) {
        try {
            DefaultFuture defaultFuture = futureMap.get(requestId);
            return defaultFuture.getResponse(10);
        } finally {
            futureMap.remove(requestId);
        }
    }

}
