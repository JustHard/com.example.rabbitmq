package com.example.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: caolingyun
 * @Date: 2021/07/29 21:56
 *
 * 连接工厂创建信道的工具类
 */
public class RabbitMqUtils {

    public static Channel getChannel() throws Exception {
        //创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();

        //工厂ip 连接rabbitmq的队列
        factory.setHost("localhost");

        //设置端口
        factory.setPort(5672);

        //用户名
        factory.setUsername("admin");

        //密码
        factory.setPassword("123456");

        //rabbitmq默认虚拟机名称为“/”，虚拟机相当于一个独立的mq服务器
        factory.setVirtualHost("/");

        //创建连接
        Connection connection = factory.newConnection();

        //获取信道
        Channel channel = connection.createChannel();
        return channel;
    }
}
