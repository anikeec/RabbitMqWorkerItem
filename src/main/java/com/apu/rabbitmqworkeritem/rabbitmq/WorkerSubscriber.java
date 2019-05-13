/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.rabbitmqworkeritem.rabbitmq;

import com.apu.rabbitmqworkeritem.SymbolCounter;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author apu
 */
public class WorkerSubscriber {
    
    private final String exchangeName;
    private final String routingKey;
    private final String queueName;
    private WorkerPublisher publisher;
    
    private ConnectionFactory factory;
    private Connection conn;
    private Channel channel;

    public WorkerSubscriber(String exchangeName, String routingKey, String queueName) {
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
        this.queueName = queueName;
    }
    
    public void addPublisher(WorkerPublisher publisher) {
        this.publisher = publisher;
    }
    
    public void run() throws IOException, TimeoutException {
        factory = new ConnectionFactory();
        factory.setUsername(RabbitMqSettings.USERNAME);
        factory.setPassword(RabbitMqSettings.PASSWORD);
        factory.setVirtualHost(RabbitMqSettings.VIRTUAL_HOST);
        factory.setHost(RabbitMqSettings.HOST);
        factory.setPort(RabbitMqSettings.PORT);
        conn = factory.newConnection();
        channel = conn.createChannel();
        boolean durable = true;
        channel.exchangeDeclare(exchangeName, "direct", durable);
        channel.queueDeclare(queueName, durable, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
             public void handleDelivery(String consumerTag,
                                        Envelope envelope, 
                                        AMQP.BasicProperties properties, 
                                        byte[] body) throws IOException {

                    String message = new String(body, "UTF-8");
                    SymbolCounter sc = new SymbolCounter();
                    Map<Character,Integer> resultMap = sc.countSymbols(message);
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonResult = mapper.writerWithDefaultPrettyPrinter()
                                                .writeValueAsString(resultMap);
                    System.out.println(jsonResult);
                    try {
                        if(publisher != null)
                            publisher.publishMessage(jsonResult);
                    } catch (TimeoutException ex) {
                        Logger.getLogger(WorkerSubscriber.class.getName()).log(Level.SEVERE, null, ex);
                    }
             }
        };
        channel.basicConsume(queueName, true, consumer);
    }
    
    public void close() {
        if(channel != null) { 
            try {
                channel.close();
            } catch (IOException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            } catch (TimeoutException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(conn != null) {
            try {
                conn.close();
            } catch (IOException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }
    
}
