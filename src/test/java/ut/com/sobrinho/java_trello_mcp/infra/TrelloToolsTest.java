package ut.com.sobrinho.java_trello_mcp.infra;

import com.sobrinho.java_trello_mcp.infra.TrelloCardRequest;
import com.sobrinho.java_trello_mcp.infra.TrelloClient;
import com.sobrinho.java_trello_mcp.infra.TrelloTools;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TrelloToolsTest {

    @Test
    public void createCard() {
        trelloTools.createCard("testCard");
        verify(trelloClient).createCard(any(TrelloCardRequest.class), anyString());
    }

    private final TrelloClient trelloClient = mock(TrelloClient.class);
    private final TrelloTools trelloTools = new TrelloTools(trelloClient);
}