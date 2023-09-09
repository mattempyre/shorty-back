package com.mattfogz.shortyback.controller;

import com.mattfogz.shortyback.model.Url;
import com.mattfogz.shortyback.service.UrlService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UrlController {

    @Autowired
    private UrlService urlService;

    @PostMapping("/api/url/create")
    public String createShortUrl(@RequestBody UrlRequest request) {
        return urlService.createShortUrl(request.getLongUrl(), request.getCustomShortUrl());
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Object> getLongUrl(@PathVariable String shortUrl) {
        String longUrl = urlService.getLongUrl(shortUrl);

        Map<String, String> response = new HashMap<>();
        response.put("redirectUrl", longUrl);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/url/update")
    public ResponseEntity<Map<String, String>> updateUrl(@RequestBody UrlUpdateRequest request) {
        urlService.updateUrl(request.getShortUrl(), request.getNewLongUrl());
    
        Map<String, String> response = new HashMap<>();
        response.put("message", "URL updated successfully.");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/url/delete/{shortUrl}")
    public ResponseEntity<Map<String, String>> deleteUrl(@PathVariable String shortUrl) {
        urlService.deleteShortUrl(shortUrl);

        Map<String, String> response = new HashMap<>();
        response.put("message", "URL with short URL: " + shortUrl + " has been deleted.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/url/all")
    public ResponseEntity<List<Url>> getAllUrls() {
        List<Url> urls = urlService.getAllUrls();
        return ResponseEntity.ok(urls);
    }

    // Inner DTO class for handling the POST request
    public static class UrlRequest {
        private String longUrl;
        private String customShortUrl; // The new field

        public String getLongUrl() {
            return longUrl;
        }

        public void setLongUrl(String longUrl) {
            this.longUrl = longUrl;
        }

        public String getCustomShortUrl() {
            return customShortUrl;
        }

        public void setCustomShortUrl(String customShortUrl) {
            this.customShortUrl = customShortUrl;
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