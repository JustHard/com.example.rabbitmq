package com.example.rabbitmq.three;

import com.example.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: caolingyun
 * @Date: 2021/07/29 22:40
 */
public class Task02 {
    //队列名称
    public static final String QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        //获取信道
        Channel channel = RabbitMqUtils.getChannel();

        //生成一个队列
        // 参数1：队列名称；
        // 参数2：队列里面的消息是否持久化，默认情况消息存储在内存中即不持久化；
        // 参数3：该队列是否只提供一个消费者消费，是否进行消息共享，true可以多个消费者消费，false只能一个消费者消费
        // 参数4：是否自动删除 最后一个消费者端开启连接以后，该队列是否自动删除，true自动删除，false不自动删除
        // 参数5：其他参数

        //需要让队列持久化
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);

        //从控制台中接收信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            //MessageProperties.PERSISTENT_TEXT_PLAIN 消息持久化
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
            System.out.println("发送消息完成:" + message);

        }
    }
}
