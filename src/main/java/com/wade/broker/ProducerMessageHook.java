package com.wade.broker;

import com.google.common.base.Joiner;
import com.wade.broker.ProducerMessageListener;
import com.wade.consumer.ConsumerClusters;
import com.wade.consumer.ConsumerContext;
import com.wade.core.*;
import com.wade.model.MessageDispatchTask;
import com.wade.msg.Message;
import com.wade.msg.ProducerAckMessage;
import com.wade.netty.MessageSystemConfig;
import io.netty.channel.Channel;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.ClosureUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.AnyPredicate;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author :lwy
 * @Date : 2019/9/11 16:15
 * @Description :
 *
 * 核心类，筛选对应的consumer
 */
public class ProducerMessageHook implements ProducerMessageListener {

    private List<ConsumerClusters> clustersSet = new ArrayList<>();
    private List<ConsumerClusters> focusTopicGroup = null;

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

    private void taskAck(Message msg, String requestId) {

        try {
            Joiner joiner = Joiner.on(MessageSystemConfig.MessageDelimiter).skipNulls();
            String key = joiner.join(requestId, msg.getMsgId());
            AckMessageCache.getAckMessageCache().appendMessage(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dispatchTask(Message msg, String topic) {
        List<MessageDispatchTask> tasks = new ArrayList<MessageDispatchTask>();

        for (int i = 0; i < clustersSet.size(); i++) {
            MessageDispatchTask task = new MessageDispatchTask();
            task.setClusters(clustersSet.get(i).getClustersId());
            task.setTopic(topic);
            task.setMessage(msg);
            tasks.add(task);

        }

        MessageTaskQueue.getInstance().pushTask(tasks);

        for (int i = 0; i < tasks.size(); i++) {
            SemaphoreCache.release(MessageSystemConfig.NotifyTaskSemaphoreValue);
        }
    }

    private boolean checkClustersSet(Message msg, String requestId) {
        if (clustersSet.size() == 0) {
            System.out.println("AvatarMQ don't have match clusters!");
            ProducerAckMessage ack = new ProducerAckMessage();
            ack.setMsgId(msg.getMsgId());
            ack.setAck(requestId);
            ack.setStatus(ProducerAckMessage.SUCCESS);
            AckTaskQueue.pushAck(ack);
            SemaphoreCache.release(MessageSystemConfig.AckTaskSemaphoreValue);
            return false;
        } else {
            return true;
        }
    }

    private void filterByTopic(String topic) {

        Predicate focusAllPredicate = object -> {
            ConsumerClusters clusters = (ConsumerClusters) object;
            return clusters.findSubscriptionData(topic) != null;
        };

        AnyPredicate any = new AnyPredicate(new Predicate[]{focusAllPredicate});

        Closure joinClosure = input -> {
            if (input instanceof ConsumerClusters) {
                ConsumerClusters clusters = (ConsumerClusters) input;
                clustersSet.add(clusters);
            }
        };

        Closure ignoreClosure = input -> { };

        Closure ifClosure = ClosureUtils.ifClosure(any, joinClosure, ignoreClosure);

        CollectionUtils.forAllDo(focusTopicGroup, ifClosure);
    }
}
