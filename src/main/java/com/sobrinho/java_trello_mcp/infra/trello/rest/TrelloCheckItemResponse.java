package com.sobrinho.java_trello_mcp.infra.trello.rest;

public record TrelloCheckItemResponse(
    String id,
    String name,
    String idChecklist,
    String state) {
}