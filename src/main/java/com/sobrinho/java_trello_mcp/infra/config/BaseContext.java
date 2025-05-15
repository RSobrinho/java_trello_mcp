package com.sobrinho.java_trello_mcp.infra.config;

import com.sobrinho.java_trello_mcp.infra.trello.rest.TrelloClient;
import com.sobrinho.java_trello_mcp.infra.trello.TrelloTools;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.util.List;

@Configuration
public class BaseContext {

    @Bean
    public TrelloConfig trelloConfig() {
        return new TrelloConfig();
    }

    @Bean
    public TrelloClient trelloClient() {
        TrelloConfig config = trelloConfig();
        RestClient restClient = RestClient.builder().baseUrl(config.baseUrl()).build();
        return new TrelloClient(restClient, config);
    }

    @Bean
    public TrelloTools trelloTools() {
        return new TrelloTools(trelloClient(), trelloConfig());
    }

    @Bean
    public List<ToolCallback> toolsCallback() {
        return List.of(ToolCallbacks.from(trelloTools()));
    }

}
