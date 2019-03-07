package com.cyh.ioclibrary.annotations;

import com.cyh.ioclibrary.recyclerview.RView;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerSetter = "setOnItemClickListener", listenerType = RView.OnItemClickListener.class, callBackListener = "onItemClick")
public @interface OnItemClick {
    int[] value();
}
