package com.budiluhur.product_catalog;

import java.text.NumberFormat;
import java.util.Locale;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.budiluhur.product_catalog.model.Product;
import com.budiluhur.product_catalog.service.ProductService;

@SpringBootApplication
public class ProductCatalogApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductCatalogApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(ProductService productService) {
        return args -> {

            // Format mata uang Indonesia
            NumberFormat rupiah =
                    NumberFormat.getCurrencyInstance(
                            Locale.forLanguageTag("id-ID"));

            // Tambahkan produk monitor
            productService.createProduct(
                new Product(
                    "PRD-03",
                    "Monitor Gaming 24 Inch",
                    2100000.0
                )
            );

            System.out.println(
                "Daftar Produk di Spring Container:");

            productService.getAllProducts()
                    .forEach(System.out::println);

            System.out.println();
            System.out.println(
                "--- INFORMASI HARGA & DISKON ---");

            // Produk yang ingin ditampilkan
            String[][] produkDiskon = {
                {"PRD-01", "10"},
                {"PRD-02", "0"}
            };

            for (String[] data : produkDiskon) {

                String id = data[0];
                double diskon = Double.parseDouble(data[1]);

                Product product =
                        productService.getProductById(id);

                double hargaNormal = product.getPrice();

                double hargaAkhir =
                        productService.getDiscountedProductPrice(
                                id, diskon);

                String statusDiskon =
                        diskon > 0
                        ? "Ada Diskon"
                        : "Tidak Ada Diskon";

                System.out.println();
                System.out.println(
                    "[" + product.getId() + "] "
                    + product.getName());

                System.out.println(
                    "- Harga Normal : "
                    + rupiah.format(hargaNormal));

                System.out.println(
                    "- Diskon       : "
                    + diskon + "% (" + statusDiskon + ")");

                System.out.println(
                    "- Harga Akhir  : "
                    + rupiah.format(hargaAkhir));
            }
        };
    }
}