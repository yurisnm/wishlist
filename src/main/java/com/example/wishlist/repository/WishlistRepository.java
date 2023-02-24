package com.example.wishlist.repository;


import com.example.wishlist.model.WishlistItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WishlistRepository extends MongoRepository<WishlistItem, String> {
    Optional<WishlistItem> findByName(String name);
    boolean existsByName(String name);
}
