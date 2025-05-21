package com.abbos.moviego.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-20
 */
@EnableRabbit
@Configuration(proxyBeanMethods = false)
public class RabbitMQConfig {

    @Value("${rabbitmq.queue}")
    private String queueName;

    @Value("${rabbitmq.exchange}")
    private String exchangeName;

    @Value("${rabbitmq.routing-key}")
    private String routingKey;

    @Bean
    public Queue emailQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    public TopicExchange emailExchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Binding emailBinding(Queue emailQueue, TopicExchange emailExchange) {
        return BindingBuilder.bind(emailQueue).to(emailExchange).with(routingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    @DependsOn("jsonMessageConverter")
    public RabbitTemplate amqpTemplate(ConnectionFactory connectionFactory,
                                       MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
