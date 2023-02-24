package com.example.wishlist.service;

import com.example.wishlist.model.WishlistItem;
import java.util.List;
import java.util.Optional;

public interface WishlistService {
    WishlistItem addItem(WishlistItem item);

    boolean removeItem(String id);

    List<WishlistItem> getAllItems();

    Optional<WishlistItem> getItemById(String id);

    int getItemsCount();
}