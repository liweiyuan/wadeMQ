package com.wade.netty;

import org.apache.commons.pool.PoolableObjectFactory;

/**
 * @Author :lwy
 * @Date : 2019/9/9 16:32
 * @Description :
 */
public class MessageConnectPoolableObjectFactory implements PoolableObjectFactory<MessageConnectFactory> {

    private String serverAddress;
    private int sessionTimeOut = 3 * 1000;

    public MessageConnectPoolableObjectFactory(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public MessageConnectPoolableObjectFactory(String serverAddress, int sessionTimeOut) {
        this.serverAddress = serverAddress;
        this.sessionTimeOut = sessionTimeOut;
    }

    @Override
    public MessageConnectFactory makeObject() throws Exception {
        MessageConnectFactory factory = new MessageConnectFactory(serverAddress);
        return factory;
    }

    @Override
    public void destroyObject(MessageConnectFactory obj) throws Exception {
        obj.close();
    }

    @Override
    public boolean validateObject(MessageConnectFactory obj) {
        return true;
    }

    @Override
    public void activateObject(MessageConnectFactory obj) throws Exception {

    }

    @Override
    public void passivateObject(MessageConnectFactory obj) throws Exception {

    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getSessionTimeOut() {
        return sessionTimeOut;
    }

    public void setSessionTimeOut(int sessionTimeOut) {
        this.sessionTimeOut = sessionTimeOut;
    }
}
