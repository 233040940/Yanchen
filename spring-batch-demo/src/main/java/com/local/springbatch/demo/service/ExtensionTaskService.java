package com.local.springbatch.demo.service;

import com.local.springbatch.demo.entity.dto.TaskDestinationDTO;
import com.local.springbatch.demo.entity.jpa.module.task.ExtensionTask;

/**
 * @Create-By: yanchen 2021/4/9 02:44
 * @Description: 推广任务服务
 */
public interface ExtensionTaskService {

    /**
     * 添加推广任务
     * @param task
     */
    void  saveTask(ExtensionTask task);

    /**
     * 添加推广任务目的地
     * @param dto
     */
    void  saveTaskDestination(TaskDestinationDTO dto);
}
