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

    /**
     * Constructs a new WishlistServiceImpl instance with the provided WishlistRepository.
     *
     * @param wishlistRepository the repository to be used to store and retrieve wishlist items.
     */
    public WishlistServiceImpl(WishlistRepository wishlistRepository) {
        this.repository = wishlistRepository;
    }

    /**
     * Constructs a new WishlistServiceImpl instance.
     */
    public WishlistServiceImpl() {

    }

    /**
     * Adds a new item to the wishlist.
     *
     * @param item the item to be added to the wishlist.
     * @return the item that was added to the wishlist.
     * @throws IllegalStateException if the wishlist already has the maximum number of items allowed.
     */
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

    /**
     * Removes an item from the wishlist with the given id.
     *
     * @param id the id of the item to be removed.
     * @return true if an item was removed, false otherwise.
     */
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

    /**
     * Retrieves all items from the wishlist.
     *
     * @return a list containing all wishlist items.
     */
    @Override
    public List<WishlistItem> getAllItems() {
        logger.info("Retrieving all items from wishlist.");
        return repository.findAll();
    }

    /**
     * Retrieves an item from the wishlist with the given id.
     *
     * @param id the id of the item to be retrieved.
     * @return an Optional containing the item with the given id, or an empty Optional if the item was not found.
     */
    @Override
    public Optional<WishlistItem> getItemById(String id) {
        logger.info("Retrieving item with id {} from wishlist.", id);
        return repository.findById(id);
    }

    /**
     * Retrieves the number of items in the wishlist.
     *
     * @return the number of items in the wishlist.
     */
    @Override
    public int getItemsCount() {
        int count = (int) repository.count();
        logger.info("Number of items in wishlist: {}", count);
        return count;
    }
}
