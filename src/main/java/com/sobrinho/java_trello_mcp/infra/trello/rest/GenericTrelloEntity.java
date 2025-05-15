package com.sobrinho.java_trello_mcp.infra.trello.rest;

import org.springframework.http.HttpStatusCode;

public record GenericTrelloEntity(
    Integer status,
    String message) {
    public static GenericTrelloEntity statusFrom(HttpStatusCode statusCode) {
      return new GenericTrelloEntity(statusCode.value(), "Resource deleted successfully");
    }
}