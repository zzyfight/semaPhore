package com.zhangzy;
import java.util.concurrent.Semaphore;
public class Test {
    int count = 0;
    final Semaphore put = new Semaphore(5);// 初始令牌个数
    final Semaphore get = new Semaphore(0);
    final Semaphore mutex = new Semaphore(1);

    public static void main(String[] args) {
        Test t = new Test();
        new Thread(t.new Producer()).start();
        new Thread(t.new Consumer()).start();
        new Thread(t.new Consumer()).start();
        new Thread(t.new Producer()).start();
    }

    class Producer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    put.acquire();// 注意顺序
                    mutex.acquire();
                    count++;
                    System.out.println("生产者" + Thread.currentThread().getName()
                            + "已生产完成，商品数量：" + count);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    mutex.release();
                    get.release();
                }

            }

        }
    }

    class Consumer implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                try {
                    get.acquire();// 注意顺序
                    mutex.acquire();
                    count--;
                    System.out.println("消费者" + Thread.currentThread().getName()
                            + "已消费，剩余商品数量：" + count);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    mutex.release();
                    put.release();
                }
            }
        }
    }
}