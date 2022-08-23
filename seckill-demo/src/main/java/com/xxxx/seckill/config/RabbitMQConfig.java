package com.xxxx.seckill.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//RabbitMQ配置类,用来准备我们的队列。
//@Configuration
public class RabbitMQConfig {

//    private static final String QUEUE01 = "queue_fanout01";
//    private static final String QUEUE02 = "queue_fanout02";
//    private static final String EXCHANGE = "fanoutExchange";
//
//    @Bean
//    public Queue queue() {
//        return new Queue("queue", true);
//    }
//    //fanout
//    @Bean
//    public Queue queue01() {
//        return new Queue(QUEUE01);
//    }
//    @Bean
//    public Queue queue02() {
//        return new Queue(QUEUE02);
//    }
//    @Bean
//    public FanoutExchange fanoutExchange() {
//        return new FanoutExchange(EXCHANGE);
//    }
//    //将队列绑定到交换机
//    @Bean
//    public Binding binding01() {
//        return BindingBuilder.bind(queue01()).to(fanoutExchange());
//    }
//    @Bean
//    public Binding binding02() {
//        return BindingBuilder.bind(queue02()).to(fanoutExchange());
//    }
//    //fanout And Direct
//    private static final String DQUEUE01 = "queue_direct01";
//    private static final String DQUEUE02 = "queue_direct02";
//    private static final String DEXCHANGE = "directExchange";
//    private static final String ROUTINGKEY01 = "queue.red";
//    private static final String ROUTINGKEY02 = "queue.green";
//
//    @Bean
//    public Queue dqueue01() {
//        return new Queue(DQUEUE01);
//    }
//    @Bean
//    public Queue dqueue02() {
//        return new Queue(DQUEUE02);
//    }
//    @Bean
//    public DirectExchange directExchange() {
//        return new DirectExchange(DEXCHANGE);
//    }
//    @Bean
//    public Binding dbinding01() {
//        return BindingBuilder.bind(queue01()).to(directExchange()).with(ROUTINGKEY01);
//    }
//    @Bean
//    public Binding dbinding02() {
//        return BindingBuilder.bind(queue02()).to(directExchange()).with(ROUTINGKEY02);
//    }
}

