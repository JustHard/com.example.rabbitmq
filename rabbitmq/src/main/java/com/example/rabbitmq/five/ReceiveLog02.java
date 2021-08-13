package com.example.rabbitmq.five;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: caolingyun
 * @Date: 2021/07/30 14:17
 */

import com.example.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * fanout交换机方式
 * 接受消息方2
 */
public class ReceiveLog02 {
    //交换机的名称
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        //声明一个队列 临时队列
        /**
         * 生成一个临时队列、队列的名称是随机的
         * 当消费者断开与队列的连接，队列就自动删除
         */

        String queueName = channel.queueDeclare().getQueue();

        /**
         * 绑定交换机与队列
         */
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println("ReceiveLog02等待接收消息，把接收到的消息打印在屏幕上。。。。");

        //接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLog02控制台打印接收到的消息" + new String(message.getBody(), "utf-8"));
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });

    }
}
