package com.sobrinho.java_trello_mcp.infra.trello.rest;

import java.util.List;

public record TrelloChecklistsResponse(List<TrelloChecklistResponse> checklists) {
}