package com.example.rabbitmq.one;

import com.rabbitmq.client.*;

import static com.example.rabbitmq.utils.RabbitMqUtils.getChannel;


/**
 * Created with IntelliJ IDEA.
 *
 * @Author: caolingyun
 * @Date: 2021/07/29 21:38
 * <p>
 * 消费者 接受消息
 */
public class Consumer {

    //队列名称
    public static final String QUEUE_NAME = "hello";

    //接受消息
    public static void main(String[] args) throws Exception {

        //获取信道
        Channel channel = getChannel();

        /**
         * 消费者消费消息
         * 参数1：消费哪个队列
         * 参数2：消费成功后是否自动应答 true 代表自动应答 false 代表手动应答
         * 参数3：消费未成功消费的回调
         * 参数4：消费者取消消费的回调
         *
         */

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody()));
        };

        CancelCallback cancelCallback = message -> {
            System.out.println("消息消费被中断了！");
        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback,cancelCallback);
    }

}
