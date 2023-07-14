package com.aryanshu.code.repository;

import com.aryanshu.code.dto.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
    Optional<ChatRoom> findBySenderIdAndRecipientId(Long senderId, Long recipientId);
}
