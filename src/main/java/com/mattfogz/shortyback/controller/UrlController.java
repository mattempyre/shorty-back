package com.mattfogz.shortyback.controller;

import com.mattfogz.shortyback.service.UrlService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/url")
public class UrlController {

    @Autowired
    private UrlService urlService;

    @PostMapping("/create")
    public String createShortUrl(@RequestBody UrlRequest request) {
        return urlService.createShortUrl(request.getLongUrl());
    }

    @GetMapping("/get/{shortUrl}")
    public String getLongUrl(@PathVariable String shortUrl) {
        return urlService.getLongUrl(shortUrl);
    }

    @PutMapping("/update")
    public void updateUrl(@RequestBody UrlUpdateRequest request) {
        urlService.updateUrl(request.getShortUrl(), request.getNewLongUrl());
    }

    @DeleteMapping("/delete/{shortUrl}")
    public ResponseEntity<Map<String, String>> deleteUrl(@PathVariable String shortUrl) {
        urlService.deleteShortUrl(shortUrl);

        // Create a response message
        Map<String, String> response = new HashMap<>();
        response.put("message", "URL with short URL: " + shortUrl + " has been deleted.");

        return ResponseEntity.ok(response); // Returns a 200 OK response with the message
    }

    // Inner DTO class for handling the POST request
    public static class UrlRequest {
        private String longUrl;

        public String getLongUrl() {
            return longUrl;
        }

        public void setLongUrl(String longUrl) {
            this.longUrl = longUrl;
        }
    }

    // Inner DTO class for handling the PUT request
    public static class UrlUpdateRequest {
        private String shortUrl;
        private String newLongUrl;

        public String getShortUrl() {
            return shortUrl;
        }

        public void setShortUrl(String shortUrl) {
            this.shortUrl = shortUrl;
        }

        public String getNewLongUrl() {
            return newLongUrl;
        }

        public void setNewLongUrl(String newLongUrl) {
            this.newLongUrl = newLongUrl;
        }
    }
}