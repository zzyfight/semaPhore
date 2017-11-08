package com.zhangzy;

import java.util.concurrent.Semaphore;

public class Main {
    private static int count;
    static Semaphore semCon = new Semaphore(0);
    static Semaphore semProd = new Semaphore(1);

    public static void consume() {
        try{
            semCon.acquire();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        count--;
        System.out.println("consume"+ count);
        semProd.release();
    }

    public static void produce() {
        try{
            semProd.acquire();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        count++;
        System.out.println("produce"+ count);
        semCon.release();
    }

    public static void main(String[] args) {
	// write your code here
        final Main pc = new Main();
        Runnable producer = new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 3; i++){
                    pc.produce();
                }
            }
        };

        Runnable consumer = new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 3; i++){
                    pc.consume();
                }
            }
        };
        Thread p = new Thread(producer);
        Thread c = new Thread(consumer);
        p.start();
        c.start();
    }
}
