package com.project.telegram.client;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserClient {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public Boolean getUserByPhone(String phone) {
        String url = "http://localhost:8080/isUserExists/" + phone;
        return restTemplate().getForObject(url, Boolean.class);
    }

    public Integer getTransactionUserByPhone(String phone) {
        String url = "http://localhost:8080/getTotalTransactionAmount/" + phone;
        return restTemplate().getForObject(url, Integer.class);
    }
}
