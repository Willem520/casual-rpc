package willem.weiyu.casual.server.handler;

import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.BeansException;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import willem.weiyu.casual.common.CasualRequest;
import willem.weiyu.casual.common.CasualResponse;

/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/24 10:55
 */
@Component
@ChannelHandler.Sharable
@Slf4j
public class CasualServerHandler extends SimpleChannelInboundHandler<CasualRequest> implements ApplicationContextAware {
    private ApplicationContext context;

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
        channelHandlerContext.writeAndFlush(casualResponse);
    }

    private Object handle(CasualRequest request) throws InvocationTargetException, ClassNotFoundException {
        Class<?> clazz = Class.forName(request.getClassName());
        Object serviceBean = context.getBean(clazz);
        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterType();
        Object[] parameters = request.getParameters();
        FastClass fastClass = FastClass.create(serviceClass);
        FastMethod fastMethod = fastClass.getMethod(methodName,parameterTypes);
        return fastMethod.invoke(serviceBean, parameters);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
