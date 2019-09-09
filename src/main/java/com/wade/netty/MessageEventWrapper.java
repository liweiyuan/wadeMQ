package com.wade.netty;

import com.wade.core.HookMessageEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author :lwy
 * @Date : 2019/9/9 16:11
 * @Description :
 * <p>
 * 消息封装
 */

public class MessageEventWrapper<T> extends ChannelInboundHandlerAdapter implements MessageEventHandler, MessageEventProxy {

    public final static String proxyMappedName = "handleMessage";
    protected MessageProcessor processor;
    protected Throwable cause;
    protected HookMessageEvent<T> hook;
    protected MessageConnectFactory factory;
    private MessageEventWrapper<T> wrapper;

    public MessageEventWrapper() {
    }

    public MessageEventWrapper(MessageProcessor processor) {
        this(processor, null);
    }

    public MessageEventWrapper(MessageProcessor processor, HookMessageEvent<T> hook) {
        this.processor = processor;
        this.hook = hook;
        this.factory = processor.getMessageConnectFactory();
    }

    @Override
    public void handleMessage(ChannelHandlerContext ctx, Object msg) {

    }

    @Override
    public void beforeMessage(Object msg) {

    }

    @Override
    public void afterMessage(Object msg) {

    }
}
