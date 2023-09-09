package com.mattfogz.shortyback.controller;

import com.mattfogz.shortyback.model.Url;
import com.mattfogz.shortyback.service.UrlService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller that provides endpoints for URL shortening operations.
 * It supports creating, retrieving, updating, and deleting short URLs.
 */
@RestController
@RequestMapping("/")
public class UrlController {

    // Autowire the URL Service to delegate business logic operations
    @Autowired
    private UrlService urlService;

    /**
     * REST endpoint for creating a short URL.
     *
     * @param request The request body containing the long URL and an optional
     *                custom short URL.
     * @return A ResponseEntity containing a JSON response with the short URL and a
     *         success message.
     */
    @PostMapping("/api/url/create")
    public ResponseEntity<Map<String, String>> createShortUrl(@RequestBody UrlRequest request) {
        // Call the UrlService to create a short URL based on the provided request
        String shortUrl = urlService.createShortUrl(request.getLongUrl(), request.getCustomShortUrl());

        // Create a response map to structure the JSON response
        Map<String, String> response = new HashMap<>();

        // Add the generated short URL to the response
        response.put("shortUrl", shortUrl);

        // Add a success message to the response (you can provide additional information
        // here)
        response.put("message", "Short URL created successfully.");

        // Return a ResponseEntity with the JSON response and an HTTP status of OK
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to retrieve the original long URL using the short URL.
     *
     * @param shortUrl The short URL to lookup.
     * @return A ResponseEntity containing the original long URL.
     */
    @GetMapping("/{shortUrl}")
    public ResponseEntity<Object> getLongUrl(@PathVariable String shortUrl) {
        String longUrl = urlService.getLongUrl(shortUrl);

        Map<String, String> response = new HashMap<>();
        response.put("redirectUrl", longUrl);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to update an existing short URL to point to a new long URL.
     *
     * @param request DTO containing the short URL and the new long URL.
     * @return A ResponseEntity indicating success.
     */
    @PutMapping("/api/url/update")
    public ResponseEntity<Map<String, String>> updateUrl(@RequestBody UrlUpdateRequest request) {
        urlService.updateUrl(request.getShortUrl(), request.getNewLongUrl());

        Map<String, String> response = new HashMap<>();
        response.put("message", "URL updated successfully.");
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to delete a short URL entry.
     *
     * @param shortUrl The short URL to delete.
     * @return A ResponseEntity indicating success.
     */
    @DeleteMapping("/api/url/delete/{shortUrl}")
    public ResponseEntity<Map<String, String>> deleteUrl(@PathVariable String shortUrl) {
        urlService.deleteShortUrl(shortUrl);

        Map<String, String> response = new HashMap<>();
        response.put("message", "URL with short URL: " + shortUrl + " has been deleted.");
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to retrieve all the stored URLs.
     *
     * @return A ResponseEntity containing a list of all stored URLs.
     */
    @GetMapping("/api/url/all")
    public ResponseEntity<List<Url>> getAllUrls() {
        List<Url> urls = urlService.getAllUrls();
        return ResponseEntity.ok(urls);
    }

    /**
     * Data Transfer Object (DTO) for handling create URL requests.
     * Encapsulates the necessary information for creating a short URL.
     */
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