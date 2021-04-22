package com.geyao.manager.common.web.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class ForkJoinPoolTest {

    //private static ForkJoinPool forkJoinPool = new ForkJoinPool(30, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);
    //private static ForkJoinPool forkJoinPool1 = ForkJoinPool.commonPool();

    public static List<JSONObject> getData(){
        List<JSONObject> list = new ArrayList<>();
        for(int i =0;i<9600;i++){
            JSONObject obj = new JSONObject();
            for(int j =0;j<50;j++){
                obj.put(i+"key"+j,"value是我！我是测试"+j);
            }
            list.add(obj);
        }
        return list;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Runtime r = Runtime.getRuntime();
        r.gc();
        System.out.println(r.maxMemory()/1024/1024);
        System.out.println("start：totalM["+(r.totalMemory()/1024/1024) + "] freeM["+(r.freeMemory()/1024/1024)+"] used【"+((r.totalMemory()-r.freeMemory())/1024/1024)+"】");
        int l = 300;
        long[] used = new long[l];
        AverageTask at = null;
        ForkJoinPool forkJoinPool = new ForkJoinPool(48, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);
        for (int i=0;i<l;i++){
            at = new AverageTask(getData(),0);
            log.info("结束用时：{}ms",forkJoinPool.submit(at).get());
            at.clear();
            System.out.println("【"+i+"】：totalM["+(r.totalMemory()/1024/1024) + "] freeM["+(r.freeMemory()/1024/1024)+"] used【"+((r.totalMemory()-r.freeMemory())/1024/1024)+"】");
            used[i] = ((r.totalMemory()-r.freeMemory())/1024/1024);
            r.gc();
        }
        forkJoinPool.shutdown();
        System.out.println("End：totalM["+(r.totalMemory()/1024/1024) + "] freeM["+(r.freeMemory()/1024/1024)+"] used【"+((r.totalMemory()-r.freeMemory())/1024/1024)+"】");
        System.out.println("平均 used："+(Arrays.stream(used).average().getAsDouble()));
        System.out.println("最大 used："+(Arrays.stream(used).max().toString()));
        System.out.println("最小 used："+(Arrays.stream(used).min().toString()));
        r.gc();
        Thread.sleep(1000*60);
        System.out.println("3 minits：totalM["+(r.totalMemory()/1024/1024) + "] freeM["+(r.freeMemory()/1024/1024)+"] used【"+((r.totalMemory()-r.freeMemory())/1024/1024)+"】");
    }

    /**
     * 二分法
     */
    public static class BlockChainTask extends RecursiveTask<Long> {



        private int startIndex;
        private int endIndex;


        private List<JSONObject> data;

        public BlockChainTask(List<JSONObject> data) {
            this.startIndex = 0;
            this.data = data;
            this.endIndex = data.size();
        }

        public BlockChainTask(List<JSONObject> data,int startIndex,int endIndex) {
            this.startIndex = startIndex;
            this.data = data;
            this.endIndex = endIndex;
        }


        @Override
        protected Long compute() {
            if(200 < (endIndex - startIndex)){
                log.info("{} - {} : {}条数据调用二分...",startIndex,endIndex,(endIndex - startIndex));
                //获取任务数量索引的中间值

                int x = (startIndex + endIndex) / 2;
                //拆分任务
                BlockChainTask work1 = new BlockChainTask(data,startIndex,x);
                work1.fork();
                //拆分任务
                BlockChainTask work2 = new BlockChainTask(data,x,endIndex);
                work2.fork();
                //获取任务执行结果
                return work1.join() + work2.join();
            }else{
                Instant start = Instant.now();
                log.info("{} - {} : {}条数据开始处理@",startIndex,endIndex,(endIndex - startIndex));
               data.stream().forEach(s ->{
                   s.put("isDeal",true);
                   try {
                       Thread.sleep(1);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               });
                log.info("{} - {} : {}条数据处理完成！",startIndex,endIndex,(endIndex - startIndex));
               return Duration.between(start, Instant.now()).toMillis();
            }
        }

    }


    /**新版
     * 均分处理
     */

    public static class AverageTask extends RecursiveTask<Long> {


        private List<JSONObject> data; //上链数据

        //上链记录批次
        int no;

        public AverageTask(List<JSONObject> data,int no) {
            this.data = data;
            this.no = no;
        }



        @Override
        protected Long compute() {
            AtomicLong al = new AtomicLong(0);
            if(data.size() > 200){
                List<List<JSONObject>>  r = averageAssign(data,48);
                List<AverageTask> taskResList = new ArrayList<>();
                for(int i = 0; i < r.size(); i++){
                    AverageTask averageTask =  new AverageTask(r.get(i),i+1);
                    averageTask.fork();
                    taskResList.add(averageTask);
                };
                taskResList.stream().forEach(o -> {
                    al.addAndGet(o.join());
                });
                r.clear();
                taskResList.clear();
                log.info("batch结束用时：{}ms",al.get());
                return al.get();
            }else {
                Instant start = Instant.now();
                //log.info("{}次数据开始处理@",no);
                data.stream().forEach(s ->{
                    s.put("isDeal",true);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                );
                //log.info("{}次数据处理完成！",no);
                //return Duration.between(start, Instant.now()).toMillis();
                return 1l;
            }
        }

        public void clear() {
            data.clear();
            no = 0;
        }
    }

    public static <T> List<List<T>> averageAssign(List<T> list,int n) {
        List<List<T>> result = new ArrayList<List<T>>();
        int remaider = list.size() % n;  //(先计算出余数)
        int number = list.size() / n;  //然后是商
        int offset = 0;//偏移量
        for (int i = 0; i < n; i++) {
            List<T> value = null;
            if (remaider > 0) {
                value = list.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            } else {
                value = list.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }
}
