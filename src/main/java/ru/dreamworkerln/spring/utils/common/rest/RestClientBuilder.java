package ru.dreamworkerln.spring.utils.common.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RestClientBuilder {

//    public static RestClientBuilder getBuilder() {
//        return new RestClientBuilder();
//    }

    private RestTemplate restTemplate;
    private HttpHeaders headers;

    public RestClientBuilder() {
        headers = new HttpHeaders();
    }

    public RestClientBuilder restTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        return this;
    }

    public RestClientBuilder basicAuth(String username, String password) {

        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.ISO_8859_1));
        String authHeader = "Basic " + new String(encodedAuth);
        headers.add(HttpHeaders.AUTHORIZATION, authHeader);
        return this;
    }

    public RestClientBuilder header(String key, String value) {
        headers.add(key, value);
        return this;
    }

    public RestClientBuilder userAgent(String value) {
        headers.add("user-agent", value);
        return this;
    }

    public RestClientBuilder acceptEncoding(String value) {
        headers.add("Accept-Encoding", value);
        return this;
    }

    public RestClient build() {
        RestClient result = new RestClient();
        result.restTemplate = restTemplate;
        result.headers = headers;
        return result;
    }
}
