package com.example.config;

import com.example.wishlist.service.WishlistService;
import com.example.wishlist.service.WishlistServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the application.
 */
@Configuration
@ComponentScan("com.example")
public class AppConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);

    /**
     * Configures the {@link WishlistService} bean to be used in the application.
     *
     * @return The {@link WishlistService} bean.
     */
    @Bean
    public WishlistService wishlistService() {
        LOGGER.info("Creating WishlistServiceImpl bean...");
        return new WishlistServiceImpl();
    }
}
