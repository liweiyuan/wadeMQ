package com.wade.broker;

import com.wade.broker.ProducerMessageListener;
import com.wade.consumer.ConsumerContext;
import com.wade.core.ChannelCache;
import com.wade.msg.Message;
import io.netty.channel.Channel;

/**
 * @Author :lwy
 * @Date : 2019/9/11 16:15
 * @Description :
 *
 * 核心类，筛选对应的consumer
 */
public class ProducerMessageHook implements ProducerMessageListener {

    @Override
    public void hookProducerMessage(Message msg, String requestId, Channel channel) {

        ChannelCache.pushRequest(requestId, channel);

        String topic = msg.getTopic();

        focusTopicGroup = ConsumerContext.selectByTopic(topic);

        filterByTopic(topic);

        if (checkClustersSet(msg, requestId)) {
            dispatchTask(msg, topic);
            taskAck(msg, requestId);
            clustersSet.clear();
        } else {
            return;
        }
    }
}
