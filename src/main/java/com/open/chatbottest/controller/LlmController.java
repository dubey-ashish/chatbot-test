package com.open.chatbottest.controller;

import com.open.chatbottest.advisors.SimpleLoggerAdvisor;
import com.open.chatbottest.dto.ActorFilms;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
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
    // @Autowired is not needed anymore for newer versions of Spring to inject Bean to ChatClient.Builder chatClientBuilder; a bean which is already created in ChatClientAutoConfiguration class.
    // when you use builder(s) the same object is passed successively to next. Using Builder we kinda create and populate a temporary object and then use that object to populate the fields of main class's object
    // whenever you finally use the .build() its not the object of the static Builder class that gets build but the main class's object
    public LlmController(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        this.chatMemory = MessageWindowChatMemory.builder().maxMessages(10).build();
        this.chatClient = chatClientBuilder
                .defaultAdvisors(List.of(new SimpleLoggerAdvisor(), MessageChatMemoryAdvisor.builder(chatMemory).build()))
                .defaultSystem("You are a considerate friend")
                .build();   // returns DefaultChatClient.class object
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
        return ResponseEntity.ok(
                this.chatClient.prompt()
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .user(userInput)
                .call()
                .content());    // this calls result generating code block
    }
}
