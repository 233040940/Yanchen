package com.local.common.office.excel.writer;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.*;
import com.local.common.enums.WorkerSource;
import com.local.common.office.excel.reader.MultiThreadReader;
import org.apache.poi.ss.usermodel.Sheet;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * @program: yanchen
 * @description: 多线程写
 * @author: yc
 * @date: 2020-07-03 12:42
 **/
public class MultiThreadWriter {

    private static final int MAX_THREAD_AMOUNT = 10;    //最大线程数

    private static final Logger LOGGER = LoggerFactory.getLogger(MultiThreadReader.class);

    private static final int MAX_WRITE_ROW = 10000;        //单个线程最大写入行

    private static final ListeningExecutorService EXECUTOR_SERVICE = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(MAX_THREAD_AMOUNT));   //采用guava的线程池

    private WorkerSource workerSource;

    private MultiThreadWriter(WorkerSource workerSource) {
        this.workerSource = workerSource;
    }

    public static MultiThreadWriter of(final WorkerSource workerSource) {
        return new MultiThreadWriter(workerSource);
    }

    public boolean startWrite(Sheet sheet, List<Field> fields, Collection excelEntities) throws InterruptedException {
        switch (workerSource) {
            case FUTURE:
                return adaptFutureWrite(sheet, fields, excelEntities);
            default:
                return adaptForkJoinWrite(sheet, fields, excelEntities);
        }
    }

    private boolean adaptForkJoinWrite(Sheet sheet, List<Field> fields, Collection excelEntities) {
        return false;
    }

    private boolean adaptFutureWrite(Sheet sheet, List<Field> fields, Collection excelEntities) throws InterruptedException {
        final int excelContentSize = excelEntities.size();
        int threadAmount = excelContentSize / MAX_WRITE_ROW;    //线程个数
        if (threadAmount * MAX_WRITE_ROW != excelContentSize) {
            threadAmount++;
        }
        final CountDownLatch downLatch = new CountDownLatch(threadAmount);
        int scale = excelContentSize % MAX_WRITE_ROW;     //取余
        int start = 1;    //写excel开始索引
        int end;          //写excel结束索引
        int subListStart = 0;    //截取excelContent集合开始索引
        int subListEnd;          //截取excelContent集合结束索引
        List<Boolean> result = Lists.newCopyOnWriteArrayList();      //写成功标志(此处用CopyOnWriteArrayList解决并发冲突)
        List entities = Lists.newArrayList(excelEntities);
        if (scale == 0) {    //总记录数刚好被线程数整除
            for (int i = 1; i <= threadAmount; i++) {
                end = i * MAX_WRITE_ROW + 1;
                subListEnd = i * MAX_WRITE_ROW;
                final List temp = entities.subList(subListStart, subListEnd);
                this.postWrite(result, downLatch, start, end, sheet, temp, fields);
                start = end;
                subListStart = subListEnd;
            }
        } else {
            for (int i = 1; i <= threadAmount; i++) {
                if (i == threadAmount) {
                    end = start + scale;
                    subListEnd = subListStart + scale;
                } else {
                    end = i * MAX_WRITE_ROW + 1;
                    subListEnd = i * MAX_WRITE_ROW;
                }
                final List temp = entities.subList(subListStart, subListEnd);
                this.postWrite(result, downLatch, start, end, sheet, temp, fields);
                start = end;
                subListStart = subListEnd;
            }
        }
        downLatch.await();
        return !result.contains(Boolean.FALSE);
    }

    private void postWrite(List result, CountDownLatch downLatch, int start, int end, Sheet sheet, Collection excelEntities, List<Field> fields) {
        ListenableFuture<Boolean> future = EXECUTOR_SERVICE.submit(new FutureWriteWorker(start, end, sheet, excelEntities, fields));
        Futures.addCallback(future, new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(@Nullable Boolean aBoolean) {
                result.add(aBoolean);
                downLatch.countDown();
            }
            @Override
            public void onFailure(Throwable throwable) {
                handleWriteFail(throwable);   //写失败处理
                downLatch.countDown();
            }
        }, MoreExecutors.directExecutor());

    }

    private void handleWriteFail(Throwable throwable) {
        LOGGER.info("写入excel出错", throwable);
    }
}
