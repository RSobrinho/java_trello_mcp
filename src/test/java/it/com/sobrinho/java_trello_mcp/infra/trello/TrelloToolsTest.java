package it.com.sobrinho.java_trello_mcp.infra.trello;

import com.sobrinho.java_trello_mcp.JavaTrelloMcpApplication;
import com.sobrinho.java_trello_mcp.infra.config.BaseContext;
import com.sobrinho.java_trello_mcp.infra.config.TrelloConfig;
import com.sobrinho.java_trello_mcp.infra.trello.TrelloTools;
import com.sobrinho.java_trello_mcp.infra.trello.card.TrelloCard;
import com.sobrinho.java_trello_mcp.infra.trello.card.TrelloCards;
import com.sobrinho.java_trello_mcp.infra.trello.checklist.TrelloChecklist;
import com.sobrinho.java_trello_mcp.infra.trello.checklist.TrelloChecklists;
import com.sobrinho.java_trello_mcp.infra.trello.checklist_item.TrelloCheckItem;
import com.sobrinho.java_trello_mcp.infra.trello.list.TrelloList;
import com.sobrinho.java_trello_mcp.infra.trello.list.TrelloLists;
import com.sobrinho.java_trello_mcp.infra.trello.rest.GenericTrelloEntity;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(classes = JavaTrelloMcpApplication.class)
@Import(BaseContext.class)
@ActiveProfiles("integration-test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TrelloToolsTest {

    @Autowired
    private TrelloTools trelloTools;

    @Autowired
    private TrelloConfig trelloConfig;

    private String setupListId;
    private String setupCardId;
    private String setupCardName;
    private String setupChecklistId;

    @BeforeAll
    void prepareData() {
        String listName = "IT Setup List " + UUID.randomUUID().toString().substring(0, 8);
        TrelloList listResponse = trelloTools.createList(listName);
        setupListId = listResponse.id().value();

        setupCardName = "IT Setup Card " + UUID.randomUUID().toString().substring(0, 8);
        TrelloCard card = trelloTools.createCard(setupCardName, setupListId);
        setupCardId = card.id().value();

        String checklistName = "IT Setup Checklist " + UUID.randomUUID().toString().substring(0, 8);
        TrelloChecklist checklist = trelloTools.createChecklist(checklistName, setupCardId);
        setupChecklistId = checklist.id().value();
    }

    @Test
    void testCreateList() {
        String uniqueListName = "IT Test List " + UUID.randomUUID().toString().substring(0, 8);
        TrelloList result = trelloTools.createList(uniqueListName);

        assertThat(result.id(), notNullValue());
        assertThat(result.cardIds().size(), is(0));
        assertThat(result.name().value(), is(uniqueListName));
    }

    @Test
    void testGetAllLists() {
        TrelloLists lists = trelloTools.getAllLists();
        assertThat(!lists.lists().isEmpty(), is(true));
    }

    @Test
    void testCreateCard() {
        String cardName = "IT Test Card " + UUID.randomUUID().toString().substring(0, 8);
        TrelloCard card = trelloTools.createCard(cardName, setupListId);
        assertThat(card.id(), notNullValue());
        assertThat(card.name().value(), is(cardName));
    }

    @Test
    void testGetAllCards() {
        TrelloCards cards = trelloTools.getAllCards(setupListId);

        boolean containsSetupCard = cards.cards().stream()
                .anyMatch(c -> c.name().value().equals(setupCardName));

        assertThat(containsSetupCard, is(true));
    }

    @Test
    void testGetAllChecklists() {
        TrelloChecklists checklists = trelloTools.getAllChecklists(setupCardId);
        assertThat(checklists, notNullValue());
    }

    @Test
    void testDeleteCard() {
        String cardName = "IT Test Card to Delete " + UUID.randomUUID().toString().substring(0, 8);
        TrelloCard cardToDelete = trelloTools.createCard(cardName, setupListId);
        String cardIdToDelete = cardToDelete.id().value();

        GenericTrelloEntity result = trelloTools.deleteCard(cardIdToDelete);

        assertThat(result, notNullValue());
        assertThat(result.status(), is(HttpStatus.OK.value()));
        assertThat(result.message(), is("Resource deleted successfully"));

        TrelloCards cardsAfterDeletion = trelloTools.getAllCards(setupListId);
        boolean cardStillExists = cardsAfterDeletion.cards().stream()
                .anyMatch(c -> c.id().value().equals(cardIdToDelete));

        assertThat("O cartão não deve existir após a deleção", cardStillExists, is(false));
    }

    @Test
    void testArchiveList() {
        String listName = "IT Test List to Archive " + UUID.randomUUID().toString().substring(0, 8);
        TrelloList listToArchive = trelloTools.createList(listName);
        String listIdToArchive = listToArchive.id().value();

        GenericTrelloEntity result = trelloTools.archiveList(listIdToArchive);

        assertThat(result, notNullValue());
        assertThat(result.status(), is(HttpStatus.OK.value()));
        assertThat(result.message(), is("Resource deleted successfully"));

        TrelloLists listsAfterArchiving = trelloTools.getAllLists();
        boolean listStillVisible = listsAfterArchiving.lists().stream()
                .anyMatch(l -> l.id().value().equals(listIdToArchive));

        assertThat("A lista não deve estar visível após o arquivamento", listStillVisible, is(false));
    }

    @Test
    void testCreateChecklist() {
        String checklistName = "IT Test Checklist " + UUID.randomUUID().toString().substring(0, 8);
        TrelloChecklist checklist = trelloTools.createChecklist(checklistName, setupCardId);

        assertThat(checklist.id(), notNullValue());
        assertThat(checklist.name().value(), is(checklistName));
        assertThat(checklist.cardId().value(), is(setupCardId));

        TrelloChecklists checklists = trelloTools.getAllChecklists(setupCardId);
        boolean checklistExists = checklists.checklists().stream()
                .anyMatch(c -> c.id().value().equals(checklist.id().value()));

        assertThat("A checklist deve existir no cartão", checklistExists, is(true));
    }

    @Test
    void testDeleteChecklist() {
        String checklistName = "IT Test Checklist to Delete " + UUID.randomUUID().toString().substring(0, 8);
        TrelloChecklist checklistToDelete = trelloTools.createChecklist(checklistName, setupCardId);
        String checklistIdToDelete = checklistToDelete.id().value();

        GenericTrelloEntity result = trelloTools.deleteChecklist(checklistIdToDelete);

        assertThat(result, notNullValue());
        assertThat(result.status(), is(HttpStatus.OK.value()));
        assertThat(result.message(), is("Resource deleted successfully"));

        TrelloChecklists checklistsAfterDeletion = trelloTools.getAllChecklists(setupCardId);
        boolean checklistStillExists = checklistsAfterDeletion.checklists().stream()
                .anyMatch(c -> c.id().value().equals(checklistIdToDelete));

        assertThat("A checklist não deve existir após a deleção", checklistStillExists, is(false));
    }

    @Test
    void testCreateCheckItem() {
        String checkItemName = "IT Test CheckItem " + UUID.randomUUID().toString().substring(0, 8);
        TrelloCheckItem checkItem = trelloTools.createCheckItem(checkItemName, setupChecklistId);

        assertThat(checkItem.id(), notNullValue());
        assertThat(checkItem.name().value(), is(checkItemName));
        assertThat(checkItem.checklistId().value(), is(setupChecklistId));
    }

    @Test
    void testDeleteCheckItem() {
        String checkItemName = "IT Test CheckItem to Delete " + UUID.randomUUID().toString().substring(0, 8);
        TrelloCheckItem checkItemToDelete = trelloTools.createCheckItem(checkItemName, setupChecklistId);
        String checkItemIdToDelete = checkItemToDelete.id().value();

        GenericTrelloEntity result = trelloTools.deleteCheckItem(setupChecklistId, checkItemIdToDelete);

        assertThat(result, notNullValue());
        assertThat(result.status(), is(HttpStatus.OK.value()));
        assertThat(result.message(), is("Resource deleted successfully"));
    }

    @AfterAll
    void cleanUpTestData() {
        TrelloLists lists = trelloTools.getAllLists();

        List<TrelloList> toRemove = lists.lists().stream()
                .filter(list -> list.name().value().contains("IT Test List") ||
                        list.name().value().contains("IT Setup List"))
                .toList();

        toRemove.forEach(list -> trelloTools.archiveList(list.id().value()));
    }
}