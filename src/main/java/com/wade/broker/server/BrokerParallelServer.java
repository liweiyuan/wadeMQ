package com.wade.broker.server;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.wade.netty.NettyClustersConfig;

import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;

/**
 * @Author :lwy
 * @Date : 2019/9/6 16:54
 * @Description :
 */
public class BrokerParallelServer implements RemoteServer {

    int parallel = NettyClustersConfig.getWorkThreads();

    private ListeningExecutorService executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(parallel));
    private ExecutorCompletionService<Void> executorService;

    @Override
    public void init() {

        executorService = new ExecutorCompletionService<>(executor);
    }

    @Override
    public void start() {
        for (int i = 0; i < parallel; i++) {
            //executorService.submit(new SendMessageController());
            //executorService.submit(new AckPullMessageController());
            //executorService.submit(new AckPushMessageController());
        }
    }

    @Override
    public void shutdown() {
        executor.shutdown();
    }
}
