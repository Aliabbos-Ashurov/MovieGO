package com.abbos.moviego.config;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-23
 */
@SpringBootTest(classes = { RabbitMQConfig.class })
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
class RabbitMQConfigTest {

    @Autowired
    private Queue emailQueue;

    @Autowired
    private TopicExchange emailExchange;

    @Autowired
    private Binding emailBinding;

    @Autowired
    private MessageConverter jsonMessageConverter;

    @Autowired
    private RabbitTemplate amqpTemplate;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Test
    void testEmailQueueBean() {
        assertNotNull(emailQueue, "Queue bean should not be null");
        assertEquals("notification.mail", emailQueue.getName(), "Queue name should match");
        assertTrue(emailQueue.isDurable(), "Queue should be durable");
    }

    @Test
    void testEmailExchangeBean() {
        assertNotNull(emailExchange, "Exchange bean should not be null");
        assertEquals("notification-events", emailExchange.getName(), "Exchange name should match");
    }

    @Test
    void testEmailBindingBean() {
        assertNotNull(emailBinding, "Binding bean should not be null");
        assertEquals("notification.mail", emailBinding.getDestination(), "Binding destination should match queue name");
        assertEquals("notification-events", emailBinding.getExchange(), "Binding exchange should match exchange name");
        assertEquals("mail.sent", emailBinding.getRoutingKey(), "Binding routing key should match");
    }

    @Test
    void testJsonMessageConverterBean() {
        assertNotNull(jsonMessageConverter, "MessageConverter bean should not be null");
        assertInstanceOf(Jackson2JsonMessageConverter.class, jsonMessageConverter,
                         "MessageConverter should be Jackson2JsonMessageConverter");
    }

    @Test
    void testAmqpTemplateBean() {
        assertNotNull(amqpTemplate, "RabbitTemplate bean should not be null");
        assertNotNull(amqpTemplate.getConnectionFactory(), "RabbitTemplate should have ConnectionFactory");
        assertInstanceOf(Jackson2JsonMessageConverter.class, amqpTemplate.getMessageConverter(),
                         "RabbitTemplate should use Jackson2JsonMessageConverter");
    }

    @Test
    void testAmqpTemplateConfiguration() {
        assertEquals(connectionFactory, amqpTemplate.getConnectionFactory(),
                     "RabbitTemplate should use the provided ConnectionFactory");
        assertEquals(jsonMessageConverter, amqpTemplate.getMessageConverter(),
                     "RabbitTemplate should use the provided MessageConverter");
    }
}