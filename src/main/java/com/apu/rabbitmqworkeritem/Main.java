/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.rabbitmqworkeritem;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author apu
 */
public class Main {
    
    private static final String QUEUE_NAME = "products_queue";
    
    public static void main(String[] args) throws IOException, TimeoutException {
        
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("/");
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();
        String exchangeName = "myExchange";
        String routingKey = "testRoute";
        boolean durable = true;
        channel.exchangeDeclare(exchangeName, "direct", durable);
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        channel.queueBind(QUEUE_NAME, exchangeName, routingKey);
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
             public void handleDelivery(String consumerTag,
                                        Envelope envelope, 
                                        AMQP.BasicProperties properties, 
                                        byte[] body) throws IOException {

                    String message = new String(body, "UTF-8");
                    System.out.println(message);
                    // process the message
             }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
//        channel.close();
//        conn.close();
    }
    
}
