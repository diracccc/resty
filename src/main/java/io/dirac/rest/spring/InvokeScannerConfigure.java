package io.dirac.rest.spring;

import io.dirac.rest.annotation.RemoteService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

public class InvokeScannerConfigure implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {
    private ApplicationContext applicationContext;
    private String basePackage;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        ClassPathInvokeScanner scanner = new ClassPathInvokeScanner(registry);
        scanner.setBasePackage(basePackage);
        scanner.setAnnotationClass(RemoteService.class);
        scanner.setBeanClass(InvokeFactoryBean.class);
        scanner.setResourceLoader(applicationContext);
        scanner.registerFilters();
        scanner.scan(StringUtils.tokenizeToStringArray(basePackage, ",; \t\n"));
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
    }
}
