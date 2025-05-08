package com.sobrinho.java_trello_mcp.infra;

import com.sobrinho.java_trello_mcp.infra.config.TrelloConfig;
import org.springframework.web.client.RestClient;

public class TrelloClient {

    private final RestClient client;
    private final TrelloConfig config;

    public TrelloClient(RestClient client, TrelloConfig config) {
        this.client = client;
        this.config = config;
    }

    public TrelloCard createCard(TrelloCardRequest cardRequest, String trelloListId) {
        return client
                .post()
                .uri("/cards?" +
                        defaultQueryParams() + "&" +
                        idListQueryParam(trelloListId) + "&" +
                        cardName(cardRequest.name()), cardRequest)
                .retrieve()
                .body(TrelloCard.class);
    }

    private String defaultQueryParams() {
        return String.format("key=%s&token=%s", config.apiKey(), config.token());
    }
    private String idListQueryParam(String trelloListId) {
        return String.format("idList=%s", trelloListId);
    }
    private String cardName(String name) {
        return String.format("name=%s", name);
    }
}
