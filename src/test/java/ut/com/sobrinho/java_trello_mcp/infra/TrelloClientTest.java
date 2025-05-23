package ut.com.sobrinho.java_trello_mcp.infra;

import com.sobrinho.java_trello_mcp.fixtures.RestClientFixture;
import com.sobrinho.java_trello_mcp.infra.trello.rest.*;
import com.sobrinho.java_trello_mcp.infra.config.TrelloConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TrelloClientTest {

        private final TrelloConfig trelloConfig = mockConfig();
        private final RestClientFixture restClientFixture = new RestClientFixture();
        private final TrelloClient trelloClient = new TrelloClient(restClientFixture.getClient(), trelloConfig);

        @Test
        void createTrelloCard() { // o problema tá na uri, não no retrieve nem no body
                TrelloCardRequest request = new TrelloCardRequest("123");
                TrelloCardResponse expected = new TrelloCardResponse(
                                "123",
                                "testCard",
                                "listId",
                                Collections.emptyList(),
                                "shortUrl");
                String uri = "/cards?" + defaultQueryParams() + "&" +
                                idListQueryParam("trelloListId") + "&" + name(request.name());
                restClientFixture.whenPostThenReturn(uri, Optional.of(request), TrelloCardResponse.class, expected);

                TrelloCardResponse result = trelloClient.createCard(request, "trelloListId");

                Assertions.assertEquals(expected, result);
        }

        @Test
        void createTrelloList() {
                TrelloListRequest request = new TrelloListRequest("Test List");
                TrelloListResponse expected = new TrelloListResponse("12345", "Test List",
                                null, null, null, null);
                String uri = "/lists?" + defaultQueryParams() + "&" +
                                name(request.name()) + "&" + idBoardQueryParam(trelloConfig.boardId());
                restClientFixture.whenPostThenReturn(uri, Optional.empty(), TrelloListResponse.class, expected);

                TrelloListResponse result = trelloClient.createList(request);

                Assertions.assertEquals(expected, result);
        }

        @Test
        void getAllLists() {
                String boardId = "board123";
                List<TrelloListResponse> listResponses = List.of(
                                new TrelloListResponse("list1", "List 1", null, null, boardId, null),
                                new TrelloListResponse("list2", "List 2", null, null, boardId, null));
                TrelloListsResponse expected = new TrelloListsResponse(listResponses);

                String uri = "/boards/" + boardId + "/lists?" + defaultQueryParams();
                ParameterizedTypeReference<List<TrelloListResponse>> typeRef = new ParameterizedTypeReference<>() {
                };
                restClientFixture.whenGetThenReturnList(uri, typeRef, listResponses);

                TrelloListsResponse result = trelloClient.getAllLists(boardId);

                Assertions.assertEquals(expected.lists().size(), result.lists().size());
                Assertions.assertEquals(expected.lists().get(0).id(), result.lists().get(0).id());
                Assertions.assertEquals(expected.lists().get(1).id(), result.lists().get(1).id());
        }

        @Test
        void getAllCards() {
                String listId = "list123";
                List<TrelloCardResponse> cardResponses = Arrays.asList(
                                new TrelloCardResponse("card1", "Card 1", listId, Collections.emptyList(), "shortUrl1"),
                                new TrelloCardResponse("card2", "Card 2", listId, Collections.emptyList(),
                                                "shortUrl2"));

                String uri = "/lists/" + listId + "/cards?" + defaultQueryParams();
                ParameterizedTypeReference<List<TrelloCardResponse>> typeRef = new ParameterizedTypeReference<>() {
                };
                restClientFixture.whenGetThenReturnList(uri, typeRef, cardResponses);

                TrelloCardsResponse result = trelloClient.getAllCards(listId);

                Assertions.assertEquals(2, result.cards().size());
                Assertions.assertEquals("card1", result.cards().get(0).id());
                Assertions.assertEquals("card2", result.cards().get(1).id());
        }

        @Test
        void getAllChecklists() {
                String cardId = "card123";
                List<TrelloChecklistResponse> checklistResponses = Arrays.asList(
                                new TrelloChecklistResponse("check1", "Checklist 1", "any", Collections.emptyList()),
                                new TrelloChecklistResponse("check2", "Checklist 2", "any", Collections.emptyList()));

                String uri = "/cards/" + cardId + "/checklists?" + defaultQueryParams();
                ParameterizedTypeReference<List<TrelloChecklistResponse>> typeRef = new ParameterizedTypeReference<>() {
                };
                restClientFixture.whenGetThenReturnList(uri, typeRef, checklistResponses);

                TrelloChecklistsResponse result = trelloClient.getAllChecklists(cardId);

                Assertions.assertEquals(2, result.checklists().size());
                Assertions.assertEquals("check1", result.checklists().get(0).id());
                Assertions.assertEquals("check2", result.checklists().get(1).id());
        }

        @Test
        void deleteCard() {
                String cardId = "card123";
                ResponseEntity<Void> expected = ResponseEntity.ok().build();

                String uri = "/cards/" + cardId + "?" + defaultQueryParams();
                restClientFixture.whenDeleteThenReturnVoid(uri, expected);

                ResponseEntity<Void> result = trelloClient.deleteCard(cardId);

                Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
        }

        @Test
        void archiveList() {
                String listId = "list123";
                ResponseEntity<Void> expected = ResponseEntity.ok().build();

                String uri = "/lists/" + listId + "/closed?" + defaultQueryParams() + "&" + valueToArchiveList();
                restClientFixture.whenPutThenReturnVoid(uri, expected);

                ResponseEntity<Void> result = trelloClient.archiveList(listId);

                Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
        }

        @Test
        void createChecklist() {
                String cardId = "card123";
                TrelloChecklistRequest request = new TrelloChecklistRequest("Test Checklist");
                TrelloChecklistResponse expected = new TrelloChecklistResponse(
                                "checklist1", "Test Checklist", cardId, Collections.emptyList());

                String uri = "/checklists?" + defaultQueryParams() + "&" +
                                idCardQueryParam(cardId) + "&" +
                                name(request.name());

                restClientFixture.whenPostThenReturn(uri, Optional.of(request), TrelloChecklistResponse.class,
                                expected);

                TrelloChecklistResponse result = trelloClient.createChecklist(request, cardId);

                Assertions.assertEquals(expected.id(), result.id());
                Assertions.assertEquals(expected.name(), result.name());
                Assertions.assertEquals(expected.idCard(), result.idCard());
        }

        @Test
        void deleteChecklist() {
                String checklistId = "checklist123";
                ResponseEntity<Void> expected = ResponseEntity.ok().build();

                String uri = "/checklists/" + checklistId + "?" + defaultQueryParams();
                restClientFixture.whenDeleteThenReturnVoid(uri, expected);

                ResponseEntity<Void> result = trelloClient.deleteChecklist(checklistId);

                Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
        }

        @Test
        void createCheckItem() {
                String checklistId = "checklist123";
                TrelloCheckItemRequest request = new TrelloCheckItemRequest("Test Item");
                TrelloCheckItemResponse expected = new TrelloCheckItemResponse(
                                "item1", "Test Item", checklistId, "incomplete");

                String uri = "/checklists/" + checklistId + "/checkItems?" +
                                defaultQueryParams() + "&" +
                                name(request.name());

                restClientFixture.whenPostThenReturn(uri, Optional.of(request), TrelloCheckItemResponse.class,
                                expected);

                TrelloCheckItemResponse result = trelloClient.createCheckItem(request, checklistId);

                Assertions.assertEquals(expected.id(), result.id());
                Assertions.assertEquals(expected.name(), result.name());
                Assertions.assertEquals(expected.idChecklist(), result.idChecklist());
                Assertions.assertEquals(expected.state(), result.state());
        }

        @Test
        void deleteCheckItem() {
                String checklistId = "checklist123";
                String checkItemId = "item123";
                ResponseEntity<Void> expected = ResponseEntity.ok().build();

                String uri = "/checklists/" + checklistId + "/checkItems/" + checkItemId + "?" + defaultQueryParams();
                restClientFixture.whenDeleteThenReturnVoid(uri, expected);

                ResponseEntity<Void> result = trelloClient.deleteCheckItem(checklistId, checkItemId);

                Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
        }

        private TrelloConfig mockConfig() {
                TrelloConfig config = mock(TrelloConfig.class);
                when(config.token()).thenReturn("token");
                when(config.apiKey()).thenReturn("apiKey");
                when(config.baseUrl()).thenReturn("baseUrl");
                when(config.boardId()).thenReturn("boardId");
                when(config.organizationId()).thenReturn("organizationId");
                return config;
        }

        private String defaultQueryParams() {
                return String.format("key=%s&token=%s", trelloConfig.apiKey(), trelloConfig.token());
        }

        private String idListQueryParam(String trelloListId) {
                return String.format("idList=%s", trelloListId);
        }

        private String name(String name) {
                return String.format("name=%s", name);
        }

        private String idBoardQueryParam(String idBoard) {
                return String.format("idBoard=%s", idBoard);
        }

        private String idCardQueryParam(String cardId) {
                return String.format("idCard=%s", cardId);
        }

        private String valueToArchiveList() {
                return String.format("value=%s", true);
        }
}
