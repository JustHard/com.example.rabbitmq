package com.example.rabbitmq.five;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: caolingyun
 * @Date: 2021/07/30 14:16
 */

import com.example.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * fanout交换机方式
 * 发送消息方 发消息给交换机
 */
public class Emitlog {
    //交换机的名称
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
            System.out.println("生产者发出消息：" + message);
        }

    }

}
