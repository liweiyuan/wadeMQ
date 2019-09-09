package com.wade.core;

/**
 * @Author :lwy
 * @Date : 2019/9/9 16:19
 * @Description :
 */
public abstract class HookMessageEvent<T> {

    public void disconnect(T message) {
    }

    public T callBackMessage(T message) {
        return message;
    }
}
