package com.aryanshu.code.repository;

import com.aryanshu.code.dto.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<ChatMessage,Long> {

}
