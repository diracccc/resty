package io.dirac.rest.spring;

import io.dirac.rest.InvokeClient;
import org.springframework.beans.factory.FactoryBean;

public class InvokeFactoryBean<T> extends InvokeClient<T> implements FactoryBean<T> {
    @Override
    public T getObject() {
        return get();
    }

    @Override
    public Class<?> getObjectType() {
        return getInterfaceClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
