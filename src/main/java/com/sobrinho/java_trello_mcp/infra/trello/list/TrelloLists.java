package com.sobrinho.java_trello_mcp.infra.trello.list;

import com.sobrinho.java_trello_mcp.infra.trello.rest.TrelloListsResponse;

import java.util.List;
import java.util.stream.Collectors;

public record TrelloLists(List<TrelloList> lists) {
    public static TrelloLists from(TrelloListsResponse response) {
        return new TrelloLists(response.lists().stream().map(TrelloList::newFrom).collect(Collectors.toList()));
    }
}
