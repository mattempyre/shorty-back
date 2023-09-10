package com.mattfogz.shortyback.controller;

import com.mattfogz.shortyback.service.UrlService;
import com.mattfogz.shortyback.exception.UrlException;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller that provides endpoints for URL shortening operations.
 * It supports creating, retrieving, updating, and deleting short URLs.
 */
@RestController
@RequestMapping("/")
public class UrlController {

    @Autowired
    private UrlService urlService;

    /**
     * REST endpoint to create a short URL.
     * 
     * @param request DTO containing the long URL and optional custom short URL.
     * @return JSON response with the short URL and success message.
     */
    @PostMapping("/api/url/create")
    public ResponseEntity<Map<String, String>> createShortUrl(@RequestBody UrlRequest request) {
        try {
            String shortUrl = urlService.createShortUrl(request.getLongUrl(), request.getCustomShortUrl());
            String longUrl = urlService.getLongUrl(shortUrl);
            int clickCount = urlService.getClickCount(shortUrl);

            Map<String, String> response = new HashMap<>();
            response.put("shortUrl", shortUrl);
            response.put("longUrl", longUrl);
            response.put("clickCount", String.valueOf(clickCount));
            response.put("message", "Short URL created successfully.");

            return ResponseEntity.ok(response);
        } catch (UrlException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Endpoint to retrieve the original long URL using the short URL and redirect
     * to it.
     * Also increments the click count for the short URL when accessed.
     * 
     * @param shortUrl The short URL to lookup and redirect to its corresponding
     *                 long URL.
     * @return Redirect response to the original long URL or an error if not found.
     */
    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> getLongUrl(@PathVariable String shortUrl) {
        String longUrl = urlService.getLongUrl(shortUrl);
        if (longUrl != null) {
            // Increment the click count for the accessed short URL
            urlService.incrementClickCount(shortUrl);

            // Issue an HTTP 302 Found response to redirect the client
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(longUrl))
                    .build();
        } else {
            // If the shortUrl isn't found in the system, you can send a 404 or another
            // suitable error response
            return ResponseEntity.notFound().build();
        }
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
    public ResponseEntity<List<Map<String, String>>> getAllUrlsWithClickCount() {
        List<Map<String, String>> urls = urlService.getAllUrlsWithClickCount();
        return ResponseEntity.ok(urls);
    }

    /**
     * Data Transfer Object (DTO) for handling create URL requests.
     * Encapsulates the necessary information for creating a short URL.
     */
    public static class UrlRequest {
        private String longUrl;
        private String customShortUrl;

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

    /**
     * Data Transfer Object (DTO) for handling update URL requests.
     * Encapsulates the necessary information for updating an existing short URL.
     */
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
