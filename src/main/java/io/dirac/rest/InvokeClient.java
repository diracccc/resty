package io.dirac.rest;

import java.lang.reflect.Proxy;

public class InvokeClient<T> {
    private volatile T proxy;
    private Class<T> interfaceClass;
    protected String interfaceName;

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public Class<T> getInterfaceClass() {
        return interfaceClass;
    }

    public T get() {
        if (proxy == null) {
            synchronized (this) {
                if (proxy == null) {
                    init();
                }
            }
        }
        return proxy;
    }

    private void init() {
        initInterface();
        createProxy();
    }

    @SuppressWarnings("unchecked")
    private void initInterface() {
        try {
            interfaceClass = (Class<T>) Class.forName(interfaceName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("", e);
        }
    }

    @SuppressWarnings("unchecked")
    private void createProxy() {
        proxy = (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{interfaceClass}, new RestInvocationHandler<>(interfaceClass));
    }
}
