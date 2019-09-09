package com.wade.netty;

import com.wade.core.CallBackInvoker;
import com.wade.core.CallBackListener;
import com.wade.core.NotifyCallback;
import com.wade.model.RequestMessage;
import com.wade.model.ResponseMessage;
import com.wade.msg.ProducerAckMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @Author :lwy
 * @Date : 2019/9/9 16:17
 * @Description :
 */
public class MessageProcessor {

    private MessageConnectFactory factory = null;
    private MessageConnectPool pool = null;

    public MessageProcessor(String serverAddress) {
        MessageConnectPool.setServerAddress(serverAddress);
        pool = MessageConnectPool.getMessageConnectPoolInstance();
        this.factory = pool.borrow();
    }

    //后续需要关注 todo
    public void closeMessageConnectFactory() {
        pool.restore();
    }

    public MessageConnectFactory getMessageConnectFactory() {
        return factory;
    }


    /**
     * 无须返回操作
     *
     * @param message
     */
    public void sendSyncMessage(RequestMessage message) {

        Channel channel = factory.getMessageChannel();
        if (channel == null) {
            return;
        }
        Map<String, CallBackInvoker<Object>> callBackMap = factory.getCallBackMap();
        final CallBackInvoker<Object> invoker = new CallBackInvoker<>();
        callBackMap.put(message.getMsgId(), invoker);
        invoker.setRequestId(message.getMsgId());

        ChannelFuture channelFuture;

        try {
            channelFuture = channel.writeAndFlush(message).sync();

            //异步操作监听器
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (!future.isSuccess()) {
                        invoker.setReason(future.cause());
                    }
                }
            });
        } catch (InterruptedException e) {
            Logger.getLogger(MessageProcessor.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * 有返回值
     *
     * @param message
     * @return
     */
    public Object sendAsyncMessage(RequestMessage message) {
        Channel channel = factory.getMessageChannel();
        if (channel == null) {
            return null;
        }
        Map<String, CallBackInvoker<Object>> callBackMap = factory.getCallBackMap();
        final CallBackInvoker<Object> invoker = new CallBackInvoker<>();
        callBackMap.put(message.getMsgId(), invoker);
        invoker.setRequestId(message.getMsgId());

        ChannelFuture channelFuture = channel.writeAndFlush(message);
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    invoker.setReason(future.cause());
                }
            }
        });
        try {
            Object result = invoker.getMessageResult(factory.getTimeout(), TimeUnit.MILLISECONDS);
            callBackMap.remove(message.getMsgId());
            return result;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 异步回调方式通知
     *
     * @param message
     * @param listener
     */
    public void sendAsyncMessage(RequestMessage message, final NotifyCallback listener) {
        Channel channel = factory.getMessageChannel();
        if (channel == null) {
            return;
        }
        Map<String, CallBackInvoker<Object>> callBackMap = factory.getCallBackMap();
        final CallBackInvoker<Object> invoker = new CallBackInvoker<>();
        callBackMap.put(message.getMsgId(), invoker);
        invoker.setRequestId(message.getMsgId());

        //关注点
        invoker.join(new CallBackListener<Object>() {
            @Override
            public void onCallBack(Object object) {
                ResponseMessage response = (ResponseMessage) object;
                listener.onEvent((ProducerAckMessage) response.getMsgParams());
            }
        });

        ChannelFuture channelFuture = channel.writeAndFlush(message);
        channelFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    invoker.setReason(future.cause());
                }
            }
        });
    }
}
