package com.local.common.office.excel.reader;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.*;
import com.local.common.enums.ReaderType;
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

    private static final int MAX_THREAD_AMOUNT=10;    //最大线程数

    private static final Logger LOGGER= LoggerFactory.getLogger(MultiThreadReader.class);

    private static final int MAX_READ_ROW=10000;        //单个线程最大读取行

    private static final ListeningExecutorService EXECUTOR_SERVICE= MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(MAX_THREAD_AMOUNT));   //采用guava的线程池

    private ReaderType readerType;

    public MultiThreadReader(ReaderType readerType){
        this.readerType=readerType;
    }

   public Collection startRead(Sheet sheet, Class<?> excelTemplate, List<Triple<Field, Integer, ? extends Class<?>>> triples) throws ExecutionException, InterruptedException {

       switch (readerType) {
           case FORKJOIN:
               return adaptForkJoinWorker(sheet, excelTemplate, triples);
           default:
               return adaptFutureWorker(sheet, excelTemplate, triples);
       }
   }

   //委托forkJoinWorker
   private Collection adaptForkJoinWorker(Sheet sheet, Class<?> excelTemplate, List<Triple<Field, Integer, ? extends Class<?>>> triples) throws ExecutionException, InterruptedException {

       ForkJoinPool forkJoinPool = new ForkJoinPool();

       ForkJoinTask<Collection> result = forkJoinPool.submit(new ForkJoinReadWorker(1, sheet.getPhysicalNumberOfRows(), sheet, excelTemplate, triples));

       return result.get();
   }

   //委托给futureWorker
    private Collection adaptFutureWorker(Sheet sheet, Class<?> excelTemplate, List<Triple<Field, Integer, ? extends Class<?>>> triples) throws ExecutionException, InterruptedException {

        final int lastRowNum=sheet.getLastRowNum();

        int threadAmount=lastRowNum/MAX_READ_ROW;   //线程个数

        if((threadAmount * MAX_READ_ROW) != lastRowNum){
            threadAmount++;
        }

        final CountDownLatch downLatch=new CountDownLatch(threadAmount);

        int start=1;

        int scale=lastRowNum % MAX_READ_ROW;     //取余

        int end;

        List result= Lists.newArrayListWithCapacity(lastRowNum);

        for (int i = 0; i <threadAmount; i++) {

            if(scale==0){
                end=start + MAX_READ_ROW;
            }else{
                end=start + scale;
            }

            ListenableFuture<Collection> future = EXECUTOR_SERVICE.submit(new FutureReadWorker(start,end , sheet, excelTemplate, triples));

            Futures.addCallback(future, new FutureCallback<Collection>() {
                @Override
                public void onSuccess(@Nullable Collection collection) {

                    result.addAll(collection);
                    downLatch.countDown();
                }

                @Override
                public void onFailure(Throwable throwable) {

                    LOGGER.warn("读取出错",throwable);
                    downLatch.countDown();
                }
            },MoreExecutors.directExecutor());

            start=end;
        }

        downLatch.await();
        return result;
    }
}
