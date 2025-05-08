package com.sobrinho.java_trello_mcp.infra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class TrelloTools {

    private static final Logger LOG = LoggerFactory.getLogger(TrelloTools.class);
    private final TrelloClient trelloClient;


    public TrelloTools(TrelloClient trelloClient) {
        this.trelloClient = trelloClient;
    }

    @Tool(name = "java_trello_mcp_create_card", description = "Create a new card on trello")
    public String createCard(String cardName) {
        trelloClient.createCard(new TrelloCardRequest(cardName), "abcde");
        return "Card created";
    }


}
