package com.example.wishlist;

import com.example.wishlist.controller.WishlistController;
import com.example.wishlist.model.WishlistItem;
import com.example.wishlist.repository.WishlistRepository;
import com.example.wishlist.service.WishlistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:test.properties")
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
    @Description("There must be no data if nothing added.")
    public void testAllItemsFromWishlist() throws Exception {
        mockMvc.perform(get("/wishlist"))
                .andExpect(status().isOk());
        List<WishlistItem> items = wishlistService.getAllItems();
        assertEquals(0, items.size());
    }

    @Test
    @Description("Must have only 1 item when adding only 1 item.")
    public void testAddWishlistItem() throws Exception {
        WishlistItem item = new WishlistItem("iPhone 13", "1");

        mockMvc.perform(post("/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("iPhone 13")))
                .andExpect(jsonPath("$.id", is("1")));
        List<WishlistItem> items = wishlistService.getAllItems();
        assertEquals(1, items.size());
    }

    @Test
    @Description("Must act as item added to guarantee idempotency")
    public void testAddDuplicateItemToWishlist() throws Exception {
        WishlistItem item = new WishlistItem("iPhone 13", "1");
        String itemJson = objectMapper.writeValueAsString(item);
        mockMvc.perform(post("/add").contentType(MediaType.APPLICATION_JSON).content(itemJson))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("iPhone 13")));
        List<WishlistItem> items = wishlistService.getAllItems();
        assertEquals(1, items.size());
        WishlistItem duplicateItem = new WishlistItem("iPhone 13", "1");
        String duplicateItemJson = objectMapper.writeValueAsString(duplicateItem);
        mockMvc.perform(post("/add").contentType(MediaType.APPLICATION_JSON).content(duplicateItemJson))
                .andExpect(status().isOk());
        List<WishlistItem> sameItems = wishlistService.getAllItems();
        assertEquals(1, sameItems.size());
    }

    @Test
    @Description("Must throw exception when trying to add more than 20 items")
    public void testAddMaxItemsToWishlist() throws Exception {
        for (int i = 1; i <= 20; i++) {
            WishlistItem item = new WishlistItem("Item " + i, String.valueOf(i) );
            String itemJson = objectMapper.writeValueAsString(item);
            mockMvc.perform(post("/add").contentType(MediaType.APPLICATION_JSON).content(itemJson))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("Item " + i)));
        }
        WishlistItem item = new WishlistItem("Item 21", "21");
        assertThrows(RuntimeException.class, () -> wishlistController.addWishlistItem(item));
    }

    @Test
    @Description("Must successfully remove existent item")
    public void testRemoveItemFromWishlist() throws Exception {
        WishlistItem item = new WishlistItem("iPhone 13", "1");
        WishlistItem addedItem = wishlistService.addItem(item);
        List<WishlistItem> items = wishlistService.getAllItems();
        assertEquals(1, items.size());
        mockMvc.perform(delete("/remove/" + addedItem.getId()))
                .andExpect(status().isOk());
        List<WishlistItem> itemsNew = wishlistService.getAllItems();
        assertEquals(0, itemsNew.size());
    }

    @Test
    @Description("Must successfully remove non-existent item do guarantee idempotency")
    public void testRemoveNonexistentItemFromWishlist() throws Exception {
        mockMvc.perform(delete("/remove/nonexistent-id"))
                .andExpect(status().isOk());
        List<WishlistItem> items = wishlistService.getAllItems();
        assertEquals(0, items.size());
    }
}

