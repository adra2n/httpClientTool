package com.taolc.http;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReflectTest {

    public static int successNum = 0;
    public static int failNum = 0;

    public static void main(String[] args) {
        xpty();
    }

    /**
     * 新品体验
     */
    public static void xpty(){
        int sum = 500000;
        int num = 500;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(num);
        ExecutorService executorService = Executors.newFixedThreadPool(num);
        for(int i=0;i<sum;i++){
            executorService.submit(new ReflectThread(cyclicBarrier,i));
        }
        executorService.shutdown();
    }
}

class ReflectThread implements Runnable{

    private CyclicBarrier cyclicBarrier;
    private int i;

    ReflectThread(CyclicBarrier cyclicBarrier,int i){
        this.cyclicBarrier = cyclicBarrier;
        this.i = i;
    }

    @Override
    public void run() {
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        String url = "http://localhost:8080/common/queryAllDish";
        String response = HttpClientTool.post(url);
        if(response == null){
            ReflectTest.failNum++;
            System.out.println("失败次数"+ReflectTest.failNum);
        }else{
            ReflectTest.successNum++;
            System.out.println("成功次数"+ReflectTest.successNum);
        }
    }
}
