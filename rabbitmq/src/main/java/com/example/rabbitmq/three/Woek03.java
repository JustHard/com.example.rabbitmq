package com.example.rabbitmq.three;

import com.example.rabbitmq.utils.RabbitMqUtils;
import com.example.rabbitmq.utils.SleepUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;


/**
 * Created with IntelliJ IDEA.
 *
 * @Author: caolingyun
 * @Date: 2021/07/29 22:46
 */
public class Woek03 {

    //队列名称
    public static final String QUEUE_NAME = "ack_queue";

    //接收消息
    public static void main(String[] args) throws Exception {
        //获取信道
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("c2等待接收消息处理时间较长！");

        DeliverCallback deliverCallback = (tag, message) -> {
            String msg = new String(message.getBody(),"UTF-8");
            SleepUtils.sleep(30);
            System.out.println("接收到的消息：" + msg);
            //1.消息标记tag 2。是否批量应答,false不批量应答信道中的消息 true批量应答信道中的消息
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };

        CancelCallback cancelCallback = tag -> {
            System.out.println(tag + "消费者取消消费接口回调逻辑！");
        };

        //设置不公平分发
//        channel.basicQos(1);

        //预取值
        channel.basicQos(5);

        System.out.println("c2等待接收消息......");

        //采用手动应答
        boolean autoAck=false;
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, cancelCallback);
    }
}
