/**
 * Copyright (C) 2016 Newland Group Holding Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wade.consumer;


import com.wade.broker.ConsumerMessageListener;
import com.wade.model.RemoteChannelData;
import com.wade.model.SubscriptionData;
import com.wade.msg.SubscribeMessage;

/**
 * @filename:ConsumerMessageHook.java
 * @description:ConsumerMessageHook功能模块
 * @author tangjie<https://github.com/tang-jie>
 * @blog http://www.cnblogs.com/jietang/
 * @since 2016-8-11
 */
public class ConsumerMessageHook implements ConsumerMessageListener {

    public ConsumerMessageHook() {

    }

    @Override
    public void hookConsumerMessage(SubscribeMessage request, RemoteChannelData channel) {

        System.out.println("receive subcript info groupid:" + request.getClusterId() + " topic:" + request.getTopic() + " clientId:" + channel.getClientId());

        SubscriptionData subscript = new SubscriptionData();

        subscript.setTopic(request.getTopic());
        channel.setSubcript(subscript);

        ConsumerContext.addClusters(request.getClusterId(), channel);
    }
}
