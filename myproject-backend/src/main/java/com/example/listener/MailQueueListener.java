package com.example.listener;

import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "mail")
public class MailQueueListener {
    @Resource
    JavaMailSender sender;

    @Value("${spring.mail.username}")
    String username;

    @RabbitHandler
    public void sendMailMessage(Map<String,Object> data){
        String email = (String) data.get("email");
        Integer code = (Integer)data.get("code");
        String type = (String)data.get("type");
        SimpleMailMessage message = switch (type){
            case "register" ->
                createMessage("Welcome to our website", "Your email verification code is:"+ code
                +", This verification code will be valid for 3 minutes. For your security, please do not share it with others. "
                        ,email);
            case "reset" ->
                createMessage("Password Reset Email","Hello, you are attempting to reset your payment password. Your verification code is: " + code
                + ", This verification code will be valid for 3 minutes. If this was not initiated by you, please ignore this message.", email);
            default -> null;
        };
        if(message == null) return;
        sender.send(message);

    }

    private SimpleMailMessage createMessage(String title, String content, String emial){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(title);
        message.setText(content);
        message.setTo(emial);
        message.setFrom(username);
        return message;
    }
}
