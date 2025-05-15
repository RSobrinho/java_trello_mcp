package com.sobrinho.java_trello_mcp.infra.trello.rest;

import java.util.List;

public record TrelloCardsResponse(List<TrelloCardResponse> cards) {
}