package com.aryanshu.code.chat;


import com.aryanshu.code.dto.ChatMessage;
import com.aryanshu.code.dto.UserSession;
import com.aryanshu.code.repository.SessionRepository;
import com.aryanshu.code.services.ChatRoomService;
import com.aryanshu.code.services.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.util.Optional;


@Controller
@Slf4j
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private SimpMessageSendingOperations messageTemplate;

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;


    @MessageMapping("/chat.sendMessage")
    public void processMessage(@Payload ChatMessage chatMessage) {
        log.info(chatMessage.toString());
        var chatId = chatRoomService.getChatId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true);
        chatMessage.setChatId(chatId.get());
        log.info(chatMessage.toString());

        Optional<UserSession> userSession = sessionRepository.findById(String.valueOf(chatMessage.getRecipientId()));

        chatService.save(chatMessage);
        String destination = "/topic/" +String.valueOf(chatMessage.getRecipientId());

        if(userSession.isPresent()){
            messageTemplate.convertAndSend(destination, chatMessage.getContent());
        }
        else{
            String routingKey = "user."+String.valueOf(chatMessage.getRecipientId());
            System.out.println(routingKey);
            rabbitTemplate.convertAndSend("amq.topic", routingKey, chatMessage.getContent());
        }

    }





}
