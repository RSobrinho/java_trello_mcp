package com.sobrinho.java_trello_mcp.infra.trello.rest;

import java.util.List;

public record TrelloCardResponse(
    String id,
    String name,
    String idList,
    List<String> idChecklists,
    String shortUrl) {
}