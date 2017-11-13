package org.saikrishna.apps.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.Instant;

import static java.time.temporal.ChronoUnit.MILLIS;

public class ExecutionTimeReporter implements InvocationHandler {

    private Object proxiedInstance;

    public static Object wrapAround(Object objectToProxy) {
        return Proxy.newProxyInstance(objectToProxy.getClass().getClassLoader(),
                objectToProxy.getClass().getInterfaces(), new ExecutionTimeReporter(objectToProxy));
    }

    public ExecutionTimeReporter(Object objectToProxy) {
        this.proxiedInstance = objectToProxy;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Instant startTime = Instant.now();
        Object result = null;
        try {
            result = method.invoke(proxiedInstance, args);
        } catch (IllegalAccessException |
                IllegalArgumentException |
                InvocationTargetException e) {
            System.err.println("Encountered error " + e);
            throw e;
        }
        finally {
            long executionTime = startTime.until(Instant.now(), MILLIS);
            System.out.println(proxiedInstance.getClass() + "." +method.getName() + "() Took " +  executionTime + " Millis ");
        }
        return result;
    }
}
