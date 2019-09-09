package com.wade.broker;

import com.wade.broker.strategy.BrokerStrategyContext;
import com.wade.model.MessageSource;
import com.wade.model.RequestMessage;
import com.wade.model.ResponseMessage;
import com.wade.netty.ShareMessageEventWrapper;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author :lwy
 * @Date : 2019/9/9 16:09
 * @Description :
 * broker消息处理
 */
public class MessageBrokerHandler extends ShareMessageEventWrapper<Object> {

    private AtomicReference<ProducerMessageListener> hookProducer;
    private AtomicReference<ConsumerMessageListener> hookConsumer;
    private AtomicReference<RequestMessage> message = new AtomicReference<RequestMessage>();

    public MessageBrokerHandler() {
        super.setWrapper(this);
    }

    public MessageBrokerHandler buildProducerHook(ProducerMessageListener hookProducer) {
        this.hookProducer = new AtomicReference<ProducerMessageListener>(hookProducer);
        return this;
    }

    public MessageBrokerHandler buildConsumerHook(ConsumerMessageListener hookConsumer) {
        this.hookConsumer = new AtomicReference<ConsumerMessageListener>(hookConsumer);
        return this;
    }

    @Override
    public void handleMessage(ChannelHandlerContext ctx, Object msg) {
        RequestMessage messgae = message.get();

        ResponseMessage responseMessage=new ResponseMessage();
        responseMessage.setMsgId(messgae.getMsgId());
        responseMessage.setMsgSource(MessageSource.AvatarMQBroker);

        BrokerStrategyContext strategy = new BrokerStrategyContext(messgae, responseMessage, ctx);
        strategy.setHookConsumer(hookConsumer.get());
        strategy.setHookProducer(hookProducer.get());
        strategy.invoke();
    }

    @Override
    public void beforeMessage(Object msg) {
        message.set((RequestMessage) msg);
    }
}
