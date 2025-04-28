package com.api.jesus_king_tech.chat;
import java.util.UUID;

public class ChatMessage {
    private String role;
    private String content;
    private String id;

    public ChatMessage() {
        this.id = UUID.randomUUID().toString();
    }

    public ChatMessage(String role, String content) {
        this.role = role;
        this.content = content;
        this.id = UUID.randomUUID().toString();
    }

    // Getters e setters
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}