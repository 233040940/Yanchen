package com.local.common.repeat.standalone;


import com.local.common.repeat.RepeatSendValidHelper;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description   单体架构重复提交验证策略,基于guava cache
 * @date 2020-06-02 16:07
 */
public  abstract class StandAloneStrategy implements RepeatSendValidHelper {

    protected abstract boolean valid();

    @Override
    public boolean isRepeatSend() {
        return valid();
    }
}
