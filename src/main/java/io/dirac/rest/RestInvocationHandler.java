package io.dirac.rest;

import io.dirac.rest.annotation.Bind;
import io.dirac.rest.annotation.RemoteService;
import io.dirac.rest.annotation.RequestObject;
import io.dirac.rest.annotation.ServiceUri;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RestInvocationHandler<T> implements InvocationHandler {

    private Class<T> proxyInterface;
    private RestTemplate client;
    private ConcurrentMap<Method, ServiceConfig> mappings = new ConcurrentHashMap<>();

    public RestInvocationHandler(Class<T> proxyInterface) {
        this.proxyInterface = proxyInterface;
        initRestClient();
        initMethodMappings();
    }

    private void initRestClient() {
        if (proxyInterface.isInterface()) {
            RemoteService remoteService = proxyInterface.getAnnotation(RemoteService.class);
            if (remoteService != null) {
                String code = remoteService.value();
                client = createRestClient(code);
            } else {
                throw new NullPointerException("interface '" + proxyInterface.getName() + "' should be annotated @RemoteService.");
            }
        } else {
            throw new RuntimeException(proxyInterface.getName() + " should be interface.");
        }
    }

    private RestTemplate createRestClient(String code) {
        return new RestTemplate();
    }

    private void initMethodMappings() {
        for (Method method : proxyInterface.getDeclaredMethods()) {
            ServiceUri serviceUri = method.getAnnotation(ServiceUri.class);
            if (serviceUri != null) {
                mappings.putIfAbsent(method, new ServiceConfig(serviceUri.value(), serviceUri.method()));
            }
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        int parameterCount = method.getParameterCount();
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(client, args);
        }
        if ("toString".equals(methodName) && parameterCount == 0) {
            return client.toString();
        }
        if ("equals".equals(methodName) && parameterCount == 1) {
            return client.equals(args[0]);
        }
        if ("hashCode".equals(methodName) && parameterCount == 0) {
            return client.hashCode();
        }
        return invokeRest(method, args);
    }

    private Object invokeRest(Method method, Object[] args) {
        ServiceConfig config = mappings.get(method);
        if (config == null) {
            throw new NullPointerException("Method '" + method.getName() + "' should be annotated @ServiceUri.");
        }

        Map<String, Object> variables = new HashMap<>();
        Object requestObject = null;
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (parameter.getAnnotation(RequestObject.class) != null) {
                requestObject = args[i];
                continue;
            }
            Bind bind = parameter.getAnnotation(Bind.class);
            if (bind != null) {
                variables.put(bind.value(), args[i]);
            }
        }

        if ("GET".equalsIgnoreCase(config.method)) {
            return client.getForObject(config.uri, method.getReturnType(), variables);
        } else if ("POST".equalsIgnoreCase(config.method)) {
            return client.postForObject(config.uri, requestObject != null ? requestObject : variables, method.getReturnType(), variables);
        } else {
            throw new UnsupportedOperationException("Not support rest request method '" + config.method + "' yet.");
        }
    }

    private static class ServiceConfig {
        String uri;
        String method;

        ServiceConfig(String uri, String method) {
            this.uri = uri;
            this.method = method;
        }
    }
}
