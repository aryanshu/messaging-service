package com.aryanshu.code.chat;


import com.aryanshu.code.dto.ChatMessage;
import com.aryanshu.code.dto.UserSession;
import com.aryanshu.code.repository.SessionRepository;
import com.aryanshu.code.services.ChatRoomService;
import com.aryanshu.code.services.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;


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

    @MessageMapping("/chat.sendMessage")
    public void processMessage(@Payload ChatMessage chatMessage) {
        log.info(chatMessage.toString());
        var chatId = chatRoomService.getChatId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true);
        chatMessage.setChatId(chatId.get());
        log.info(chatMessage.toString());

        UserSession userSession = sessionRepository.findById(String.valueOf(chatMessage.getRecipientId())).orElseThrow();
        log.info("userSession"+userSession.getSessionId());
        chatService.save(chatMessage);
        String destination = "/topic/" +String.valueOf(chatMessage.getRecipientId());
        messageTemplate.convertAndSend(destination,chatMessage.getContent());
//        messageTemplate.convertAndSendToUser(String.valueOf(chatMessage.getRecipientId()), destination, chatMessage.getContent());
    }





}
