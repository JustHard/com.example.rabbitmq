package com.example.rabbitmq.one;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.rabbitmq.utils.RabbitMqUtils.getChannel;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: caolingyun
 * @Date: 2021/07/29 18:11
 * <p>
 * 生产者：发送消息
 */

public class Producer {

    //队列名称
    public static final String QUEUE_NAME = "hello";

    //发信息
    public static void main(String[] args) throws Exception {
        //获取信道
        Channel channel = getChannel();

        Map<String, Object> arguments = new HashMap<>();
        //优先级参数 官方允许是0-255之间 此处设置10，允许优先级范围为0-10，不要设置过大 浪费cpu和内存
        arguments.put("x-max-priority", 10);

        //生成一个队列
        // 参数1：队列名称；
        // 参数2：队列里面的消息是否持久化，默认情况消息存储在内存中即不持久化；
        // 参数3：该队列是否只提供一个消费者消费，是否进行消息共享，true可以多个消费者消费，false只能一个消费者消费
        // 参数4：是否自动删除 最后一个消费者端开启连接以后，该队列是否自动删除，true自动删除，false不自动删除
        // 参数5：其他参数
        channel.queueDeclare(QUEUE_NAME, true, false, false, arguments);


        /**
         * 发送一个消息 basicPublish
         * 参数1。发送到哪个交换机
         * 参数2。路由的key值是哪个 本次是队列的名称
         * 参数3。其他参数信息
         * 参数4。发送信息的消息体
         */

        for (int i = 1; i < 11; i++) {
            //发消息
            String message = "hello world!" + i + ":::::" + new Date();
            if (i == 5) {
                AMQP.BasicProperties props = new AMQP.BasicProperties().builder().priority(5).build();

                channel.basicPublish("", QUEUE_NAME, props, message.getBytes());
            } else {
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            }
        }
        System.out.println("消息发送完毕！");
    }
}
