package com.aryanshu.code.dto;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;


@Data
@Getter
@RedisHash("UserSession")
public class UserSession implements Serializable {
    @Id
    private String userId;

    public UserSession(String userId, String instanceId, String sessionId) {
        this.userId = userId;
        this.instanceId = instanceId;
        this.sessionId = sessionId;

    }

    private String sessionId;
    private String instanceId;

}
