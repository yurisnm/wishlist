package com.example.wishlist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WishlistItemNotFoundException extends RuntimeException {
    private static final Logger LOGGER = LoggerFactory.getLogger(WishlistItemNotFoundException.class);

    /**
     * Constructs a new WishlistItemNotFoundException with the given message.
     *
     * @param message The detail message.
     */
    public WishlistItemNotFoundException(String message) {
        super(message);
        LOGGER.error("WishlistItemNotFoundException: {}", message);
    }
}

