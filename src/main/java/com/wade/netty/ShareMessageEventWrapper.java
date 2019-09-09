package com.wade.netty;

import com.wade.core.HookMessageEvent;
import io.netty.channel.ChannelHandler;

/**
 * @Author :lwy
 * @Date : 2019/9/9 17:44
 * @Description :
 */
@ChannelHandler.Sharable
public class ShareMessageEventWrapper<T> extends MessageEventWrapper<T>{

    public ShareMessageEventWrapper() {
        super.setWrapper(this);
    }

    public ShareMessageEventWrapper(MessageProcessor processor) {
        super(processor,null);
        super.setWrapper(this);
    }

    public ShareMessageEventWrapper(MessageProcessor processor, HookMessageEvent<T> hook) {
        super(processor, hook);
        super.setWrapper(this);
    }
}
