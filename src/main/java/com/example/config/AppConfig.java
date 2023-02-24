package com.example.config;

import com.example.wishlist.service.WishlistService;
import com.example.wishlist.service.WishlistServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.example")
public class AppConfig {

    @Bean
    public WishlistService wishlistService() {
        return new WishlistServiceImpl();
    }
}
