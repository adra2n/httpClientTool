package com.taolc.http;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DwkTest {

    public static int successNum = 0;
    public static int failNum = 0;

    public static void main(String[] args) {
        xpty();
    }

    /**
     * 新品体验
     */
    public static void xpty(){
        int sum = 5000;
        int num = 500;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(num);
        ExecutorService executorService = Executors.newFixedThreadPool(num);
        for(int i=0;i<sum;i++){
            executorService.submit(new DwkThread(cyclicBarrier,i));
        }
        executorService.shutdown();
    }
}

class DwkThread implements Runnable{

    private CyclicBarrier cyclicBarrier;
    private int i;

    DwkThread(CyclicBarrier cyclicBarrier,int i){
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
        String url = "http://test.qquser.mur.qq.com/tasks/home";
        String response = HttpClientTool.post(url);
        if(response == null){
            DwkTest.failNum++;
            System.out.println("失败次数"+DwkTest.failNum);
        }else{
            DwkTest.successNum++;
            System.out.println("成功次数"+DwkTest.successNum);
        }
    }
}
