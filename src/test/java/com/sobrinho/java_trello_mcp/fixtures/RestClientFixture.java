package com.sobrinho.java_trello_mcp.fixtures;

import org.springframework.web.client.RestClient;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RestClientFixture {
    private final RestClient client;
    private final RestClient.RequestBodyUriSpec requestBodyUriSpec;
    private final RestClient.RequestBodySpec requestBodySpec;
    private final RestClient.ResponseSpec responseSpec;

    public RestClientFixture() {
        this.client = mock(RestClient.class);
        this.requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
        this.requestBodySpec = mock(RestClient.RequestBodySpec.class);
        this.responseSpec = mock(RestClient.ResponseSpec.class);

        when(client.post()).thenReturn(requestBodyUriSpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
    }

    public <T, R> RestClientFixture whenPostWithBodyThenReturn(String uri, R requestBody, Class<T> responseType, T response) {
        when(requestBodyUriSpec.uri(eq(uri), eq((Object)requestBody))).thenReturn(requestBodySpec);
        when(responseSpec.body(responseType)).thenReturn(response);
        return this;
    }


    public <T> RestClientFixture whenPostThenReturn(String uri, Class<T> responseType, T response) {
        when(requestBodyUriSpec.uri(eq(uri), eq((Object)null))).thenReturn(requestBodySpec);
        when(responseSpec.body(responseType)).thenReturn(response);
        return this;
    }

    public RestClient getClient() {
        return client;
    }
}
