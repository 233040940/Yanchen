package com.local.common.entity;

import com.local.common.utils.DateTimeHelper;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * @Create-By: yanchen 2021/4/9 01:51
 * @Description: TODO
 */
@MappedSuperclass
public class MataEntity {

    private long timestamp;

    private long updateTimestamp;

    public long getTimestamp() {
        return timestamp;
    }

    @PrePersist
    public void setTimestamp() {
        this.timestamp = DateTimeHelper.getSystemTimeStampToMillis();
    }

    public long getUpdateTimestamp() {
        return updateTimestamp;
    }

    @PreUpdate
    public void setUpdateTimestamp() {
        this.updateTimestamp = DateTimeHelper.getSystemTimeStampToMillis();
    }
}
