package com.sobrinho.java_trello_mcp.infra.trello.checklist;

import com.sobrinho.java_trello_mcp.infra.trello.rest.TrelloChecklistsResponse;

import java.util.List;
import java.util.stream.Collectors;

public record TrelloChecklists(List<TrelloChecklist> checklists) {

  public static TrelloChecklists from(TrelloChecklistsResponse response) {
    List<TrelloChecklist> checklists = response.checklists().stream()
        .map(TrelloChecklist::newFrom)
        .collect(Collectors.toList());

    return new TrelloChecklists(checklists);
  }
}