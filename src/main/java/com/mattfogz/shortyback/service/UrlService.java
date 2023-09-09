package com.mattfogz.shortyback.service;

import com.mattfogz.shortyback.model.Url;
import com.mattfogz.shortyback.repository.UrlRepository;
import com.mattfogz.shortyback.exception.UrlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.validator.routines.UrlValidator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    private Map<String, String> longToShortUrlMapping = new HashMap<>();

    /**
     * Generates a random 6-character short URL string.
     *
     * @return Randomly generated short URL string
     */
    private String generateShortUrl() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }

        return sb.toString();
    }

    /**
     * Creates a short URL for the given long URL. If a custom short URL is
     * provided, it uses that, otherwise, it generates a random one.
     *
     * @param longUrl        Original long URL
     * @param customShortUrl Custom short URL provided by the user (can be null)
     * @return Created short URL
     * @throws UrlException if the URL format is invalid
     */
    public String createShortUrl(String longUrl, String customShortUrl) {
        // Normalize the long URL by converting it to lowercase and removing any
        // "http://" or "https://"
        String lowercaseLongUrl = normalizeLongUrl(longUrl);

        // Check if a custom short URL is provided. If not, check if the long URL
        // already exists with a generated short URL.
        String shortUrl;

        if (customShortUrl == null || customShortUrl.isEmpty()) {
            // Check if the long URL already exists with a generated short URL
            if (longToShortUrlMapping.containsKey(lowercaseLongUrl)) {
                // Use the existing generated short URL
                shortUrl = longToShortUrlMapping.get(lowercaseLongUrl);
            } else {
                // Generate a new short URL only if it's the first entry with this long URL
                shortUrl = generateShortUrl();

                // Ensure the desired short URL does not already exist in the database
                // (case-insensitive search)
                while (urlRepository.findById(shortUrl).isPresent()) {
                    shortUrl = generateShortUrl();
                }

                // Store the mapping of long URL to generated short URL
                longToShortUrlMapping.put(lowercaseLongUrl, shortUrl);
            }
        } else {
            // Use the custom short URL provided by the user
            shortUrl = customShortUrl;

            // Ensure the custom short URL does not already exist in the database
            // (case-insensitive search)
            if (urlRepository.findById(shortUrl).isPresent()) {
                throw new UrlException("Custom short URL already exists. Please choose another");
            }
        }

        // Validate the long URL
        if (!isValidUrl(lowercaseLongUrl)) {
            throw new UrlException("Invalid URL format.");
        }

        // Save the new mapping of long URL and short URL in the database
        Url url = new Url(lowercaseLongUrl, shortUrl);
        urlRepository.save(url);

        return shortUrl;
    }

    private String normalizeLongUrl(String longUrl) {
        // Check if "http://" or "https://" is present in the original URL, and if not,
        // prepend "https://www."
        if (!longUrl.startsWith("http://") && !longUrl.startsWith("https://")) {
            if (!longUrl.startsWith("www.")) {
                longUrl = "https://www." + longUrl;
            } else {
                longUrl = "https://" + longUrl;
            }
        }

        // Normalize the long URL by converting it to lowercase
        return longUrl.toLowerCase();
    }

    /**
     * Retrieves the original long URL corresponding to the provided short URL in a
     * case-insensitive manner. Ensures that the retrieved URL starts with at least
     * "http://". Also, increments the click count.
     *
     * @param shortUrl Short URL to look up (case-insensitive).
     * @return Original long URL
     * @throws UrlException if the provided short URL is not found in the database
     */
    public String getLongUrl(String shortUrl) {
        // Perform a case-insensitive lookup
        Optional<Url> url = StreamSupport.stream(urlRepository.findAll().spliterator(), false)
                .filter(u -> u.getShortUrl().equalsIgnoreCase(shortUrl))
                .findFirst();

        if (url.isPresent()) {
            // Normalize the long URL by ensuring it starts with at least "http://"
            String longUrl = url.get().getLongUrl();
            if (!longUrl.startsWith("http://") && !longUrl.startsWith("https://")) {
                longUrl = "http://" + longUrl;
            }

            // Increment the click count
            Url urlEntity = url.get();
            urlEntity.setClickCount(urlEntity.getClickCount() + 1);
            urlRepository.save(urlEntity);

            // Convert the long URL to lowercase before returning
            String lowercaseLongUrl = longUrl.toLowerCase();
            return lowercaseLongUrl;
        } else {
            throw new UrlException("Short URL not found.");
        }
    }

    /**
     * Retrieves the click count for the given short URL.
     *
     * @param shortUrl Short URL to retrieve the click count for.
     * @return Click count
     * @throws UrlException if the provided short URL is not found in the database
     */
    public int getClickCount(String shortUrl) {
        // Implement the logic to retrieve the click count for the given short URL
        // You can use your repository or any other data source for this
        Optional<Url> url = urlRepository.findById(shortUrl);
        if (url.isPresent()) {
            return url.get().getClickCount();
        } else {
            throw new UrlException("Short URL not found.");
        }
    }

    /**
     * Updates the long URL corresponding to the provided short URL in a
     * case-insensitive manner.
     *
     * @param shortUrl   Short URL to update (case-insensitive).
     * @param newLongUrl New long URL to associate with the short URL
     * @throws UrlException if the provided short URL is not found in the database
     */
    public void updateUrl(String shortUrl, String newLongUrl) {
        // Perform a case-insensitive lookup
        List<Url> allUrls = StreamSupport.stream(urlRepository.findAll().spliterator(), false)
                .filter(u -> u.getShortUrl().equalsIgnoreCase(shortUrl))
                .collect(Collectors.toList());

        if (allUrls.isEmpty()) {
            throw new UrlException("Short URL not found. Cannot update.");
        }

        // Update each matching URL
        for (Url existingUrl : allUrls) {
            Url updatedUrl = new Url(newLongUrl, existingUrl.getShortUrl());
            urlRepository.save(updatedUrl);
        }
    }

    /**
     * Deletes the mapping of the provided short URL from the database.
     * 
     * @param shortUrl Short URL to delete
     * @throws UrlException if the provided short URL is not found in the database
     */
    public void deleteShortUrl(String shortUrl) {
        Optional<Url> existingUrl = urlRepository.findById(shortUrl);

        if (!existingUrl.isPresent()) {
            // If not found, perform a case-insensitive search and delete if found
            List<Url> urls = StreamSupport.stream(urlRepository.findAll().spliterator(), false)
                    .filter(url -> url.getShortUrl().equalsIgnoreCase(shortUrl))
                    .collect(Collectors.toList());

            if (!urls.isEmpty()) {
                urlRepository.deleteById(urls.get(0).getShortUrl());
            } else {
                throw new UrlException("Short URL not found. Cannot delete.");
            }
        } else {
            // Delete the found URL
            urlRepository.deleteById(shortUrl);
        }
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

    /**
     * Retrieves all the URL mappings (both long and short) from the database,
     * including click counts.
     * 
     * @return List of URL mappings with click counts
     */
    public List<Map<String, String>> getAllUrlsWithClickCount() {
        Iterable<Url> urls = urlRepository.findAll();
        List<Map<String, String>> urlList = new ArrayList<>();

        for (Url url : urls) {
            Map<String, String> urlInfo = new HashMap<>();
            urlInfo.put("longUrl", url.getLongUrl());
            urlInfo.put("shortUrl", url.getShortUrl());
            urlInfo.put("clickCount", String.valueOf(url.getClickCount())); // Add click count to the response
            urlList.add(urlInfo);
        }

        return urlList;
    }

    /**
     * Validates whether a given URL is valid.
     *
     * @param url The URL to validate.
     * @return true if the URL is valid, false otherwise.
     */
    public boolean isValidUrl(String url) {
        // Define URL validation options (you can customize this as needed)
        UrlValidator urlValidator = new UrlValidator(
                new String[] { "http", "https" },
                UrlValidator.ALLOW_LOCAL_URLS);

        // Check if the URL is valid
        return urlValidator.isValid(url);
    }
}
