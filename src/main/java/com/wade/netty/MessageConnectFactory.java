package com.wade.netty;

import com.wade.core.CallBackInvoker;
import io.netty.channel.Channel;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author :lwy
 * @Date : 2019/9/9 16:20
 * @Description :
 */
public class MessageConnectFactory {

    private SocketAddress remoteAddr = null;
    //消息的载体
    private Channel messageChannel = null;
    private Map<String, CallBackInvoker<Object>> callBackMap = new ConcurrentHashMap<String, CallBackInvoker<Object>>();
    //超时时间
    private long timeout = 10 * 1000;


    public MessageConnectFactory(String serverAddress) {
        String[] ipAddr = serverAddress.split(MessageSystemConfig.IpV4AddressDelimiter);
        if (ipAddr.length == 2) {
            remoteAddr = NettyUtil.string2SocketAddress(serverAddress);
        }
    }


    public void close() {

        if(messageChannel!=null){
            try {
                messageChannel.close().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Channel getMessageChannel() {
        return messageChannel;
    }

    public Map<String, CallBackInvoker<Object>> getCallBackMap() {
        return callBackMap;
    }

    public void setCallBackMap(Map<String, CallBackInvoker<Object>> callBackMap) {
        this.callBackMap = callBackMap;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
