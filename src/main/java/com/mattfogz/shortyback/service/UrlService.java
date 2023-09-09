package com.mattfogz.shortyback.service;

import com.mattfogz.shortyback.model.Url;
import com.mattfogz.shortyback.repository.UrlRepository;
import com.mattfogz.shortyback.exception.UrlException; // Import the custom exception

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    private String generateShortUrl() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public String createShortUrl(String longUrl, String customShortUrl) {
        Optional<Url> existingUrl = urlRepository.findByLongUrl(longUrl);
        if (existingUrl.isPresent()) {
            return existingUrl.get().getShortUrl();
        } else {
            String shortUrl = (customShortUrl == null || customShortUrl.isEmpty()) ? generateShortUrl() : customShortUrl;

            if(urlRepository.findById(shortUrl).isPresent()) {
                throw new UrlException("Short URL already exists. Please choose another"); // Use custom exception
            }

            Url url = new Url(longUrl, shortUrl);
            urlRepository.save(url);
            return shortUrl;
        }
    }

    public String getLongUrl(String shortUrl) {
        Optional<Url> url = urlRepository.findById(shortUrl);
        return url.map(Url::getLongUrl).orElseThrow(() -> new UrlException("Short URL not found.")); // Use custom exception
    }

    public void updateUrl(String shortUrl, String newLongUrl) {
        Optional<Url> existingUrl = urlRepository.findById(shortUrl);
        if (!existingUrl.isPresent()) {
            throw new UrlException("Short URL not found. Cannot update."); // Use custom exception
        }
        Url url = new Url(newLongUrl, shortUrl);
        urlRepository.save(url);
    }

    public void deleteShortUrl(String shortUrl) {
        urlRepository.deleteById(shortUrl);
    }

    public List<Url> getAllUrls() {
        Iterable<Url> urls = urlRepository.findAll();
        return StreamSupport.stream(urls.spliterator(), false)
                            .collect(Collectors.toList());
    }
}