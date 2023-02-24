package com.example.wishlist.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WishlistItem {
    private String id;
    private String name;

    /**
     * Constructor used by Jackson to deserialize JSON data into a WishlistItem object.
     *
     * @param name The name of the wishlist item.
     * @param id The ID of the wishlist item.
     */
    @JsonCreator
    public WishlistItem(@JsonProperty("name") String name, @JsonProperty("id") String id) {
        this.name = name;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

