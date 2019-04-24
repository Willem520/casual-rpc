package willem.weiyu.casual.proxy;

import java.lang.reflect.Proxy;

/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/24 11:32
 */
public class CasualProxyFactory {

    public static <T> T create(Class<T> interfaceClass){
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),new Class<?>[]{interfaceClass},
                new CasualInvoker<T>(interfaceClass));
    }
}
