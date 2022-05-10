package watcherz.authenticationservice.messaging;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import watcherz.authenticationservice.model.RabbitUser;

@Service
public class RabbitMQSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Value("${watcherz.rabbitmq.exchange}")
    private String exchange;

    @Value("${watcherz.rabbitmq.routingkey}")
    private String routingkey;

    public void send(RabbitUser user) {
        rabbitTemplate.convertAndSend(exchange, routingkey, user);
        System.out.println("Send msg = " + user);
    }
}