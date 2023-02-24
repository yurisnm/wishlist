package com.example.wishlist.service;

import com.example.wishlist.model.WishlistItem;
import com.example.wishlist.repository.WishlistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishlistServiceImpl implements WishlistService {

    private static final Logger logger = LoggerFactory.getLogger(WishlistServiceImpl.class);

    @Autowired
    private WishlistRepository repository;

    public WishlistServiceImpl(WishlistRepository wishlistRepository) {
        this.repository = wishlistRepository;
    }

    public WishlistServiceImpl() {

    }

    @Override
    public WishlistItem addItem(WishlistItem item) {
        logger.info("Adding item {} to wishlist.", item.getName());
        if (getItemsCount() < 20) {
            repository.save(item);
            logger.info("Item {} added to wishlist.", item.getName());
        } else {
            logger.error("Wishlist is full. Cannot add more items.");
            throw new IllegalStateException("Wishlist is full. Cannot add more items.");
        }
        return item;
    }

    @Override
    public boolean removeItem(String id) {
        logger.info("Removing item with id {} from wishlist.", id);
        boolean exists = repository.existsById(id);
        if (exists) {
            repository.deleteById(id);
            logger.info("Item with id {} removed from wishlist.", id);
        } else {
            logger.warn("Item with id {} not found in wishlist.", id);
        }
        return exists;
    }

    @Override
    public List<WishlistItem> getAllItems() {
        logger.info("Retrieving all items from wishlist.");
        return repository.findAll();
    }

    @Override
    public Optional<WishlistItem> getItemById(String id) {
        logger.info("Retrieving item with id {} from wishlist.", id);
        return repository.findById(id);
    }

    @Override
    public int getItemsCount() {
        int count = (int) repository.count();
        logger.info("Number of items in wishlist: {}", count);
        return count;
    }
}
