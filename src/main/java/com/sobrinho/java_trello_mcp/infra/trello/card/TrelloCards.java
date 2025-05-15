package com.sobrinho.java_trello_mcp.infra.trello.card;

import com.sobrinho.java_trello_mcp.infra.trello.rest.TrelloCardsResponse;

import java.util.List;
import java.util.stream.Collectors;

public record TrelloCards(List<TrelloCard> cards) {

  public static TrelloCards from(TrelloCardsResponse response) {
    List<TrelloCard> cards = response.cards().stream()
        .map(TrelloCard::newFrom)
        .collect(Collectors.toList());

    return new TrelloCards(cards);
  }
}