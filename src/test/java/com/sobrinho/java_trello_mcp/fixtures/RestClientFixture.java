package com.sobrinho.java_trello_mcp.fixtures;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RestClientFixture {
    private final RestClient client;
    private final RestClient.RequestBodyUriSpec requestBodyUriSpec;
    private final RestClient.RequestBodySpec requestBodySpec;
    private final RestClient.ResponseSpec responseSpec;
    private final RestClient.RequestHeadersUriSpec requestHeadersUriSpec;
    private final RestClient.RequestHeadersSpec requestHeadersSpec;
    private final RestClient.RequestHeadersUriSpec requestDeleteUriSpec;
    private final RestClient.RequestBodyUriSpec requestPutUriSpec;

    public RestClientFixture() {
        this.client = mock(RestClient.class);
        this.requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
        this.requestBodySpec = mock(RestClient.RequestBodySpec.class);
        this.responseSpec = mock(RestClient.ResponseSpec.class);
        this.requestHeadersUriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        this.requestHeadersSpec = mock(RestClient.RequestHeadersSpec.class);
        this.requestDeleteUriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        this.requestPutUriSpec = mock(RestClient.RequestBodyUriSpec.class);

        when(client.post()).thenReturn(requestBodyUriSpec);
        when(client.get()).thenReturn(requestHeadersUriSpec);
        when(client.delete()).thenReturn(requestDeleteUriSpec);
        when(client.put()).thenReturn(requestPutUriSpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    public <T, R> void whenPostThenReturn(String uri, Optional<R> requestBody,
            Class<T> responseType, T response) {
        if (requestBody.isPresent()) {
            when(requestBodyUriSpec.uri(eq(uri), eq((Object) requestBody.get()))).thenReturn(requestBodySpec);
        } else {
            when(requestBodyUriSpec.uri(eq(uri))).thenReturn(requestBodySpec);
        }
        when(responseSpec.body(responseType)).thenReturn(response);
    }

    public <T> void whenGetThenReturn(String uri, Class<T> responseType, T response) {
        when(requestHeadersUriSpec.uri(eq(uri))).thenReturn(requestHeadersSpec);
        when(responseSpec.body(responseType)).thenReturn(response);
    }

    public <T> void whenGetThenReturnList(String uri, ParameterizedTypeReference<T> typeReference, T response) {
        when(requestHeadersUriSpec.uri(eq(uri))).thenReturn(requestHeadersSpec);
        when(responseSpec.body(eq(typeReference))).thenReturn(response);
    }

    public <T> void whenDeleteThenReturn(String uri, Class<T> responseType, T response) {
        when(requestDeleteUriSpec.uri(eq(uri))).thenReturn(requestHeadersSpec);
        when(responseSpec.body(responseType)).thenReturn(response);
    }

    public void whenDeleteThenReturnVoid(String uri, ResponseEntity<Void> response) {
        when(requestDeleteUriSpec.uri(eq(uri))).thenReturn(requestHeadersSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(response);
    }

    public void whenPutThenReturnVoid(String uri, ResponseEntity<Void> response) {
        when(requestPutUriSpec.uri(eq(uri))).thenReturn(requestBodySpec);
        when(responseSpec.toBodilessEntity()).thenReturn(response);
    }

    public RestClient getClient() {
        return client;
    }
}
