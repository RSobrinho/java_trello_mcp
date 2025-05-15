package com.sobrinho.java_trello_mcp.infra.trello.rest;

public record TrelloListResponse(String id, String name, String closed,
                                 Boolean color, String idBoard, Integer pos) {


}
