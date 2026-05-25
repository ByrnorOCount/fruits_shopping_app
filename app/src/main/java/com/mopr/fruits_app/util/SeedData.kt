package com.mopr.fruits_app.util

import com.mopr.fruits_app.R
import com.mopr.fruits_app.data.model.*

object SeedData {
    val fruits = listOf(
        Fruit("apple", "Apple", 1.99, R.drawable.img_fruit_apple, "Apples are high in fiber.", "Malus domestica", "High in fiber, Vitamin C.", "Pome"),
        Fruit("banana", "Banana", 0.50, R.drawable.img_fruit_banana, "Bananas are great for energy.", "Musa", "Rich in potassium.", "Tropical"),
        Fruit("cherry", "Cherry", 4.99, R.drawable.img_fruit_cherry, "Cherries are small, round fruits.", "Prunus avium", "Good source of fiber.", "Stone Fruit"),
        Fruit("date", "Date", 3.50, R.drawable.img_fruit_date, "Dates are sweet fruits.", "Phoenix dactylifera", "Excellent source of energy.", "Tropical"),
        Fruit("elderberry", "Elderberry", 6.00, R.drawable.img_fruit_elderberry, "Elderberries are small dark purple berries.", "Sambucus", "Known for immune-boosting properties.", "Berry"),
        Fruit("fig", "Fig", 2.25, R.drawable.img_fruit_fig, "Figs are unique fruits.", "Ficus carica", "Good source of dietary fiber.", "Tropical"),
        Fruit("grape", "Grape", 2.99, R.drawable.img_fruit_grape, "Grapes grow in clusters.", "Vitis vinifera", "Contains powerful antioxidants.", "Berry"),
        Fruit("honeydew", "Honeydew", 3.99, R.drawable.img_fruit_honeydew, "Honeydew is a sweet melon.", "Cucumis melo", "Hydrating and high in Vitamin C.", "Melon"),
        Fruit("kiwi", "Kiwi", 0.80, R.drawable.img_fruit_kiwi, "Kiwi is a fuzzy brown fruit.", "Actinidiae deliciosa", "Extremely high in Vitamin C.", "Tropical"),
        Fruit("lemon", "Lemon", 0.60, R.drawable.img_fruit_lemon, "Lemons are bright yellow citrus.", "Citrus limon", "Great source of Vitamin C.", "Citrus"),
        Fruit("mango", "Mango", 1.50, R.drawable.img_fruit_mango, "Mangoes are tropical fruits.", "Mangifera indica", "Rich in Vitamin A.", "Tropical"),
        Fruit("nectarine", "Nectarine", 1.20, R.drawable.img_fruit_nectarine, "Nectarines are similar to peaches.", "Prunus persica", "Good source of vitamins.", "Stone Fruit"),
        Fruit("orange", "Orange", 0.75, R.drawable.img_fruit_orange, "Oranges are popular citrus.", "Citrus x sinensis", "Boosts immune system.", "Citrus"),
        Fruit("papaya", "Papaya", 2.50, R.drawable.img_fruit_papaya, "Papayas are tropical fruits.", "Carica papaya", "Contains papain.", "Tropical"),
        Fruit("quince", "Quince", 3.00, R.drawable.img_fruit_quince, "Quince is a hard yellow fruit.", "Cydonia oblonga", "High in dietary fiber.", "Pome"),
        Fruit("raspberry", "Raspberry", 5.50, R.drawable.img_fruit_raspberry, "Raspberries are delicate berries.", "Rubus idaeus", "High in fiber.", "Berry"),
        Fruit("strawberry", "Strawberry", 4.00, R.drawable.img_fruit_strawberry, "Strawberries are popular red berries.", "Fragaria x ananassa", "Excellent source of Vitamin C.", "Berry")
    )

    val users = listOf(
        User("u1", "johndoe", "john@example.com", "password123", "+1-555-0101", listOf(Address("a1", "u1", "Home", "123 Apple St", "Cupertino", "95014", true), Address("a2", "u1", "Work", "1 Infinite Loop", "Cupertino", "95014", false)), false),
        User("u2", "janedoe", "jane@example.com", "password123", "+1-555-0202", listOf(Address("a3", "u2", "Home", "456 Banana Ave", "Florida", "32101", true)), false),
        User("u3", "alice", "alice@example.com", "password123", "+1-555-0303", listOf(Address("a4", "u3", "Studio", "789 Cherry Lane", "Traverse City", "49684", true)), false),
        User("u4", "bob", "bob@example.com", "password123", "+1-555-0404", listOf(Address("a5", "u4", "Farm", "101 Date Palm Way", "Indio", "92201", true)), false),
        User("u5", "admin", "admin@fruitapp.com", "admin123", "+1-555-9999", emptyList(), true)
    )

    val promotions = listOf(
        Promotion("p1", "SAVE10", 10, "10% off", true),
        Promotion("p2", "FREESHIP", 5, "Flat $5 discount", true),
        Promotion("p3", "WELCOME", 15, "15% off for new users", true)
    )

    val comments = listOf(
        Comment("c1", "u1", "johndoe", "apple", "Very crunchy!", 5),
        Comment("c2", "u2", "janedoe", "apple", "A bit sour.", 4),
        Comment("c3", "u3", "alice", "banana", "Perfectly ripe.", 5),
        Comment("c4", "u4", "bob", "mango", "The best!", 5)
    )

    val orders = listOf(
        Order("o1", "u1", listOf(CartItem("apple", "Apple", 2, 1.99, R.drawable.img_fruit_apple)), 3.98, System.currentTimeMillis() - 86400000, "Completed", Address("a1", "u1", "Home", "123 Apple St", "Cupertino", "95014", true), "Credit Card"),
        Order("o2", "u2", listOf(CartItem("banana", "Banana", 5, 0.50, R.drawable.img_fruit_banana)), 2.50, System.currentTimeMillis() - 172800000, "Completed", Address("a3", "u2", "Home", "456 Banana Ave", "Florida", "32101", true), "PayPal"),
        Order("o3", "u3", listOf(CartItem("mango", "Mango", 3, 1.50, R.drawable.img_fruit_mango)), 4.50, System.currentTimeMillis() - 259200000, "Completed", Address("a4", "u3", "Studio", "789 Cherry Lane", "Traverse City", "49684", true), "Cash on Delivery"),
        Order("o4", "u1", listOf(CartItem("strawberry", "Strawberry", 1, 4.00, R.drawable.img_fruit_strawberry)), 4.00, System.currentTimeMillis() - 345600000, "Completed", Address("a1", "u1", "Home", "123 Apple St", "Cupertino", "95014", true), "Credit Card")
    )
}
