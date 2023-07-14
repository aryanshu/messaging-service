package com.aryanshu.code.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ChatMessage {

    @SequenceGenerator(
            name = "chat_sequence",
            sequenceName = "chat_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "chat_sequence"
    )
    private Long id;
    private String chatId;
    private Long senderId;
    private Long recipientId;

    @Column(length = 1000)
    private String content;
    private String timestamp;
    private MessageStatus status;


}
