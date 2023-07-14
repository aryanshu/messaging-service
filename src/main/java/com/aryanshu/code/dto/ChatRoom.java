package com.aryanshu.code.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ChatRoom {
    @SequenceGenerator(
            name = "chat_message_sequence",
            sequenceName = "chat_message_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "chat_message_sequence"
    )
    private Long id;
    private String chatId;
    private Long senderId;
    private Long recipientId;
}
