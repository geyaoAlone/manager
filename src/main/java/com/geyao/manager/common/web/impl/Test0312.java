package com.geyao.manager.common.web.impl;



import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

@Slf4j
public class Test0312 {

    private static int THREAD_MAX = 48;//并发最大线程数
    //private static int THREAD_DEAL_DATA_MAX = 200;//单线程处理最大条数
    private static int QUERY_DATA_MAX = 9600;//单个查询最多条数

    public static class BlockChainTask extends RecursiveTask<String> {

        private String token;    //token

        private int startIndex;
        private int endIndex;

        private List<String> data;

        public BlockChainTask(List<String> data) {
            this.startIndex = 0;
            this.data = data;
            this.endIndex = data.size();
        }

        public BlockChainTask(List<String> data,int startIndex,int endIndex) {
            this.startIndex = startIndex;
            this.data = data;
            this.endIndex = endIndex;
        }


        @Override
        protected String compute() {
            log.info("start...");
            log.info("data size：{}",data.size());

            for(int i = 0;i<data.size();i++){
                try {
                    Thread.sleep(3000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            log.info("end...");
            return data.size()+"";
        }
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        for (int i = 0;i<QUERY_DATA_MAX;i++){
            list.add("test"+i);
        }
        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_MAX, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);
        List<List<String>>  r = averageAssign(list,THREAD_MAX);
        System.out.println(r.size());
        r.stream().forEach(list1 ->{
            ForkJoinTask<String> fj =  forkJoinPool.submit(new BlockChainTask(list1));
        });

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
