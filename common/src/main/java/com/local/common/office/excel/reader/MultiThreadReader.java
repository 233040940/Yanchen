package com.local.common.office.excel.reader;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.*;
import com.local.common.enums.WorkerSource;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.poi.ss.usermodel.Sheet;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author yc
 * @version 1.0
 * @project yanchen
 * @description 多线程读取
 * @date 2020-06-25 19:09
 */
public class MultiThreadReader {

    private static final int MAX_THREAD_AMOUNT = 10;    //最大线程数

    private static final Logger LOGGER = LoggerFactory.getLogger(MultiThreadReader.class);

    private static final int MAX_READ_ROW = 10000;        //单个线程最大读取行

    private static final ListeningExecutorService EXECUTOR_SERVICE = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(MAX_THREAD_AMOUNT));   //采用guava的线程池

    private WorkerSource workerSource;

    public MultiThreadReader(WorkerSource workerSource) {
        this.workerSource = workerSource;
    }

    public Collection startRead(Sheet sheet, Class<?> excelTemplate, List<Triple<Field, Integer, ? extends Class<?>>> triples) throws ExecutionException, InterruptedException {

        switch (workerSource) {
            case FUTURE:
                return adaptFutureWorkerRead(sheet, excelTemplate, triples);
            default:
                return adaptForkJoinWorkerRead(sheet, excelTemplate, triples);
        }
    }

    //委托forkJoinWorker
    private Collection adaptForkJoinWorkerRead(Sheet sheet, Class<?> excelTemplate, List<Triple<Field, Integer, ? extends Class<?>>> triples) throws ExecutionException, InterruptedException {

        ForkJoinPool forkJoinPool = new ForkJoinPool();

        final int defaultStart=1;

        ForkJoinTask<Collection> result = forkJoinPool.submit(new ForkJoinReadWorker(defaultStart, sheet.getPhysicalNumberOfRows(), sheet, excelTemplate, triples));

        return result.get();
    }

    //委托futureWorker
    private Collection adaptFutureWorkerRead(Sheet sheet, Class<?> excelTemplate, List<Triple<Field, Integer, ? extends Class<?>>> triples) throws ExecutionException, InterruptedException {

        final int lastRowNum = sheet.getLastRowNum();

        int threadAmount = lastRowNum / MAX_READ_ROW;   //线程个数

        if ((threadAmount * MAX_READ_ROW) != lastRowNum) {    //如果有余数则线程数+1
            threadAmount++;
        }

        final CountDownLatch downLatch = new CountDownLatch(threadAmount);

        int start = 1;         //excel row 开始索引

        int scale = lastRowNum % MAX_READ_ROW;     //取余

        int end;             //excel row 结束索引

        List result = Lists.newCopyOnWriteArrayList();   //采用并发安全的CopyOnWriteArrayList容器

        if (scale == 0) {           //刚好被整除
            for (int i = 1; i <= threadAmount; i++) {
                end = i * MAX_READ_ROW + 1;
                this.futurePostRead(result, downLatch, start, end, sheet, excelTemplate, triples);
                start = end;
            }
        } else {
            for (int i = 1; i <= threadAmount; i++) {
                if (i == threadAmount) {
                    end = start + scale;
                } else {
                    end = i * MAX_READ_ROW + 1;
                }
                this.futurePostRead(result, downLatch, start, end, sheet, excelTemplate, triples);
                start = end;
            }
        }
        downLatch.await();
        return result;
    }

    private void futurePostRead(List result, CountDownLatch downLatch, int start, int end, Sheet sheet, Class<?> excelTemplate, List<Triple<Field, Integer, ? extends Class<?>>> triples) {

        ListenableFuture<Collection> future = EXECUTOR_SERVICE.submit(new FutureReadWorker(start, end, sheet, excelTemplate, triples));

        Futures.addCallback(future, new FutureCallback<Collection>() {
            @Override
            public void onSuccess(@Nullable Collection collection) {

                result.addAll(collection);
                downLatch.countDown();
            }

            @Override
            public void onFailure(Throwable throwable) {

                LOGGER.warn("读取出错", throwable); //TODO 补偿机制
                downLatch.countDown();
            }
        }, MoreExecutors.directExecutor());
    }

}
