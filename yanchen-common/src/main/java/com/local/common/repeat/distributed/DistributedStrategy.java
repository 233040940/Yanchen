package com.local.common.repeat.distributed;


import com.local.common.repeat.RepeatSendValidHelper;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description TODO  分布式架构重复提交验证策略
 * @date 2020-06-02 16:09
 */
public abstract class DistributedStrategy implements RepeatSendValidHelper {

    @Override
    public boolean isRepeatSend() {
        return valid();
    }

    protected abstract boolean valid();
}
