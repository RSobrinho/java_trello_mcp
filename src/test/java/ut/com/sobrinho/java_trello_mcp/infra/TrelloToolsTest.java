package ut.com.sobrinho.java_trello_mcp.infra;

import com.sobrinho.java_trello_mcp.infra.config.TrelloConfig;
import com.sobrinho.java_trello_mcp.infra.trello.card.TrelloCard;
import com.sobrinho.java_trello_mcp.infra.trello.card.TrelloCards;
import com.sobrinho.java_trello_mcp.infra.trello.checklist.TrelloChecklist;
import com.sobrinho.java_trello_mcp.infra.trello.checklist.TrelloChecklists;
import com.sobrinho.java_trello_mcp.infra.trello.checklist_item.TrelloCheckItem;
import com.sobrinho.java_trello_mcp.infra.trello.list.TrelloList;
import com.sobrinho.java_trello_mcp.infra.trello.list.TrelloLists;
import com.sobrinho.java_trello_mcp.infra.trello.rest.*;
import com.sobrinho.java_trello_mcp.infra.trello.TrelloTools;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TrelloToolsTest {

    private TrelloClient trelloClient;
    private TrelloConfig trelloConfig;
    private TrelloTools trelloTools;

    @BeforeEach
    void setUp() {
        trelloClient = mock(TrelloClient.class);
        trelloConfig = mock(TrelloConfig.class);
        when(trelloConfig.boardId()).thenReturn("default-board-id");
        trelloTools = new TrelloTools(trelloClient, trelloConfig);
    }

    @Test
    void createCardShouldUseProvidedListId() {
        String cardName = "Test Card";
        String listId = "list-123";
        TrelloCardResponse mockResponse = new TrelloCardResponse("card-123", cardName, listId, Collections.emptyList(),
                "shortUrl");

        when(trelloClient.createCard(any(TrelloCardRequest.class), eq(listId))).thenReturn(mockResponse);

        TrelloCard result = trelloTools.createCard(cardName, listId);

        ArgumentCaptor<TrelloCardRequest> requestCaptor = ArgumentCaptor.forClass(TrelloCardRequest.class);
        verify(trelloClient).createCard(requestCaptor.capture(), eq(listId));
        assertEquals(cardName, requestCaptor.getValue().name());
        assertEquals(mockResponse.id(), result.id().value());
        assertEquals(mockResponse.name(), result.name().value());
    }

    @Test
    void createListShouldReturnSuccessMessage() {
        TrelloListResponse response = new TrelloListResponse("12345", "any", null, null, null, null);
        TrelloList expected = TrelloList.newFrom(response);
        when(trelloClient.createList(any(TrelloListRequest.class))).thenReturn(response);

        TrelloList result = trelloTools.createList("any");

        assertThat(result, is(expected));
    }

    @Test
    void getAllListsShouldUseProvidedBoardId() {
        TrelloListResponse listResponse = new TrelloListResponse("12345", "any", null, null, null, null);
        TrelloListsResponse response = new TrelloListsResponse(List.of(listResponse));
        TrelloLists expectedLists = new TrelloLists(List.of(TrelloList.newFrom(listResponse)));
        when(trelloClient.getAllLists(anyString())).thenReturn(response);

        TrelloLists result = trelloTools.getAllLists();

        assertEquals(expectedLists, result);
    }

    @Test
    void getAllCardsShouldReturnCardsFromSpecifiedList() {
        String listId = "list-123";
        TrelloCardResponse cardResponse = new TrelloCardResponse("card-123", "Test Card", listId,
                Collections.emptyList(), "shortUrl");
        TrelloCardsResponse response = new TrelloCardsResponse(List.of(cardResponse));
        TrelloCards expectedCards = TrelloCards.from(response);

        when(trelloClient.getAllCards(listId)).thenReturn(response);

        TrelloCards result = trelloTools.getAllCards(listId);

        verify(trelloClient).getAllCards(listId);
        assertEquals(expectedCards, result);
    }

    @Test
    void getAllChecklistsShouldReturnChecklistsFromSpecifiedCard() {
        String cardId = "card-123";
        TrelloChecklistResponse checklistResponse = new TrelloChecklistResponse("checklist-123", "Test Checklist",
                "12345", Collections.emptyList());
        TrelloChecklistsResponse response = new TrelloChecklistsResponse(List.of(checklistResponse));
        TrelloChecklists expectedChecklists = TrelloChecklists.from(response);

        when(trelloClient.getAllChecklists(cardId)).thenReturn(response);

        TrelloChecklists result = trelloTools.getAllChecklists(cardId);

        verify(trelloClient).getAllChecklists(cardId);
        assertEquals(expectedChecklists, result);
    }

    @Test
    void deleteCardShouldReturnSuccessEntity() {
        String cardId = "card-123";
        ResponseEntity<Void> response = ResponseEntity.ok().build();
        when(trelloClient.deleteCard(cardId)).thenReturn(response);

        GenericTrelloEntity result = trelloTools.deleteCard(cardId);

        verify(trelloClient).deleteCard(cardId);
        assertEquals(HttpStatus.OK.value(), result.status());
        assertEquals("Resource deleted successfully", result.message());
    }

    @Test
    void archiveListShouldReturnSuccessEntity() {
        String listId = "list-123";
        ResponseEntity<Void> response = ResponseEntity.ok().build();
        when(trelloClient.archiveList(listId)).thenReturn(response);

        GenericTrelloEntity result = trelloTools.archiveList(listId);

        verify(trelloClient).archiveList(listId);
        assertEquals(HttpStatus.OK.value(), result.status());
        assertEquals("Resource deleted successfully", result.message());
    }

    @Test
    void createChecklistShouldUseProvidedCardId() {
        String checklistName = "Test Checklist";
        String cardId = "card-123";
        TrelloChecklistResponse mockResponse = new TrelloChecklistResponse(
                "checklist-123", checklistName, cardId, Collections.emptyList());

        when(trelloClient.createChecklist(any(TrelloChecklistRequest.class), eq(cardId))).thenReturn(mockResponse);

        TrelloChecklist result = trelloTools.createChecklist(checklistName, cardId);

        ArgumentCaptor<TrelloChecklistRequest> requestCaptor = ArgumentCaptor.forClass(TrelloChecklistRequest.class);
        verify(trelloClient).createChecklist(requestCaptor.capture(), eq(cardId));
        assertEquals(checklistName, requestCaptor.getValue().name());
        assertEquals(mockResponse.id(), result.id().value());
        assertEquals(mockResponse.name(), result.name().value());
        assertEquals(mockResponse.idCard(), result.cardId().value());
    }

    @Test
    void deleteChecklistShouldReturnSuccessEntity() {
        String checklistId = "checklist-123";
        ResponseEntity<Void> response = ResponseEntity.ok().build();
        when(trelloClient.deleteChecklist(checklistId)).thenReturn(response);

        GenericTrelloEntity result = trelloTools.deleteChecklist(checklistId);

        verify(trelloClient).deleteChecklist(checklistId);
        assertEquals(HttpStatus.OK.value(), result.status());
        assertEquals("Resource deleted successfully", result.message());
    }

    @Test
    void createCheckItemShouldUseProvidedChecklistId() {
        String checkItemName = "Test Item";
        String checklistId = "checklist-123";
        TrelloCheckItemResponse mockResponse = new TrelloCheckItemResponse(
                "item-123", checkItemName, checklistId, "incomplete");

        when(trelloClient.createCheckItem(any(TrelloCheckItemRequest.class), eq(checklistId))).thenReturn(mockResponse);

        TrelloCheckItem result = trelloTools.createCheckItem(checkItemName, checklistId);

        ArgumentCaptor<TrelloCheckItemRequest> requestCaptor = ArgumentCaptor.forClass(TrelloCheckItemRequest.class);
        verify(trelloClient).createCheckItem(requestCaptor.capture(), eq(checklistId));
        assertEquals(checkItemName, requestCaptor.getValue().name());
        assertEquals(mockResponse.id(), result.id().value());
        assertEquals(mockResponse.name(), result.name().value());
        assertEquals(mockResponse.idChecklist(), result.checklistId().value());
        assertEquals(mockResponse.state(), result.state());
    }

    @Test
    void deleteCheckItemShouldReturnSuccessEntity() {
        String checklistId = "checklist-123";
        String checkItemId = "item-123";
        ResponseEntity<Void> response = ResponseEntity.ok().build();
        when(trelloClient.deleteCheckItem(checklistId, checkItemId)).thenReturn(response);

        GenericTrelloEntity result = trelloTools.deleteCheckItem(checklistId, checkItemId);

        verify(trelloClient).deleteCheckItem(checklistId, checkItemId);
        assertEquals(HttpStatus.OK.value(), result.status());
        assertEquals("Resource deleted successfully", result.message());
    }
}
