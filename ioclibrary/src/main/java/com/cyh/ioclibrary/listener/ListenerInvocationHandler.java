package com.cyh.ioclibrary.listener;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

// 将回调的onClick方法拦截，执行我们自己自定义的方法（aop概念）
public class ListenerInvocationHandler implements InvocationHandler {
    //需要拦截的对象
    private Object target;
    private HashMap<String, Method> methodHashMap = new HashMap<>();
    public ListenerInvocationHandler(Object target) {
        this.target = target;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (target != null) {
            //获取拦截的方法名
            String methodName = method.getName();
            //重新赋值，将拦截的方法换为show
            method = methodHashMap.get(methodName);
            if (method != null) {
                return method.invoke(target, args);
            }
        }
        return null;
    }


    /**
     * 将需要拦截的方法添加
     * @param methodName 需要拦截的方法。如onClick()
     * @param method 执行拦截后的方法。如show()
     */
    public void addMethod(String methodName, Method method) {
        methodHashMap.put(methodName, method);
    }
}
