/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.rabbitmqworkeritem;

import com.apu.rabbitmqworkeritem.rabbitmq.RabbitMqSettings;
import com.apu.rabbitmqworkeritem.rabbitmq.WorkerSubscriber;
import com.apu.rabbitmqworkeritem.rabbitmq.WorkerPublisher;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author apu
 */
public class Main {
    
    
    
    public static void main(String[] args) throws IOException, TimeoutException {
        
        WorkerPublisher publisher = 
                new WorkerPublisher(RabbitMqSettings.TO_CLIENT_EXCHANGE_NAME, 
                                    RabbitMqSettings.ROUTING_KEY, 
                                    RabbitMqSettings.TO_CLIENT_QUEUE_NAME);
        
        WorkerSubscriber subscriber = 
                new WorkerSubscriber(RabbitMqSettings.TO_SERVER_EXCHANGE_NAME, 
                                    RabbitMqSettings.ROUTING_KEY, 
                                    RabbitMqSettings.TO_SERVER_QUEUE_NAME);
        
        subscriber.addPublisher(publisher);
        subscriber.run();

    }
    
}
