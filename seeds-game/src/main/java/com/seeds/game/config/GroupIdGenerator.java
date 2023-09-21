package com.seeds.game.config;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GroupIdGenerator {
    public String randomId(){
       return UUID.randomUUID().toString();
    }
}