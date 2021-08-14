package com.sz.transformation.util.eventbus;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Sean Zhang
 * Date 2021/8/14
 */
public class EventBusUtil {
    public static void register(Object subscriber){
        EventBus.getDefault().register(subscriber);
    }
    public static void unregister(Object subscriber){
        EventBus.getDefault().unregister(subscriber);
    }
    public static void send(Event event){
        EventBus.getDefault().post(event);
    }
    public static void send(int code){
        EventBus.getDefault().post(new Event(code));
    }
    public static boolean isRegistered(Object subscriber){
        return EventBus.getDefault().isRegistered(subscriber);
    }
}
