package com.wade.netty;

/**
 * @Author :lwy
 * @Date : 2019/9/6 17:00
 * @Description :
 */
public class NettyClustersConfig {

    private int clientSocketSndBufSize = MessageSystemConfig.SocketSndbufSize;
    private int clientSocketRcvBufSize = MessageSystemConfig.SocketRcvbufSize;

    private static int workThreads = Runtime.getRuntime().availableProcessors() * 2;


    public int getClientSocketSndBufSize() {
        return clientSocketSndBufSize;
    }

    public int getClientSocketRcvBufSize() {
        return clientSocketRcvBufSize;
    }

    public static int getWorkThreads() {
        return workThreads;
    }
}
