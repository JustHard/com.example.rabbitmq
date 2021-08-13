package com.example.rabbitmq.four;

import com.example.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: caolingyun
 * @Date: 2021/07/30 10:48
 * <p>
 * 1.单个确认发布
 * 2。批量确认发布
 * 3。异步确认发布
 */
public class ConfirmMessage {

    //批量发消息的个数
    public static final int MESSAGE_COUNT = 1000;


    public static void main(String[] args) throws Exception {
        //1.单个确认发布
        //publishMessageIndividually();//发布1000条单独确认消息，用时：3789ms
        //2。批量确认发布
        //publishMessageBatch(); //发布1000条批量确认消息，用时：184ms
        // 3。异步确认发布
        publishMessageAsync(); //发布1000条异步确认消息，用时：84ms
    }

    //1.单个确认发布
    public static void publishMessageIndividually() throws Exception {
        //获取信道
        Channel channel = RabbitMqUtils.getChannel();
        //队列名称
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        //批量发消息 单个发布确认
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = String.valueOf(i);
            channel.basicPublish("", queueName, null, message.getBytes("UTF-8"));
            //单个消息就马上进行发布确认
            boolean b = channel.waitForConfirms();
            if (b) {
                System.out.println("消息发送成功！");
            }
        }
        //结束时间
        long end = System.currentTimeMillis();

        System.out.println("发布" + MESSAGE_COUNT + "条单独确认消息，用时：" + (end - begin) + "ms");
    }

    //2.批量确认发布
    public static void publishMessageBatch() throws Exception {
        //获取信道
        Channel channel = RabbitMqUtils.getChannel();
        //队列名称
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        //批量确认消息大小
        int batchSize = 100;

        //批量发消息 批量发布确认
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = String.valueOf(i);
            channel.basicPublish("", queueName, null, message.getBytes("UTF-8"));
            //间隔100条消息的时候 批量确认一次
            if (i % batchSize == 0) {
                //发布确认
                channel.waitForConfirms();
            }
        }
        //结束时间
        long end = System.currentTimeMillis();

        System.out.println("发布" + MESSAGE_COUNT + "条批量确认消息，用时：" + (end - begin) + "ms");
    }

    // 3。异步确认发布
    public static void publishMessageAsync() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //队列名称
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();

        /**
         * 线程安全有序的一个hash表 适用于高并发的情况下
         * 1.轻松的将序号和消息进行关联
         * 2。轻松的批量删除条目 只要给到需序号
         * 3。支持高并发（多线程）
         */
        ConcurrentSkipListMap<Long, String> outsandingConfirms = new ConcurrentSkipListMap<>();

        //消息确认成功 回调函数
        /**
         * 1。delivery：消息的标记
         * 2。multipe：是否为批量确认
         */
        ConfirmCallback ackCallback = (delivery, multipe) -> {
            //如果是批量
            if (multipe) {
                //2。删除已确认的消息，剩下的就是未确认的消息
                ConcurrentNavigableMap<Long, String> confirmed = outsandingConfirms.headMap(delivery);
                confirmed.clear();
            } else {
                outsandingConfirms.remove(delivery);
            }
            System.out.println("确认的消息：" + delivery);
        };
        //消息确认失败 回调函数
        ConfirmCallback nackCallback = (delivery, multipe) -> {
            //3。打印下未确认的消息
            String message = outsandingConfirms.get(delivery);
            System.out.println("未确认的消息是：" + message+"：：：：：：未确认的消息标记是：" + delivery);
        };
        //准备消息的监听器，监听哪些消息成功了，哪些消息失败了
        channel.addConfirmListener(ackCallback, nackCallback);

        //开始时间
        long begin = System.currentTimeMillis();

        //批量发消息 批量发布确认
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = String.valueOf(i);
            channel.basicPublish("", queueName, null, message.getBytes("UTF-8"));

            //1.此处记录下所有要发送的消息 消息的总和
            outsandingConfirms.put(channel.getNextPublishSeqNo(), message);
        }
        //结束时间
        long end = System.currentTimeMillis();

        System.out.println("发布" + MESSAGE_COUNT + "条异步确认消息，用时：" + (end - begin) + "ms");
    }
}
