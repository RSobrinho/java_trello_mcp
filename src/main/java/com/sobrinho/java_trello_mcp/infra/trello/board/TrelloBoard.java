package com.sobrinho.java_trello_mcp.infra.trello.board;

import com.sobrinho.java_trello_mcp.infra.trello.list.TrelloListId;
import com.sobrinho.java_trello_mcp.infra.trello.organization.TrelloOrganizationId;

import java.util.List;

public record TrelloBoard(TrelloBoardId id, TrelloBoardName name, TrelloOrganizationId organizationId,
                          List<TrelloListId> lists) {
}
