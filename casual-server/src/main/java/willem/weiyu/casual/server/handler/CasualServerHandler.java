package willem.weiyu.casual.server.handler;

import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import willem.weiyu.casual.common.CasualRequest;
import willem.weiyu.casual.common.CasualResponse;
import willem.weiyu.casual.config.CasualServerConfig;

/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/24 10:55
 */
@Component
@ChannelHandler.Sharable
@Slf4j
public class CasualServerHandler extends SimpleChannelInboundHandler<CasualRequest>{
    @Autowired
    private CasualServerConfig config;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, CasualRequest casualRequest)
            throws Exception {
        log.info("receive request:{}",casualRequest);
        CasualResponse casualResponse = new CasualResponse();
        casualResponse.setRequestId(casualRequest.getRequestId());
        try {
            Object result = handle(casualRequest);
            casualResponse.setResult(result);
        } catch (Exception e) {
            casualResponse.setThrowable(e);
            e.printStackTrace();
        }
        channelHandlerContext.writeAndFlush(casualResponse).addListener(ChannelFutureListener.CLOSE);
    }

    private Object handle(CasualRequest request) throws InvocationTargetException, ClassNotFoundException {
        String serviceName = request.getInterfaceName();
        String version = request.getVersion();
        if (!StringUtils.isEmpty(version)){

        }
        Object serviceBean = config.getHandlerMap().get(serviceName);
        if (serviceBean == null){
            throw new RuntimeException(String.format("can not find service bean by key: %s", serviceName));
        }
        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterType();
        Object[] parameters = request.getParameters();
        FastClass fastClass = FastClass.create(serviceClass);
        FastMethod fastMethod = fastClass.getMethod(methodName,parameterTypes);
        return fastMethod.invoke(serviceBean, parameters);
    }
}
