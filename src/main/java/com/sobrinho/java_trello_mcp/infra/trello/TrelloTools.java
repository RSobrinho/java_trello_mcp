package com.sobrinho.java_trello_mcp.infra.trello;

import com.sobrinho.java_trello_mcp.infra.config.TrelloConfig;
import com.sobrinho.java_trello_mcp.infra.trello.card.TrelloCard;
import com.sobrinho.java_trello_mcp.infra.trello.card.TrelloCards;
import com.sobrinho.java_trello_mcp.infra.trello.checklist.TrelloChecklist;
import com.sobrinho.java_trello_mcp.infra.trello.checklist.TrelloChecklists;
import com.sobrinho.java_trello_mcp.infra.trello.checklist_item.TrelloCheckItem;
import com.sobrinho.java_trello_mcp.infra.trello.list.TrelloList;
import com.sobrinho.java_trello_mcp.infra.trello.list.TrelloLists;
import com.sobrinho.java_trello_mcp.infra.trello.rest.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TrelloTools {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrelloTools.class);
    private final TrelloClient trelloClient;
    private final TrelloConfig trelloConfig;

    @Autowired
    public TrelloTools(TrelloClient trelloClient, TrelloConfig trelloConfig) {
        this.trelloClient = trelloClient;
        this.trelloConfig = trelloConfig;
    }

    @Tool(name = "create_card", description = "Cria um novo cartão no Trello em uma lista específica. Você pode" +
            " especificar o nome do cartão e a lista de destino.")
    public TrelloCard createCard(String cardName, String listId) {
        LOGGER.info("Criando cartão '{}' na lista '{}'", cardName, listId);
        TrelloCardResponse response = trelloClient.createCard(new TrelloCardRequest(cardName), listId);
        return TrelloCard.newFrom(response);
    }

    @Tool(name = "create_list", description = "Cria uma nova lista (coluna) no quadro Trello. " +
            "Uma lista é um container para cartões, como 'A fazer', 'Em progresso', 'Concluído'.")
    public TrelloList createList(String listName) {
        LOGGER.info("Criando lista '{}'", listName);
        TrelloListResponse response = trelloClient.createList(new TrelloListRequest(listName));
        return TrelloList.newFrom(response);
    }

    @Tool(name = "get_lists", description = "Obtém todas as listas (colunas) do quadro Trello.")
    public TrelloLists getAllLists() {
        LOGGER.info("Obtendo todas as listas do quadro '{}'", trelloConfig.boardId());
        TrelloListsResponse response = trelloClient.getAllLists(trelloConfig.boardId());
        return TrelloLists.from(response);
    }

    @Tool(name = "get_cards", description = "Obtém todos os cartões de uma lista específica no Trello. " +
            "Você precisa fornecer o ID da lista de onde deseja obter os cartões.")
    public TrelloCards getAllCards(String listId) {
        LOGGER.info("Obtendo todos os cartões da lista '{}'", listId);
        TrelloCardsResponse response = trelloClient.getAllCards(listId);
        return TrelloCards.from(response);
    }

    @Tool(name = "get_checklists", description = "Obtém todas as checklists de um cartão específico no Trello. " +
            "Uma checklist é uma lista de itens que podem ser marcados como concluídos. " +
            "Você precisa fornecer o ID do cartão de onde deseja obter as checklists.")
    public TrelloChecklists getAllChecklists(String cardId) {
        LOGGER.info("Obtendo todas as checklists do cartão '{}'", cardId);
        TrelloChecklistsResponse response = trelloClient.getAllChecklists(cardId);
        return TrelloChecklists.from(response);
    }

    @Tool(name = "delete_card", description = "Exclui um cartão específico do Trello. " +
            "Você precisa fornecer o ID do cartão que deseja excluir.")
    public GenericTrelloEntity deleteCard(String cardId) {
        LOGGER.info("Excluindo cartão '{}'", cardId);
        ResponseEntity<Void> response = trelloClient.deleteCard(cardId);
        return GenericTrelloEntity.statusFrom(response.getStatusCode());
    }

    @Tool(name = "archive_list", description = "Arquiva uma lista específica do Trello. " +
            "Você precisa fornecer o ID da lista que deseja arquivar. " +
            "Uma lista arquivada ficará oculta no quadro, mas ainda pode ser restaurada posteriormente.")
    public GenericTrelloEntity archiveList(String listId) {
        LOGGER.info("Arquivando lista '{}'", listId);
        ResponseEntity<Void> response = trelloClient.archiveList(listId);
        return GenericTrelloEntity.statusFrom(response.getStatusCode());
    }

    @Tool(name = "create_checklist", description = "Cria uma nova checklist em um cartão específico do Trello. " +
            "Você precisa fornecer o nome da checklist e o ID do cartão onde ela será criada.")
    public TrelloChecklist createChecklist(String checklistName, String cardId) {
        LOGGER.info("Criando checklist '{}' no cartão '{}'", checklistName, cardId);
        TrelloChecklistResponse response = trelloClient.createChecklist(new TrelloChecklistRequest(checklistName),
                cardId);
        return TrelloChecklist.newFrom(response);
    }

    @Tool(name = "delete_checklist", description = "Exclui uma checklist específica do Trello. " +
            "Você precisa fornecer o ID da checklist que deseja excluir.")
    public GenericTrelloEntity deleteChecklist(String checklistId) {
        LOGGER.info("Excluindo checklist '{}'", checklistId);
        ResponseEntity<Void> response = trelloClient.deleteChecklist(checklistId);
        return GenericTrelloEntity.statusFrom(response.getStatusCode());
    }

    @Tool(name = "create_check_item", description = "Cria um novo item em uma checklist específica do Trello. " +
            "Você precisa fornecer o nome do item e o ID da checklist onde ele será criado.")
    public TrelloCheckItem createCheckItem(String checkItemName, String checklistId) {
        LOGGER.info("Criando item '{}' na checklist '{}'", checkItemName, checklistId);
        TrelloCheckItemResponse response = trelloClient.createCheckItem(new TrelloCheckItemRequest(checkItemName),
                checklistId);
        return TrelloCheckItem.newFrom(response);
    }

    @Tool(name = "delete_check_item", description = "Exclui um item específico de uma checklist do Trello. " +
            "Você precisa fornecer o ID da checklist e o ID do item que deseja excluir.")
    public GenericTrelloEntity deleteCheckItem(String checklistId, String checkItemId) {
        LOGGER.info("Excluindo item '{}' da checklist '{}'", checkItemId, checklistId);
        ResponseEntity<Void> response = trelloClient.deleteCheckItem(checklistId, checkItemId);
        return GenericTrelloEntity.statusFrom(response.getStatusCode());
    }
}
