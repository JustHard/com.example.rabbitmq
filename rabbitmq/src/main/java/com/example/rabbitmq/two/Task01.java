package com.example.rabbitmq.two;

import com.rabbitmq.client.Channel;

import java.util.Scanner;

import static com.example.rabbitmq.utils.RabbitMqUtils.getChannel;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: caolingyun
 * @Date: 2021/07/29 22:14
 * <p>
 * 生产者 发送大量的消息
 */
public class Task01 {

    //队列名称
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        //获取信道
        Channel channel = getChannel();

        //生成一个队列
        // 参数1：队列名称；
        // 参数2：队列里面的消息是否持久化，默认情况消息存储在内存中即不持久化；
        // 参数3：该队列是否只提供一个消费者消费，是否进行消息共享，true可以多个消费者消费，false只能一个消费者消费
        // 参数4：是否自动删除 最后一个消费者端开启连接以后，该队列是否自动删除，true自动删除，false不自动删除
        // 参数5：其他参数
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //从控制台中接收信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("发送消息完成:" + message);

        }
    }
}
