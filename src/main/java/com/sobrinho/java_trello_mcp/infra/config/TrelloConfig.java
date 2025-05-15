package com.sobrinho.java_trello_mcp.infra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TrelloConfig {

    @Value("${trello_config.base_url}")
    private String baseUrl;

    @Value("${trello_config.api_key}")
    private String apiKey;

    @Value("${trello_config.token}")
    private String token;

    @Value("${trello_config.organization_id}")
    private String organizationId;

    @Value("${trello_config.board_id}")
    private String boardId;

    public String baseUrl() {
        return baseUrl;
    }

    public String apiKey() {
        return apiKey;
    }

    public String token() {
        return token;
    }

    public String organizationId() {
        return organizationId;
    }

    public String boardId() {
        return boardId;
    }
}
