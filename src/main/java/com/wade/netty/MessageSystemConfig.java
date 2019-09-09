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



    public final static String IpV4AddressDelimiter = ":";

}
