package com.wade.broker.server;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.wade.broker.MessageBrokerHandler;
import com.wade.netty.*;
import com.wade.serialize.KryoCodecUtil;
import com.wade.serialize.KryoPoolFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @Author :lwy
 * @Date : 2019/9/6 16:47
 * @Description :
 */
public class WadeMQBrokerServer extends BrokerParallelServer {

    private ThreadFactory boosFactory = new ThreadFactoryBuilder()
            .setDaemon(true)
            .setNameFormat("WadeMQBroker-boosSelector-%d")
            .build();

    private ThreadFactory workFactory = new ThreadFactoryBuilder()
            .setDaemon(true)
            .setNameFormat("WadeMQBroker-workSelector-%d")
            .build();

    private int brokerServerPort = 0;
    private SocketAddress serverIpAddr;

    private EventLoopGroup boss;
    private EventLoopGroup workers;
    private ServerBootstrap bootstrap;
    private NettyClustersConfig nettyClustersConfig = new NettyClustersConfig();
    private DefaultEventExecutorGroup defaultEventExecutorGroup;
    private MessageBrokerHandler handler;

    public WadeMQBrokerServer(String serverAddress) {

        String[] ipAddr = serverAddress.split(MessageSystemConfig.IpV4AddressDelimiter);

        if (ipAddr.length == 2) {
            serverIpAddr = NettyUtil.string2SocketAddress(serverAddress);
        }
    }

    @Override
    public void init() {
        try {
            boss = new NioEventLoopGroup(1, boosFactory);
            workers = new NioEventLoopGroup(parallel, workFactory, NettyUtil.getNioSelectorProvider());

            final KryoCodecUtil util = new KryoCodecUtil(KryoPoolFactory.getKryoPoolInstance());
            bootstrap = new ServerBootstrap();
            bootstrap.group(boss, workers).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_KEEPALIVE, false)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_SNDBUF, nettyClustersConfig.getClientSocketSndBufSize())
                    .option(ChannelOption.SO_RCVBUF, nettyClustersConfig.getClientSocketRcvBufSize())
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .localAddress(serverIpAddr)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                            ch.pipeline().addLast(
                                    defaultEventExecutorGroup,
                                    new MessageObjectEncoder(util),
                                    new MessageObjectDecoder(util),
                                    handler);
                        }
                    });


            super.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {

        try {
            String ipAddress = NettyUtil.socketAddress2String(serverIpAddr);
            System.out.printf("broker server ip:[%s]\n", ipAddress);

            ChannelFuture sync = this.bootstrap.bind().sync();

            super.start();

            sync.channel().closeFuture().sync();
            InetSocketAddress addr = (InetSocketAddress) sync.channel().localAddress();
            brokerServerPort = addr.getPort();
        } catch (InterruptedException ex) {
            Logger.getLogger(WadeMQBrokerServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void shutdown() {

        try {
            super.shutdown();
            boss.shutdownGracefully();
            workers.shutdownGracefully();
            defaultEventExecutorGroup.shutdownGracefully();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("AvatarMQBrokerServer shutdown exception!");
        }
    }
}
