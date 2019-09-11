/**
 * Copyright (C) 2016 Newland Group Holding Limited
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wade.broker.strategy;

import com.wade.broker.ConsumerMessageListener;
import com.wade.broker.ProducerMessageListener;
import com.wade.model.RequestMessage;
import com.wade.model.ResponseMessage;
import com.wade.msg.Message;
import io.netty.channel.ChannelHandlerContext;

/**
 * @filename:BrokerProducerMessageStrategy.java
 * @description:BrokerProducerMessageStrategy功能模块
 * @author tangjie<https: / / github.com / tang-jie>
 * @blog http://www.cnblogs.com/jietang/
 * @since 2016-8-11
 *
 * 生产者---->broker
 */
public class BrokerProducerMessageStrategy implements BrokerStrategy {

    private ProducerMessageListener producerMessageHandler;
    private ChannelHandlerContext channelHandler;

    public BrokerProducerMessageStrategy() {

    }

    @Override
    public void messageDispatch(RequestMessage request, ResponseMessage response) {
        Message message = (Message) request.getMsgParams();
        producerMessageHandler.hookProducerMessage(message, request.getMsgId(), channelHandler.channel());
    }

    @Override
    public void setProducerMessageHandler(ProducerMessageListener producerMessageHandler) {
        this.producerMessageHandler = producerMessageHandler;
    }

    @Override
    public void setChannelHandler(ChannelHandlerContext channelHandler) {
        this.channelHandler = channelHandler;
    }

    @Override
    public void setHookConsumer(ConsumerMessageListener hookConsumer) {

    }
}
