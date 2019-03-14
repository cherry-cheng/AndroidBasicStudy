package com.weizhan.lib;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2019/3/14.
 */

public class MessageQueue {
    //存储Message的数组
    private Message[] items;

    //入队和出队的索引位置
    private int putIndex;
    private int takeIndex;

    //计数器
    private int count;

    private Lock lock;

    //条件变量
    private Condition notEmpty;
    private Condition notFull;

    public MessageQueue() {
        items = new Message[50];
        lock = new ReentrantLock(); //可重入锁
        notFull = lock.newCondition();
        notEmpty = lock.newCondition();
    }

    /**
     * 将消息加入队列，生产者
     * @param msg
     */
    public void enqueueMessage(Message msg) {
        try {
            lock.lock();
            //消费队列满了，等待消费
            while (count == 50) {//之所以用while是因为收到可以生产的解锁信号后，还要再判断是不是符合当前线程的消费流程
                notFull.await();
            }
            items[putIndex] = msg;
            putIndex = (++putIndex == items.length) ? 0 : putIndex;
            count++;

            //已经生成了，可以消费了（必须选用signalAll，通知所有线程可以消费了，不只是当前线程）
            notEmpty.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 取出队列中的消息，消费者
     * @return
     */
    public Message next() {
        Message msg = null;
        try {
            lock.lock();
            //消息队列空了，等待生产
            while (count == 0) {
                notEmpty.await();
            }

            msg = items[takeIndex];
            items[takeIndex] = null;
            takeIndex = (++takeIndex == items.length) ? 0 : takeIndex;
            count --;

            //已经消费了，可以继续生产了
            notFull.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return msg;
    }
}
