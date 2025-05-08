package ut.com.sobrinho.java_trello_mcp.infra;


import com.sobrinho.java_trello_mcp.fixtures.RestClientFixture;
import com.sobrinho.java_trello_mcp.infra.TrelloCard;
import com.sobrinho.java_trello_mcp.infra.TrelloCardRequest;
import com.sobrinho.java_trello_mcp.infra.TrelloClient;
import com.sobrinho.java_trello_mcp.infra.config.TrelloConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TrelloClientTest {

    private final TrelloConfig trelloConfig = mockConfig();
    private final RestClientFixture restClientFixture = new RestClientFixture();
    private final TrelloClient trelloClient = new TrelloClient(restClientFixture.getClient(), trelloConfig);

    @Test
    public void createTrelloCard() {
        TrelloCardRequest request = new TrelloCardRequest("123");
        TrelloCard expected = new TrelloCard("123", "testCard");
        String uri = "/cards?" + defaultQueryParams() + "&" + idListQueryParam("trelloListId") + "&" + cardName(request.name());

        restClientFixture.whenPostWithBodyThenReturn(uri, request, TrelloCard.class, expected);

        TrelloCard result = trelloClient.createCard(request, "trelloListId");

        Assertions.assertEquals(expected, result);
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

    private String cardName(String name) {
        return String.format("name=%s", name);
    }
}
