# Java Trello MCP

A Spring Boot application that provides Trello integration as a Model Composition Platform (MCP) tool for Spring AI.

## Features

- Create and manage Trello cards, lists, and checklists
- Fully integrated with Spring AI's Model Composition Platform
- RESTful API with Swagger UI documentation
- Configurable Trello API credentials

## Prerequisites

- Java 24
- Maven 3.8+
- Trello account with API access

## Configuration

Configure your Trello API credentials using one of these methods:

### Environment Variables
```bash
export TRELLO_API_KEY=your_api_key
export TRELLO_TOKEN=your_token
export TRELLO_ORGANIZATION_ID=your_organization_id
export TRELLO_BOARD_ID=your_board_id
export TRELLO_BASE_URL=https://api.trello.com/1
```

### Application Properties
Edit `src/main/resources/application.yml`:
```yaml
trello_config:
  api_key: your_api_key
  token: your_token
  organization_id: your_organization_id
  board_id: your_board_id
  base_url: https://api.trello.com/1
```

## Running the MCP

1. Build the application:
   ```bash
   ./mvnw clean package
   ```

2. Run the application:
   ```bash
   java -jar target/java_trello_mcp-0.0.1-SNAPSHOT.jar
   ```

   Or with Maven:
   ```bash
   ./mvnw spring-boot:run
   ```

3. The MCP server will start on port 8080

## API Documentation

Access Swagger UI documentation at:
```
http://localhost:8080/swagger-ui.html
```

## Available MCP Tools

The following Trello tools are available for Spring AI:

- `create_card` - Create a new Trello card in a specific list
- `create_list` - Create a new list on a Trello board
- `get_lists` - Get all lists from a Trello board
- `get_cards` - Get all cards from a specific list
- `get_checklists` - Get all checklists from a specific card
- `create_checklist` - Create a new checklist for a card
- `create_check_item` - Create a new item in a checklist
- `delete_card` - Delete a specific card
- `delete_checklist` - Delete a specific checklist
- `delete_check_item` - Delete a specific checklist item
- `archive_list` - Archive a specific list

## MCP Client Configuration

To configure an AI platform (like Claude, Cursor, or others) to use this MCP server, add the following configuration to your MCP client setup:

```json
{
  "mcpServers": {
    "mcp-trello": {
      "command": "java",
      "args": [
        "-jar", "C:\\Users\\your_username\\path\\to\\java_trello_mcp-0.0.1-SNAPSHOT.jar"
      ],
      "env": {
        "TRELLO_API_KEY": "your_api_key",
        "TRELLO_TOKEN": "your_token",
        "TRELLO_ORGANIZATION_ID": "your_org_id",
        "TRELLO_BOARD_ID": "your_board_id",
        "TRELLO_BASE_URL": "https://api.trello.com/1"
      }
    }
  }
}
```

Make sure to:
1. Update the jar path to match your local environment
2. Replace the environment variables with your actual Trello credentials
3. Configure this in your AI platform's MCP settings