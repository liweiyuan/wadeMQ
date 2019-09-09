package com.wade.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Author :lwy
 * @Date : 2019/9/9 16:52
 * @Description :
 */
public class CallBackInvoker<T> {


    //TODO
    private String requestId;
    private Throwable reason;
    private final CountDownLatch countDownLatch = new CountDownLatch(1);
    private List<CallBackListener<T>> listeners = Collections.synchronizedList(new ArrayList<CallBackListener<T>>());
    //返回结果
    private T messageResult;


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /**
     * 异常信息处理
     *
     * @param cause
     */
    public void setReason(Throwable cause) {
        reason = cause;
        publish();
        //发布
        countDownLatch.countDown();
    }

    /**
     * 调用回调监听器
     */
    private void publish() {
        for (CallBackListener<T> listener : listeners) {
            listener.onCallBack(messageResult);
        }
    }

    /**
     * 在时间段内等待结果返回
     *
     * @param timeout
     * @param unit
     * @return
     */
    public Object getMessageResult(long timeout, TimeUnit unit) {
        try {
            countDownLatch.await(timeout, unit);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        if (reason != null) {
            return null;
        }
        return messageResult;
    }


    public void join(CallBackListener<T> listener) {
        this.listeners.add(listener);
    }
}
