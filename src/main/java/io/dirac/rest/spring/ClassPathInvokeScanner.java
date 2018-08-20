package io.dirac.rest.spring;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.util.Set;

public class ClassPathInvokeScanner extends ClassPathBeanDefinitionScanner {
    private String basePackage;
    private Class<?> beanClass;
    private Class<? extends Annotation> annotationClass;

    public ClassPathInvokeScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public void registerFilters() {
        if (annotationClass != null) {
            addIncludeFilter(new AnnotationTypeFilter(annotationClass));
        }
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> holders = super.doScan(basePackages);
        if (holders.isEmpty()) {
            //
        } else {
            processProxyBeanDefinitions(holders);
        }
        return holders;
    }

    private void processProxyBeanDefinitions(Set<BeanDefinitionHolder> holders) {
        holders.stream().map(holder -> (GenericBeanDefinition) holder.getBeanDefinition()).forEach(beanDefinition -> {
            String beanClassName = beanDefinition.getBeanClassName();
            beanDefinition.setBeanClass(beanClass);
            beanDefinition.getPropertyValues().add("interfaceName", beanClassName);
            beanDefinition.setAutowireMode(2);
        });
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }
}
