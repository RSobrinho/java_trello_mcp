package com.sobrinho.java_trello_mcp.infra.trello.rest;

import java.util.List;

public record TrelloChecklistResponse(String id, String name, String idCard, List<String> checkItems) {
}