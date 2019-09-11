package com.wade.netty;

import com.wade.core.HookMessageEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;

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

    public MessageEventWrapper<T> getWrapper() {
        return wrapper;
    }

    public void setWrapper(MessageEventWrapper<T> wrapper) {
        this.wrapper = wrapper;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        //消息处理
        ProxyFactory weaver = new ProxyFactory(wrapper);
        PointcutAdvisor advisor = new NameMatchMethodPointcutAdvisor();
        ((NameMatchMethodPointcutAdvisor) advisor).setMappedName(proxyMappedName);
        ((NameMatchMethodPointcutAdvisor) advisor).setAdvice(new MessageEventAdvisor(wrapper, msg));
        weaver.addAdvisor(advisor);

        MessageEventHandler proxyHandler = (MessageEventHandler) weaver.getProxy();
        proxyHandler.handleMessage(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        this.cause = cause;
        cause.printStackTrace();
    }
}
