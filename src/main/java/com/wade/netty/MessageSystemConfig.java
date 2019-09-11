package com.wade.netty;

/**
 * @Author :lwy
 * @Date : 2019/9/6 16:59
 * @Description :
 */
public class MessageSystemConfig {

    private static final String SystemPropertySocketSndbufSize
            = "com.newlandframework.avatarmq.netty.socket.sndbuf.size";
    static int SocketSndbufSize
            = Integer.parseInt(System.getProperty(SystemPropertySocketSndbufSize, "65535"));

    private static final String SystemPropertySocketRcvbufSize
            = "com.newlandframework.avatarmq.netty.socket.rcvbuf.size";
    static int SocketRcvbufSize
            = Integer.parseInt(System.getProperty(SystemPropertySocketRcvbufSize, "65535"));

    public static final String SystemPropertyAckTaskSemaphoreValue
            = "com.newlandframework.avatarmq.semaphore.ackTaskSemaphoreValue";
    public static String AckTaskSemaphoreValue
            = System.getProperty(SystemPropertyAckTaskSemaphoreValue, "Ack");

    public static final String SystemPropertySemaphoreCacheHookTimeValue
            = "com.newlandframework.avatarmq.semaphore.hooktime";
    public static int SemaphoreCacheHookTimeValue
            = Integer.parseInt(System.getProperty(SystemPropertySemaphoreCacheHookTimeValue, "5"));


    public static final String SystemPropertyNotifyTaskSemaphoreValue
            = "com.newlandframework.avatarmq.semaphore.NotifyTaskSemaphoreValue";
    public static String NotifyTaskSemaphoreValue
            = System.getProperty(SystemPropertyNotifyTaskSemaphoreValue, "Notify");

    public final static String IpV4AddressDelimiter = ":";
    public final static String MessageDelimiter = "@";

}
