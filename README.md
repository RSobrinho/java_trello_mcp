# Java Trello MCP

A Spring Boot application that provides Trello card creation functionality as a tool for Spring AI Model Composition Platform (MCP).

## Overview

This application allows you to create Trello cards programmatically through a simple Java API. It's designed to be used with Spring AI's Model Composition Platform, enabling AI models to interact with Trello.

The application provides a tool called `java_trello_mcp_create_card` that can be used to create cards on a specific Trello list.

## Features

- Create Trello cards with a simple API
- Integration with Spring AI MCP
- Configurable Trello API credentials
- Unit tests for all components

## Prerequisites

- Java 17 or higher
- Maven
- Trello account with API access

## Installation

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/java_trello_mcp.git
   cd java_trello_mcp
   ```

2. Build the application:
   ```
   ./mvnw clean install
   ```

## Configuration

The application requires Trello API credentials to be configured. You can set these through environment variables:

- `TRELLO_API_KEY` - Your Trello API key
- `TRELLO_TOKEN` - Your Trello API token
- `TRELLO_ORGANIZATION_ID` - Your Trello organization ID
- `TRELLO_BOARD_ID` - Your Trello board ID
- `TRELLO_BASE_URL` - The Trello API base URL (usually `https://api.trello.com/1`)

Alternatively, you can set these values in the `application.yml` file:

```yaml
trello_config:
  api_key: your_api_key
  token: your_token
  organization_id: your_organization_id
  board_id: your_board_id
  base_url: https://api.trello.com/1
```

## Usage

### As a Spring Bean

You can use the `TrelloTools` class directly in your Spring application:

```java
@Autowired
private TrelloTools trelloTools;

public void createTrelloCard() {
    String result = trelloTools.createCard("My new card");
    System.out.println(result); // Outputs: "Card created"
}
```

### As a Spring AI Tool

The application registers the `TrelloTools` class as a Spring AI tool. You can use it in your Spring AI application:

```java
@Autowired
private ToolCallbacks toolCallbacks;

public void useAiTool() {
    // This would be called by the AI system
    ToolCallback trelloTool = toolCallbacks.stream()
        .filter(tc -> tc.getName().equals("java_trello_mcp_create_card"))
        .findFirst()
        .orElseThrow();
    
    String result = (String) trelloTool.call("My AI-created card");
    System.out.println(result); // Outputs: "Card created"
}
```

## Project Structure

- `TrelloCard` - Data model representing a Trello card
- `TrelloCardRequest` - Request object for creating a Trello card
- `TrelloClient` - Client for interacting with the Trello API
- `TrelloTools` - Service that provides the Trello functionality as a tool
- `TrelloConfig` - Configuration for Trello API credentials
- `BaseConfig` - Spring configuration for setting up the beans

## Testing

The application includes unit tests for all components. You can run the tests with:

```
./mvnw test
```

The tests use mocks to avoid making actual API calls to Trello.

## License

[Add your license information here]

## Contributing

[Add contribution guidelines here]

## Acknowledgements

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring AI](https://spring.io/projects/spring-ai)
- [Trello API](https://developer.atlassian.com/cloud/trello/rest/api-group-actions/)