package com.mattfogz.shortyback.repository;

import com.mattfogz.shortyback.model.Url;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UrlRepository extends CrudRepository<Url, String> {
    Optional<Url> findByLongUrl(String longUrl);
}