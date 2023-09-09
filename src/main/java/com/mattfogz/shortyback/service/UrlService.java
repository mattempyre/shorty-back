package com.mattfogz.shortyback.service;

import com.mattfogz.shortyback.model.Url;
import com.mattfogz.shortyback.repository.UrlRepository;
import com.mattfogz.shortyback.exception.UrlException;  // Import for custom exceptions related to URL handling

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service   // Marks this class as a Spring Service component
public class UrlService {

    @Autowired  // Automatically injects an instance of UrlRepository into this class
    private UrlRepository urlRepository;

    /**
     * Generates a random 6-character short URL string.
     * 
     * @return Randomly generated short URL string
     */
    private String generateShortUrl() {
        // Characters that can be used for the short URL
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(6); // Initialize a StringBuilder for 6 characters
        
        // Loop to randomly pick 6 characters from the chars string
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        
        return sb.toString();
    }

    /**
     * Creates a short URL for the given long URL. If a custom short URL is provided,
     * it uses that, otherwise, it generates a random one.
     * 
     * @param longUrl Original long URL
     * @param customShortUrl Custom short URL provided by the user (can be null)
     * @return Created short URL
     * @throws UrlException if the desired short URL already exists
     */
    public String createShortUrl(String longUrl, String customShortUrl) {
        // Check if this long URL already exists in the database
        Optional<Url> existingUrl = urlRepository.findByLongUrl(longUrl);
        
        if (existingUrl.isPresent()) {
            // Return the existing short URL if the long URL is already present
            return existingUrl.get().getShortUrl();
        } else {
            // Check if a custom short URL is provided. If not, generate one
            String shortUrl = (customShortUrl == null || customShortUrl.isEmpty()) ? generateShortUrl() : customShortUrl;

            // Ensure the desired short URL does not already exist in the database
            if(urlRepository.findById(shortUrl).isPresent()) {
                throw new UrlException("Short URL already exists. Please choose another");
            }

            // Save the new mapping of long URL and short URL in the database
            Url url = new Url(longUrl, shortUrl);
            urlRepository.save(url);
            
            return shortUrl;
        }
    }

    /**
     * Retrieves the original long URL corresponding to the provided short URL.
     * 
     * @param shortUrl Short URL to look up
     * @return Original long URL
     * @throws UrlException if the provided short URL is not found in the database
     */
    public String getLongUrl(String shortUrl) {
        Optional<Url> url = urlRepository.findById(shortUrl);
        return url.map(Url::getLongUrl).orElseThrow(() -> new UrlException("Short URL not found."));
    }

    /**
     * Updates the long URL corresponding to the provided short URL.
     * 
     * @param shortUrl Short URL to update
     * @param newLongUrl New long URL to associate with the short URL
     * @throws UrlException if the provided short URL is not found in the database
     */
    public void updateUrl(String shortUrl, String newLongUrl) {
        Optional<Url> existingUrl = urlRepository.findById(shortUrl);
        
        if (!existingUrl.isPresent()) {
            throw new UrlException("Short URL not found. Cannot update.");
        }
        
        Url url = new Url(newLongUrl, shortUrl);
        urlRepository.save(url);
    }

    /**
     * Deletes the mapping of the provided short URL from the database.
     * 
     * @param shortUrl Short URL to delete
     */
    public void deleteShortUrl(String shortUrl) {
        urlRepository.deleteById(shortUrl);
    }

    /**
     * Retrieves all the URL mappings (both long and short) from the database.
     * 
     * @return List of URL mappings
     */
    public List<Url> getAllUrls() {
        Iterable<Url> urls = urlRepository.findAll();
        // Convert Iterable<Url> to List<Url>
        return StreamSupport.stream(urls.spliterator(), false).collect(Collectors.toList());
    }
}