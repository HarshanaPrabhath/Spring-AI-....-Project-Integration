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
                        "Respond ONLY with a valid JSON object that contains exactly one field: " +
                        "'responseText' which is the AI's answer to the user's input. " +
                        "Do not include any extra text, explanations, or keys. " +
                        "User input: %s",
                prompt
        );

        String response = chatClient.prompt().user(customizeMSG).call().content();

        if (response.contains("{")) {
            int start = response.indexOf("{");
            int end = response.lastIndexOf("}") + 1;
            response = response.substring(start, end);
        }


        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
