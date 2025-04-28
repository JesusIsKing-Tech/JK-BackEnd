package com.api.jesus_king_tech.chat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class MistralService {
    private static final String MISTRAL_API_URL = "https://api.mistral.ai/v1/chat/completions";
    private static final String IGREJA_SYSTEM_PROMPT =
            "Você é um assistente virtual da Igreja Batista Vila Maria. Seja sempre respeitoso, acolhedor e prestativo ao conversar com qualquer pessoa.\n" +
                    "\n" +
                    "Informações da igreja:\n" +
                    "- Nome: Igreja Batista Vila Maria\n" +
                    "- Endereço: Rua Moreira de Vasconcelos, 425\n" +
                    "- Horários de culto:\n" +
                    "  - Domingo às 9h e 18h\n" +
                    "  - Quarta-feira às 19h30\n" +
                    "  - Sábado às 18h\n" +
                    "\n" +
                    "Informações do pastor:\n" +
                    "- Nome: Pastor Raphael Xavier\n" +
                    "- Horário de atendimento: Segunda a Sexta, das 9h às 17h\n" +
                    "\n" +
                    "Você deve ajudar com:\n" +
                    "1. Responder perguntas sobre a igreja\n" +
                    "2. Orientar sobre como fazer pedidos de oração (esses pedidos devem ser feitos pela funcionalidade específica no chat)\n" +
                    "3. Fornecer informações sobre os horários de culto\n" +
                    "4. Orientar sobre como atualizar o endereço (a atualização deve ser feita pela opção específica do chat)\n" +
                    "5. Informar como entrar em contato com o pastor Raphael Xavier\n" +
                    "\n" +
                    "Informações importantes:\n" +
                    "- As doações aceitas são somente de alimentos não perecíveis, para montagem de cestas básicas.\n" +
                    "- As doações devem ser entregues diretamente na portaria da igreja.\n" +
                    "- Para mais dúvidas, entre em contato com o Pastor Raphael Xavier via WhatsApp.\n" +
                    "- Se for alguma dúvida relacionada à igreja PIBVM que você não saiba responder, oriente a pessoa a falar diretamente com o pastor.\n" +
                    "- Não invente informações. Se não souber, oriente a procurar a secretaria da igreja.\n" +
                    "- Não saia do papel de assistente virtual da igreja.";

    private final RestTemplate restTemplate;
    private final String mistralApiKey;
    private final ObjectMapper objectMapper;
    private final ExecutorService executorService;

    @Autowired
    public MistralService(RestTemplate restTemplate, @Value("${mistral.api.key}") String mistralApiKey) {
        this.restTemplate = restTemplate;
        this.mistralApiKey = mistralApiKey;
        this.objectMapper = new ObjectMapper();
        this.executorService = Executors.newCachedThreadPool();
    }

    public String generateChatResponse(List<ChatMessage> messages) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(mistralApiKey);

        // Converter mensagens para o formato do Mistral
        List<Map<String, String>> mistralMessages = new ArrayList<>();

        // Adicionar mensagem do sistema
        mistralMessages.add(Map.of(
                "role", "system",
                "content", IGREJA_SYSTEM_PROMPT
        ));

        // Adicionar mensagens do usuário e assistente
        for (ChatMessage message : messages) {
            mistralMessages.add(Map.of(
                    "role", message.getRole(),
                    "content", message.getContent()
            ));
        }

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "mistral-small-latest");
        requestBody.put("messages", mistralMessages);
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 2000);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                MISTRAL_API_URL,
                requestEntity,
                String.class
        );

        try {
            JsonNode rootNode = objectMapper.readTree(responseEntity.getBody());
            return rootNode.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar resposta da Mistral", e);
        }
    }

    public SseEmitter streamChatResponse(List<ChatMessage> messages) {
        SseEmitter emitter = new SseEmitter(180000L); // 3 minutos de timeout

        executorService.execute(() -> {
            try {
                URL url = new URL(MISTRAL_API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + mistralApiKey);
                connection.setDoOutput(true);
                connection.setConnectTimeout(30000);
                connection.setReadTimeout(180000);

                // Converter mensagens para o formato do Mistral
                List<Map<String, String>> mistralMessages = new ArrayList<>();

                // Adicionar mensagem do sistema
                mistralMessages.add(Map.of(
                        "role", "system",
                        "content", IGREJA_SYSTEM_PROMPT
                ));

                // Adicionar mensagens do usuário e assistente
                for (ChatMessage message : messages) {
                    mistralMessages.add(Map.of(
                            "role", message.getRole(),
                            "content", message.getContent()
                    ));
                }

                Map<String, Object> requestBody = new HashMap<>();
                requestBody.put("model", "mistral-small-latest");
                requestBody.put("messages", mistralMessages);
                requestBody.put("temperature", 0.7);
                requestBody.put("max_tokens", 2000);
                requestBody.put("stream", true);

                String jsonInputString = objectMapper.writeValueAsString(requestBody);
                connection.getOutputStream().write(jsonInputString.getBytes());

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (line.startsWith("data: ") && !line.contains("[DONE]")) {
                                String jsonData = line.substring(6);
                                JsonNode rootNode = objectMapper.readTree(jsonData);

                                if (rootNode.has("choices") && rootNode.path("choices").size() > 0) {
                                    JsonNode choice = rootNode.path("choices").get(0);
                                    if (choice.has("delta") && choice.path("delta").has("content")) {
                                        String content = choice.path("delta").path("content").asText();
                                        emitter.send(SseEmitter.event().data(content));
                                    }
                                }
                            }
                        }
                        emitter.complete();
                    }
                } else {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        emitter.completeWithError(new RuntimeException("Erro na API da Mistral: " + response));
                    }
                }
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }
}