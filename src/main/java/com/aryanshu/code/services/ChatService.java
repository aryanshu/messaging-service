package com.aryanshu.code.services;

import com.aryanshu.code.dto.ChatMessage;
import com.aryanshu.code.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    @Autowired
    ChatRepository chatRepository;

    public void save(ChatMessage chatMessage) {
        chatRepository.save(chatMessage);
    }
}
