package com.wade.core;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author :lwy
 * @Date : 2019/9/11 16:42
 * @Description :
 */
public class ChannelCache {


    private static Map<String, Channel> channelMap = new ConcurrentHashMap<>();


    public static void pushRequest(String requestId, Channel channel) {
        channelMap.put(requestId, channel);
    }

    public static Channel filteChannel(final String requestId) {
        return channelMap.remove(requestId);
    }
}
