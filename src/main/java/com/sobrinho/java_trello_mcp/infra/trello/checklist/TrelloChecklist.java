package com.sobrinho.java_trello_mcp.infra.trello.checklist;

import com.sobrinho.java_trello_mcp.infra.trello.card.TrelloCardId;
import com.sobrinho.java_trello_mcp.infra.trello.checklist_item.TrelloCheckItemId;
import com.sobrinho.java_trello_mcp.infra.trello.rest.TrelloChecklistResponse;

import java.util.List;

public record TrelloChecklist(TrelloChecklistId id, TrelloChecklistName name,
                              TrelloCardId cardId, List<TrelloCheckItemId> itemIds) {

  public static TrelloChecklist newFrom(TrelloChecklistResponse response) {
    return new TrelloChecklist(
        new TrelloChecklistId(response.id()),
        new TrelloChecklistName(response.name()),
        new TrelloCardId(response.idCard()),
        List.of()
    );
  }
}
