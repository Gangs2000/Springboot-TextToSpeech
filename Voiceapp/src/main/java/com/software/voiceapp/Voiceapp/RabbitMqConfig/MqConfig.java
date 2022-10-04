package com.software.voiceapp.Voiceapp.RabbitMqConfig;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfig {
    public static final String QUEUE1="voiceapp-signup";
    public static final String QUEUE2="voiceapp-forgotpin";
    public static final String EXCHANGE="text-to-speech-producer";
    public static final String ROUTING_KEY1="signup-routing-key";
    public static final String ROUTING_KEY2="forgotpin-routing-key";

    @Bean
    public Queue queue1(){
        return new Queue(QUEUE1);
    }

    @Bean
    public Queue queue2(){
        return new Queue(QUEUE2);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding1(Queue queue1, TopicExchange topicExchange){
        return BindingBuilder.bind(queue1())
                .to(topicExchange)
                .with(ROUTING_KEY1);
    }

    @Bean
    public Binding binding2(Queue queue2, TopicExchange topicExchange){
        return BindingBuilder.bind(queue2())
                .to(topicExchange)
                .with(ROUTING_KEY2);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate=new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
