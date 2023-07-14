package com.aryanshu.code.util;

import com.aryanshu.code.dto.UserSession;
import com.aryanshu.code.repository.InstanceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    CommandLineRunner commandLineRunner(InstanceRepository repository){
        return args -> {
            UserSession userSession = new UserSession("1","33","11");
            repository.save(userSession);
        };
    }
}
