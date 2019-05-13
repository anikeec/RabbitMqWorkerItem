/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.rabbitmqworkeritem;

import com.apu.rabbitmqworkeritem.rabbitmq.WorkerSubscriber;
import com.apu.rabbitmqworkeritem.rabbitmq.WorkerPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author apu
 */
public class Main {
    
    private static final String TO_SERVER_QUEUE_NAME = "to_server_queue";
    private static final String TO_CLIENT_QUEUE_NAME = "to_client_queue";
    
    private static final String TO_SERVER_EXCHANGE_NAME = "toServerExchange";
    private static final String TO_CLIENT_EXCHANGE_NAME = "toClientExchange";
    
    private static final String ROUTING_KEY = "testRoute";
    
    public static void main(String[] args) throws IOException, TimeoutException {
        
        WorkerPublisher publisher = new WorkerPublisher(TO_CLIENT_EXCHANGE_NAME, ROUTING_KEY, TO_CLIENT_QUEUE_NAME);
        
        WorkerSubscriber subscriber = new WorkerSubscriber(TO_SERVER_EXCHANGE_NAME, ROUTING_KEY, TO_SERVER_QUEUE_NAME);
        
        subscriber.addPublisher(publisher);
        subscriber.run();
        
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setUsername("guest");
//        factory.setPassword("guest");
//        factory.setVirtualHost("/");
//        factory.setHost("127.0.0.1");
//        factory.setPort(5672);
//        Connection conn = factory.newConnection();
//        Channel channel = conn.createChannel();
//        String exchangeName = "toServerExchange";
//        String routingKey = "testRoute";
//        boolean durable = true;
//        channel.exchangeDeclare(exchangeName, "direct", durable);
//        channel.queueDeclare(TO_SERVER_QUEUE_NAME, durable, false, false, null);
//        channel.queueBind(TO_SERVER_QUEUE_NAME, exchangeName, routingKey);
//        Consumer consumer = new DefaultConsumer(channel) {
//            @Override
//             public void handleDelivery(String consumerTag,
//                                        Envelope envelope, 
//                                        AMQP.BasicProperties properties, 
//                                        byte[] body) throws IOException {
//
//                    String message = new String(body, "UTF-8");
//                    SymbolCounter sc = new SymbolCounter();
//                    Map<Character,Integer> resultMap = sc.countSymbols(message);
//                    ObjectMapper mapper = new ObjectMapper();
//                    String jsonResult = mapper.writerWithDefaultPrettyPrinter()
//                                                .writeValueAsString(resultMap);
//                    System.out.println(jsonResult);
//                    // process the message
//             }
//        };
//        channel.basicConsume(QUEUE_NAME, true, consumer);
//        channel.close();
//        conn.close();
    }
    
}
