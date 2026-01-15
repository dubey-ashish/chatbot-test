package com.open.chatbottest.controller;

import com.open.chatbottest.advisors.SimpleLoggerAdvisor;
import com.open.chatbottest.dto.ActorFilms;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LlmController {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    // You initialize the ChatClient with the foundation of communication. Things like temp, models, tokens can be specified here
    public LlmController(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        this.chatMemory = MessageWindowChatMemory.builder().build();
        this.chatClient = chatClientBuilder
                .defaultAdvisors(List.of(new SimpleLoggerAdvisor(), MessageChatMemoryAdvisor.builder(chatMemory).build()))
                .defaultSystem("You are a considerate friend")
                .build();
    }

    private static final Logger log =
            LoggerFactory.getLogger(LlmController.class);

    // This is just communication after you've already built the foundational structure
//    @GetMapping("/ai")
//    ResponseEntity<ActorFilms> generation(
//            @RequestParam("userInput") String userInput,
//            @RequestParam("conversationId") String conversationId) {
//        return ResponseEntity.ok(this.chatClient.prompt()
//                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
//                .user(userInput)
//                .call()
//                .entity(ActorFilms.class));
//    }

    @GetMapping("/ai")
    ResponseEntity<String> generation(
            @RequestParam("userInput") String userInput,
            @RequestParam("conversationId") String conversationId) {
        return ResponseEntity.ok(this.chatClient.prompt()
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .user(userInput)
                .call()
                .content());
    }
}
