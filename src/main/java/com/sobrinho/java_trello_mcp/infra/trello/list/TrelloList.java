package com.sobrinho.java_trello_mcp.infra.trello.list;

import com.sobrinho.java_trello_mcp.infra.trello.card.TrelloCardId;
import com.sobrinho.java_trello_mcp.infra.trello.rest.TrelloListResponse;

import java.util.List;

public record TrelloList(TrelloListId id, TrelloListName name, List<TrelloCardId> cardIds) {

    public static TrelloList newFrom(TrelloListResponse response) {
        return new TrelloList(new TrelloListId(response.id()), new TrelloListName(response.name()), List.of());
    }
}