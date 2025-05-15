package com.sobrinho.java_trello_mcp.infra.trello.rest;

import com.sobrinho.java_trello_mcp.infra.config.TrelloConfig;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.util.List;

public class TrelloClient {

    public static final String CHECKLIST_PATH = "/checklists/";
    private final RestClient client;
    private final TrelloConfig config;

    public TrelloClient(RestClient client, TrelloConfig config) {
        this.client = client;
        this.config = config;
    }

    public TrelloCardResponse createCard(TrelloCardRequest cardRequest, String trelloListId) {
        return client
                .post()
                .uri("/cards?" +
                        defaultQueryParams() + "&" +
                        idListQueryParam(trelloListId) + "&" +
                        name(cardRequest.name()), cardRequest)
                .retrieve()
                .body(TrelloCardResponse.class);
    }

    public TrelloListResponse createList(TrelloListRequest listRequest) {
        return client
                .post()
                .uri("/lists?" +
                        defaultQueryParams() + "&" +
                        name(listRequest.name()) + "&" +
                        idBoardQueryParam(config.boardId()))
                .retrieve()
                .body(TrelloListResponse.class);
    }

    public TrelloListsResponse getAllLists(String boardId) {
        List<TrelloListResponse> list = client
                .get()
                .uri("/boards/" + boardId + "/lists?" + defaultQueryParams())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
        return new TrelloListsResponse(list);
    }

    public TrelloCardsResponse getAllCards(String listId) {
        List<TrelloCardResponse> cards = client
                .get()
                .uri("/lists/" + listId + "/cards?" + defaultQueryParams())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
        return new TrelloCardsResponse(cards);
    }

    public TrelloChecklistsResponse getAllChecklists(String cardId) {
        List<TrelloChecklistResponse> checklists = client
                .get()
                .uri("/cards/" + cardId + "/checklists?" + defaultQueryParams())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
        return new TrelloChecklistsResponse(checklists);
    }

    public ResponseEntity<Void> deleteCard(String cardId) {
        return client
                .delete()
                .uri("/cards/" + cardId + "?" + defaultQueryParams())
                .retrieve().toBodilessEntity();
    }

    public ResponseEntity<Void> archiveList(String listId) {
        return client
                .put()
                .uri("/lists/" + listId + "/closed?" + defaultQueryParams() + "&" + archiveList())
                .retrieve().toBodilessEntity();
    }

    public TrelloChecklistResponse createChecklist(TrelloChecklistRequest checklistRequest, String cardId) {
        return client
                .post()
                .uri("/checklists?" +
                        defaultQueryParams() + "&" +
                        idCardQueryParam(cardId) + "&" +
                        name(checklistRequest.name()))
                .retrieve()
                .body(TrelloChecklistResponse.class);
    }

    public ResponseEntity<Void> deleteChecklist(String checklistId) {
        return client
                .delete()
                .uri(CHECKLIST_PATH + checklistId + "?" + defaultQueryParams())
                .retrieve().toBodilessEntity();
    }

    public TrelloCheckItemResponse createCheckItem(TrelloCheckItemRequest checkItemRequest, String checklistId) {
        return client
                .post()
                .uri(CHECKLIST_PATH + checklistId + "/checkItems?" +
                        defaultQueryParams() + "&" +
                        name(checkItemRequest.name()))
                .retrieve()
                .body(TrelloCheckItemResponse.class);
    }

    public ResponseEntity<Void> deleteCheckItem(String checklistId, String checkItemId) {
        return client
                .delete()
                .uri(CHECKLIST_PATH + checklistId + "/checkItems/" + checkItemId + "?" + defaultQueryParams())
                .retrieve().toBodilessEntity();
    }

    private String defaultQueryParams() {
        return String.format("key=%s&token=%s", config.apiKey(), config.token());
    }

    private String idListQueryParam(String trelloListId) {
        return String.format("idList=%s", trelloListId);
    }

    private String idCardQueryParam(String cardId) {
        return String.format("idCard=%s", cardId);
    }

    private String name(String name) {
        return String.format("name=%s", name);
    }

    private String idBoardQueryParam(String idBoard) {
        return String.format("idBoard=%s", idBoard);
    }

    private String archiveList() {
        return String.format("value=%s", true);
    }
}
