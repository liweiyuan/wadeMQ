package com.wade.consumer;

import com.wade.model.RemoteChannelData;
import com.wade.model.SubscriptionData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Predicate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author :lwy
 * @Date : 2019/9/11 16:48
 * @Description :
 * <p>
 * 消费者集群信息
 */
public class ConsumerClusters {
    private int next = 0;
    private final String clustersId;
    private final ConcurrentHashMap<String, SubscriptionData> subMap
            = new ConcurrentHashMap<String, SubscriptionData>();

    private final ConcurrentHashMap<String, RemoteChannelData> channelMap
            = new ConcurrentHashMap<String, RemoteChannelData>();

    private final List<RemoteChannelData> channelList = Collections.synchronizedList(new ArrayList<RemoteChannelData>());

    public ConsumerClusters(String clustersId) {
        this.clustersId = clustersId;
    }

    public String getClustersId() {
        return clustersId;
    }

    public ConcurrentHashMap<String, SubscriptionData> getSubMap() {
        return subMap;
    }

    public ConcurrentHashMap<String, RemoteChannelData> getChannelMap() {
        return channelMap;
    }

    public List<RemoteChannelData> getChannelList() {
        return channelList;
    }

    public void attachRemoteChannelData(String clientId, RemoteChannelData remoteChannelData) {

        if (findRemoteChannelData(remoteChannelData.getClientId()) == null) {
            channelMap.put(clientId, remoteChannelData);
            subMap.put(remoteChannelData.getSubcript().getTopic(), remoteChannelData.getSubcript());
            channelList.add(remoteChannelData);
        } else {
            System.out.println("consumer clusters exists! it's clientId:" + clientId);
        }
    }

    public RemoteChannelData findRemoteChannelData(String clientId) {
        return (RemoteChannelData) MapUtils.getObject(channelMap, clientId);
    }

    public void detachRemoteChannelData(String clientId) {
        channelMap.remove(clientId);

        Predicate predicate = o -> {
            String id = ((RemoteChannelData) o).getClientId();
            return id.compareTo(clientId) == 0;
        };

        RemoteChannelData o = (RemoteChannelData) CollectionUtils.find(channelList, predicate);
        if (o != null) {
            channelList.remove(o);
        }
    }
}
