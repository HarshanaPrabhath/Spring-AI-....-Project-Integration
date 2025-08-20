package com.chat.app.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @PostMapping("/ai")
    public ResponseEntity<String> chat(@RequestBody String prompt) {

        // Better, structured prompt
        String customizeMSG = String.format(
                "You are an assistant. " +
                        "Respond to the user input with a JSON object containing:" +
                        " 1) 'responseText': the AI's answer to the user's input," +
                        " 2) 'data': any structured data relevant to the answer." +
                        " Ensure the JSON is properly formatted and valid. dont include json part " +
                        "User input: %s",
                prompt
        );

        String response = chatClient.prompt().user(customizeMSG).call().content();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
