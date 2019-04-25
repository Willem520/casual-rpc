package willem.weiyu.casual.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import willem.weiyu.casual.CasualServiceRegistry;
import willem.weiyu.casual.client.CasualClient;
import willem.weiyu.casual.client.IClient;
import willem.weiyu.casual.common.CasualRequest;

/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/24 11:32
 */
@Component
public class CasualProxy {
    @Autowired
    private CasualServiceRegistry registry;

    public <T> T create(Class<T> interfaceClass){
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] {interfaceClass},
                (proxy, method, args) -> {
                    CasualRequest request = new CasualRequest();
                    String requestId = UUID.randomUUID().toString();
                    String className = method.getDeclaringClass().getName();
                    String methodName = method.getName();
                    Class<?>[] parameterTypes = method.getParameterTypes();

                    request.setRequestId(requestId);
                    request.setInterfaceName(className);
                    request.setMethodName(methodName);
                    request.setParameterType(parameterTypes);
                    request.setParameters(args);

                    if (registry == null){
                        throw new RuntimeException("can not find registry");
                    }
                    String serviceName = interfaceClass.getName();
                    String serviceAddress = registry.discover(serviceName);
                    String host = serviceAddress.split(":")[0];
                    int port = Integer.parseInt(serviceAddress.split(":")[1]);
                    IClient client = new CasualClient(host, port);
                    client.connect(client.getInetSocketAddress());
                    return client.send(request).getResult();
                });
    }
}
