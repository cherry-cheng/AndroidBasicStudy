package com.cyh.ioclibrary;

import android.app.Activity;
import android.view.View;

import com.cyh.ioclibrary.annotations.ContentView;
import com.cyh.ioclibrary.annotations.EventBase;
import com.cyh.ioclibrary.annotations.InjectView;
import com.cyh.ioclibrary.listener.ListenerInvocationHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectManager {
    public static void inject(Activity activity) {
        //布局的注入
        injectLayout(activity);
        //控件的注入
        injectViews(activity);
        //事件的注入
        injectEvents(activity);
    }

    //点击事件的注入
    public static void injectEvents(Activity activity) {
        //获取类
        Class<? extends Activity> clazz = activity.getClass();
        //获取类所有的方法
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            //获取方法的注解（多个控件id）
            Annotation[] annotations = method.getAnnotations();
            //遍历注解
            for (Annotation annotation : annotations) {
                //获取注解上的注解
                //获取OnClick注解上的注解类型
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType != null) {
                    //通过EventBase指定获取
                    EventBase eventBase = annotationType.getAnnotation(EventBase.class);
                    if (eventBase != null) {
                        //事件3大成员
                        String listenerSetter = eventBase.listenerSetter();
                        Class<?> listerType = eventBase.listenerType();
                        String callBackListener = eventBase.callBackListener();
                        try {
                            //获取注解的值，执行方法再去获得注解的值
                            //通过annotationType获取onClick注解的value值
                            Method valueMethod = annotationType.getDeclaredMethod("value");
                            //执行value方法获得注解的值
                            int[] viewIds = (int[]) valueMethod.invoke(annotation);

                            //代理方法（3个成员组合）
                            //拦截方法
                            //得到监听的代理对象（新建代理单例、类的加载器，指定要代理的对象类的类型、class实例）
                            ListenerInvocationHandler handler = new ListenerInvocationHandler(activity);
                            //添加到拦截列表里面
                            handler.addMethod(callBackListener, method);
                            //监听对象的代理对象
                            //ClassLoader loader:指定当前目标对象使用的类加载器，获取加载器的方法是固定的
                            //Class<?>[] interfaces:目标对象实现的借口的类型，使用范型确认类型
                            //InvocationHandler h:事件处理，执行目标对象的方法时，会触发事件处理器的方法
                            Object listener = Proxy.newProxyInstance(listerType.getClassLoader(),
                                    new Class[]{listerType}, handler);

                            //遍历注解的值
                            for (int viewId : viewIds) {
                                //获取当前activity的view(赋值)
                                View view = activity.findViewById(viewId);
                                //获取指定的方法
                                Method setter = view.getClass().getMethod(listenerSetter, listerType);
                                //执行方法
                                setter.invoke(view, listener);
                            }
                        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    //控件的注入
    private static void injectViews(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            InjectView injectView = field.getAnnotation(InjectView.class);
            if (injectView != null) {
                int viewId = injectView.value();
                try {
                    Method method = clazz.getMethod("findViewById", int.class);
                    Object view = method.invoke(activity, viewId);
                    //另一种写法
                    //view = activity.findViewById(viewId);
                    //还有坑，访问修饰符
                    field.setAccessible(true);
                    field.set(activity, view);
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    //布局的注入
    private static void injectLayout(Activity activity) {
        //获取类
        Class<? extends Activity> clazz = activity.getClass();
        //获取注解的值（R.layout.mmmmain）
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if (contentView != null) {
            int layoutId = contentView.value();
            try {
                //获取指定的方法（setContentView） 坑： getMethod
                Method method = clazz.getMethod("setContentView", int.class);
                //执行方法
                method.invoke(activity, layoutId);

                //另一种写法：
                //activity.setContentView(layoutId);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
