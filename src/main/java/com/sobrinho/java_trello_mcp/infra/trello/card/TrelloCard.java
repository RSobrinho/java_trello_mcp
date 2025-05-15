package com.sobrinho.java_trello_mcp.infra.trello.card;

import com.sobrinho.java_trello_mcp.infra.trello.checklist.TrelloChecklistId;
import com.sobrinho.java_trello_mcp.infra.trello.list.TrelloListId;
import com.sobrinho.java_trello_mcp.infra.trello.rest.TrelloCardResponse;

import java.util.List;
import java.util.stream.Collectors;

public record TrelloCard(TrelloCardId id, TrelloCardName name, TrelloListId listId,
    List<TrelloChecklistId> checklistIds, TrelloCardShortUrl shortUrl) {

  public static TrelloCard newFrom(TrelloCardResponse response) {
    List<TrelloChecklistId> checklistIds = response.idChecklists() != null ? response.idChecklists().stream()
        .map(TrelloChecklistId::new)
        .collect(Collectors.toList()) : List.of();

    return new TrelloCard(
        new TrelloCardId(response.id()),
        new TrelloCardName(response.name()),
        new TrelloListId(response.idList()),
        checklistIds,
        new TrelloCardShortUrl(response.shortUrl()));
  }
}
