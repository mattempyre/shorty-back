package com.mattfogz.shortyback.repository;

import com.mattfogz.shortyback.model.Url;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Repository interface for CRUD operations on Url entities.
 *
 * By extending CrudRepository, this interface inherits methods for CRUD operations.
 * Spring Data JPA will provide a default implementation of this interface out of the box.
 * The entity being managed is Url and its primary key is of type String.
 */
public interface UrlRepository extends CrudRepository<Url, String> {

    /**
     * Custom query method to find a Url entity by its long URL (case-insensitive).
     *
     * @param longUrl The long URL to search for (case-insensitive).
     * @return An Optional containing the found Url entity, or empty if not found.
     */
    Optional<Url> findByLongUrlIgnoreCase(String longUrl);
}