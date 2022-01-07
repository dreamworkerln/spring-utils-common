package ru.dreamworkerln.spring.utils.common.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.Collections;

@Slf4j
public class RestClient {

    RestTemplate restTemplate;
    HttpHeaders headers;

    RestClient() {}

    // --------------------------------------------------------------------------------------

    public ResponseEntity<String> get(String url) {
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    public ResponseEntity<String> get(String url, HttpHeaders headers) {
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    public ResponseEntity<String> post(String url, String body) {
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

    public ResponseEntity<String> post(String url, String body, HttpHeaders headers) {
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }





    public ResponseEntity<String> put(String url, String body) {
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
    }

    public ResponseEntity<String> put(String url, String body, HttpHeaders headers) {
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
    }






    public ResponseEntity<byte[]> download(String url) {
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);  // ResponseEntity<byte[]> response
    }

    /**
     * Download file to specified path
     * @param url url
     * @param path path
     * @return null if not found (in case of disabled exception throwing)
     */
    public File downloadFile(String url, Path path) {
        RequestCallback requestCallback = request -> {
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
            request.getHeaders().addAll(headers);
        };

        return restTemplate.execute(url, HttpMethod.GET, requestCallback,
            clientHttpResponse -> {
                File file = null;
                if(clientHttpResponse.getStatusCode() == HttpStatus.OK) {
                    file = path.toFile();
                    StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(file));
                }
                return file;
            });
    }
}


//    public RestClient(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }

//    public RestClient(RestTemplate restTemplate, String username, String password) {
//        this.restTemplate = restTemplate;
//        this.username = username;
//        this.password = password;
//    }

// -------------------------------------------------------------------------------------

//    private void addUserAgent(HttpHeaders headers) {
//        //headers.add("user-agent", "Mozilla-4.3");
//    }
//
//    private void addBasicAuth(HttpHeaders headers) {
//
//        if(isBlank(username) || isBlank(password)) {
//            return;
//        }
//
//        String auth = username + ":" + password;
//        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.ISO_8859_1));
//        String authHeader = "Basic " + new String(encodedAuth);
//        headers.add(HttpHeaders.AUTHORIZATION, authHeader);
//    }


// -------------------------------------------------------------------------------------

//    private HttpHeaders getHeaders() {
//        HttpHeaders result = new HttpHeaders();
//        // USER AGENT
//        addUserAgent(result);
//        // BASIC AUTH
//        addBasicAuth(result);
//        return result;
//    }

