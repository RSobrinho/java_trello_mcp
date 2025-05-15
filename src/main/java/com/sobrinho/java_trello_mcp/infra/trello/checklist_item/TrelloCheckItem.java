package com.sobrinho.java_trello_mcp.infra.trello.checklist_item;

import com.sobrinho.java_trello_mcp.infra.trello.checklist.TrelloChecklistId;
import com.sobrinho.java_trello_mcp.infra.trello.rest.TrelloCheckItemResponse;

public record TrelloCheckItem(
    TrelloCheckItemId id,
    TrelloCheckItemName name,
    TrelloChecklistId checklistId,
    String state) {
  public static TrelloCheckItem newFrom(TrelloCheckItemResponse response) {
    return new TrelloCheckItem(
        new TrelloCheckItemId(response.id()),
        new TrelloCheckItemName(response.name()),
        new TrelloChecklistId(response.idChecklist()),
        response.state());
  }
}
