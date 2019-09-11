package com.wade.consumer;

import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.Objects;

/**
 * @Author :lwy
 * @Date : 2019/9/11 17:03
 * @Description :
 */
public class ClustersRelation {

    private String id;
    private ConsumerClusters clusters;

    public ClustersRelation() {
    }

    public ClustersRelation(String id, ConsumerClusters clusters) {
        this.id = id;
        this.clusters = clusters;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ConsumerClusters getClusters() {
        return clusters;
    }

    public void setClusters(ConsumerClusters clusters) {
        this.clusters = clusters;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj != null && ClustersRelation.class.isAssignableFrom(obj.getClass())) {
            ClustersRelation clusters = (ClustersRelation) obj;
            result = new EqualsBuilder().append(id, clusters.getId())
                    .isEquals();
        }
        return result;
    }
}
