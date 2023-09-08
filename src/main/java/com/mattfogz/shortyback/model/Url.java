package com.mattfogz.shortyback.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash("Url")  // This annotation indicates this object can be saved in Redis
public class Url {
    
    @Indexed  // This annotation allows querying in Redis, helping in findByLongUrl
    private String longUrl;
    
    @Id  // This is the primary key for Redis entries
    private String shortUrl;

    public Url() {}

    public Url(String longUrl, String shortUrl) {
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    // Optionally, you can override `hashCode()`, `equals()`, and `toString()`
}
