package com.example.wishlist;

import com.example.wishlist.controller.WishlistController;
import com.example.wishlist.model.WishlistItem;
import com.example.wishlist.repository.WishlistRepository;
import com.example.wishlist.service.WishlistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class WishlistServiceApplicationTests {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WishlistController wishlistController;

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private WishlistRepository wishlistRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public WishlistServiceApplicationTests() {
    }

    @Before
    public void clearDatabase() {
        wishlistRepository.deleteAll();
    }

    @Test
    public void testAddItemToWishlist() throws Exception {
        WishlistItem item = new WishlistItem("iPhone 13", "1");
        String itemJson = objectMapper.writeValueAsString(item);
        mockMvc.perform(post("/wishlist/add").contentType(MediaType.APPLICATION_JSON).content(itemJson))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Item added to the wishlist with ID")));
        List<WishlistItem> items = wishlistService.getAllItems();
        assertEquals(1, items.size());
        assertEquals("iPhone 13", items.get(0).getName());
    }

    @Test
    public void testAddDuplicateItemToWishlist() throws Exception {
        WishlistItem item = new WishlistItem("iPhone 13", "1");
        String itemJson = objectMapper.writeValueAsString(item);
        mockMvc.perform(post("/wishlist/add").contentType(MediaType.APPLICATION_JSON).content(itemJson))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Item added to the wishlist with ID")));
        WishlistItem duplicateItem = new WishlistItem("iPhone 13", "1");
        String duplicateItemJson = objectMapper.writeValueAsString(duplicateItem);
        mockMvc.perform(post("/com/example/wishlist/add").contentType(MediaType.APPLICATION_JSON).content(duplicateItemJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Item already exists in the wishlist")));
        List<WishlistItem> items = wishlistService.getAllItems();
        assertEquals(1, items.size());
    }

    @Test
    public void testAddMaxItemsToWishlist() throws Exception {
        for (int i = 1; i <= 20; i++) {
            WishlistItem item = new WishlistItem("Item " + i, String.valueOf(i) );
            String itemJson = objectMapper.writeValueAsString(item);
            mockMvc.perform(post("/wishlist/add").contentType(MediaType.APPLICATION_JSON).content(itemJson))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(containsString("Item added to the wishlist with ID")));
        }
        WishlistItem extraItem = new WishlistItem("Extra item", "21");
        String extraItemJson = objectMapper.writeValueAsString(extraItem);
        mockMvc.perform(post("/com/example/wishlist/add").contentType(MediaType.APPLICATION_JSON).content(extraItemJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Wishlist is full")));
        List<WishlistItem> items = wishlistService.getAllItems();
        assertEquals(20, items.size());
    }

    @Test
    public void testRemoveItemFromWishlist() throws Exception {
        WishlistItem item = new WishlistItem("iPhone 13", "1");
        WishlistItem addedItem = wishlistService.addItem(item);
        mockMvc.perform(delete("/wishlist/remove/" + addedItem.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Item with ID " + addedItem.getId() + " removed from the wishlist")));
        List<WishlistItem> items = wishlistService.getAllItems();
        assertEquals(0, items.size());
    }

    @Test
    public void testRemoveNonexistentItemFromWishlist() throws Exception {
        mockMvc.perform(delete("/wishlist/remove/nonexistent-id"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Item with ID nonexistent-id not found")));
        List<WishlistItem> items = wishlistService.getAllItems();
        assertEquals(0, items.size());
    }
}

