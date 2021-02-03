package com.local.security.entity;

import com.local.common.utils.DateTimeHelper;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@MappedSuperclass
public class MataEntity {
    private long createTimeStamp;
    private long updateTimeStamp;

    public long getCreateTimeStamp() {
        return createTimeStamp;
    }
    @PrePersist
    public void setCreateTimeStamp() {
        this.createTimeStamp = DateTimeHelper.getSystemTimeStampToMillis();
    }
    public long getUpdateTimeStamp() {
        return updateTimeStamp;
    }
    @PreUpdate
    public void setUpdateTimeStamp() {
        this.updateTimeStamp = DateTimeHelper.getSystemTimeStampToMillis();
    }
}
