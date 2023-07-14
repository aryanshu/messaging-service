package com.aryanshu.code.config;

import com.aryanshu.code.dto.UserSession;
import com.aryanshu.code.repository.SessionRepository;
import com.netflix.appinfo.InstanceInfo;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig  implements WebSocketMessageBrokerConfigurer {

    private List<String> connectedUsers = new ArrayList<>();

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private SessionRepository sessionRepository;

//    private SimpMessageSendingOperations messageTemplate;


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.enableSimpleBroker("/topic");
        registry
                .setApplicationDestinationPrefixes("/app")
                .enableStompBrokerRelay("/topic")
                .setRelayHost("localhost")
                .setRelayPort(61613)
                .setClientLogin("guest")
                .setClientPasscode("guest");
    }


    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptorAdapter() {

            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String userId = accessor.getFirstNativeHeader("senderId"); // Extract username from headers
                    log.info(userId);
                    connectedUsers.add(userId);
                    String sessionId = accessor.getSessionId(); // Extract session ID
//                    Session session = (Session) accessor.getSessionAttributes().get("session"); // Extract session object

                    log.info("sessionId: "+sessionId);
                    log.info("userId:"+userId);
//                    log.info(("session: "+session.toString()));

                    String Instance = "messaging-service:8088";

                    UserSession userSession = new UserSession(userId,Instance,sessionId);
                    sessionRepository.save(userSession);
                    List<ServiceInstance> instances = discoveryClient.getInstances(Instance.split(":")[0]);

                    String topic = "/topic/private/" + userId;
//                    messageTemplate.convertAndSend(topic,"topic created");

                    if (!instances.isEmpty()) {
                        for (ServiceInstance instance : instances) {
                            log.info("instance"+instance);
                            if (Integer.parseInt(Instance.split(":")[1]) == instance.getPort()) {
                                log.info("final instance"+instance);
                            }
                        }
                    }

                    System.out.println("Connected Users: " + connectedUsers);
                }

                return message;
            }
        });
    }




}
