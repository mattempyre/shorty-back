package com.mattfogz.shortyback.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

/**
 * Represents a URL entity to be stored in a Redis database.
 * The entity contains both a long URL and its corresponding short URL.
 */
@RedisHash("Url")  // This annotation indicates this object can be saved in Redis and is associated with the "Url" hash.
public class Url {
    
    @Indexed  // This annotation ensures the longUrl can be efficiently queried in Redis.
    private String longUrl;
    
    @Id  // This annotation denotes the primary key for Redis entries. Each URL will have a unique short URL.
    private String shortUrl;

    // Default no-args constructor. Required by Spring Data.
    public Url() {}

    /**
     * Parameterized constructor to create a Url object.
     * 
     * @param longUrl The original long URL.
     * @param shortUrl The shortened URL representation.
     */
    public Url(String longUrl, String shortUrl) {
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
    }

    // Getter for longUrl
    public String getLongUrl() {
        return longUrl;
    }

    // Setter for longUrl
    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    // Getter for shortUrl
    public String getShortUrl() {
        return shortUrl;
    }

    // Setter for shortUrl
    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    // Optionally, you can override `hashCode()`, `equals()`, and `toString()` if needed, 
    // especially if you plan on storing Url objects in collections, comparing them, or need custom string representations.
}