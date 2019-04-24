package willem.weiyu.casual.config;

import java.util.Set;

import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import willem.weiyu.casual.annotation.CasualInterface;
import willem.weiyu.casual.proxy.CasualProxyFactory;

/**
 * @Author weiyu
 * @Description
 * @Date 2019/4/24 11:24
 */
@Configuration
@Slf4j
public class CasualClientConfig implements ApplicationContextAware, InitializingBean {
    private ApplicationContext context;

    @Override
    public void afterPropertiesSet() throws Exception {
        Reflections reflections = new Reflections("willem.weiyu.casual");
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(CasualInterface.class);
        for (Class<?> clazz : typesAnnotatedWith){
            beanFactory.registerSingleton(clazz.getSimpleName(), CasualProxyFactory.create(clazz));
        }
        log.info("afterPropertiesSet is {}", typesAnnotatedWith);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
