package com.example.wishlist.controller;

import com.example.wishlist.exception.WishlistItemNotFoundException;
import com.example.wishlist.model.WishlistItem;
import com.example.wishlist.service.WishlistService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class WishlistController {

    private static final Logger logger = LoggerFactory.getLogger(WishlistController.class);

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    /**
     * Get all wishlist items.
     *
     * @return List of all wishlist items.
     */
    @GetMapping
    public List<WishlistItem> getAllWishlistItems() {
        logger.info("Received request to retrieve all wishlist items.");
        return wishlistService.getAllItems();
    }

    /**
     * Get a specific wishlist item by ID.
     *
     * @param id The ID of the wishlist item to retrieve.
     * @return The wishlist item with the given ID.
     * @throws WishlistItemNotFoundException If the given ID does not correspond to a valid wishlist item.
     */
    @GetMapping("/{id}")
    public Optional<WishlistItem> getWishlistItemById(@PathVariable String id) throws WishlistItemNotFoundException {
        logger.info("Received request to retrieve wishlist item with ID: {}", id);
        return wishlistService.getItemById(id);
    }

    /**
     * Add a new wishlist item.
     *
     * @param wishlistItem The wishlist item to add.
     * @return The newly added wishlist item.
     */
    @PostMapping("/add")
    public WishlistItem addWishlistItem(@RequestBody WishlistItem wishlistItem) {
        logger.info("Received request to add wishlist item with name: {}", wishlistItem.getName());
        return wishlistService.addItem(wishlistItem);
    }

    /**
     * Remove a wishlist item by ID.
     *
     * @param id The ID of the wishlist item to remove.
     * @throws WishlistItemNotFoundException If the given ID does not correspond to a valid wishlist item.
     */
    @DeleteMapping("/remove/{id}")
    public void removeWishlistItem(@PathVariable String id) throws WishlistItemNotFoundException {
        logger.info("Received request to remove wishlist item with ID: {}", id);
        wishlistService.removeItem(id);
    }

}


