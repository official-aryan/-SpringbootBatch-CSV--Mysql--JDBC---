package com.Springboot_batch.Config;

import com.Springboot_batch.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor implements ItemProcessor<Product, Product> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomItemProcessor.class);

    @Override
    public Product process(Product item) throws Exception {
        try {
            // Log the values of discount and price
            LOGGER.info("Discount: {}", item.getDiscount());
            LOGGER.info("Price: {}", item.getPrice());

            // Trim the discount value and check if it's empty
            String discountStr = item.getDiscount().trim();
            if (discountStr.isEmpty()) {
                // Handle empty discount value (optional)
                LOGGER.warn("Discount value is empty for product: {}", item);
                return item; // Skip further processing
            }

            // Parse the discount value to an integer
            int discountPer = Integer.parseInt(discountStr);
            double originalPrice = Double.parseDouble(item.getPrice().trim());
            double discount = (discountPer / 100.0) * originalPrice;
            double finalPrice = originalPrice - discount;
            item.setDiscountedPrice(String.valueOf(finalPrice));
        } catch (NumberFormatException ex) {
            // Log and handle parsing errors
            LOGGER.error("Error processing item: {}", item, ex);
            // Optionally, you can rethrow the exception to stop the batch job
            // throw ex;
        }

        return item;
    }
}
