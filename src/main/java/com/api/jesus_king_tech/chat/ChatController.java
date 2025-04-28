package com.api.jesus_king_tech.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final MistralService mistralService;

    @Autowired
    public ChatController(MistralService mistralService) {
        this.mistralService = mistralService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, List<ChatMessage>> request) {
        List<ChatMessage> messages = request.get("messages");
        String response = mistralService.generateChatResponse(messages);
        return ResponseEntity.ok(Map.of("response", response));
    }

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamChat(@RequestBody Map<String, List<ChatMessage>> request) {
        List<ChatMessage> messages = request.get("messages");
        return mistralService.streamChatResponse(messages);
    }
}