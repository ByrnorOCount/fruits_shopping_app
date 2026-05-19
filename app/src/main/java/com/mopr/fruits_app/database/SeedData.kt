package com.mopr.fruits_app.database

import com.mopr.fruits_app.R
import com.mopr.fruits_app.models.Fruit
import com.mopr.fruits_app.models.User

object SeedData {
    val fruits = listOf(
        Fruit(
            "Apple",
            R.drawable.img_fruit_apple,
            "Apples are high in fiber and Vitamin C. They are one of the most popular fruits in the world.",
            "Malus domestica",
            "High in fiber, Vitamin C, and various antioxidants.",
        ),
        Fruit(
            "Banana",
            R.drawable.img_fruit_banana,
            "Bananas are great for energy and potassium. They are naturally wrapped and convenient for snacks.",
            "Musa",
            "Rich in potassium, Vitamin B6, and Vitamin C.",
        ),
        Fruit(
            "Cherry",
            R.drawable.img_fruit_cherry,
            "Cherries are small, round fruits that can be sweet or sour. They are known for their deep red color.",
            "Prunus avium",
            "Good source of fiber, vitamins, and minerals including potassium and calcium.",
        ),
        Fruit(
            "Date",
            R.drawable.img_fruit_date,
            "Dates are sweet fruits from the date palm tree. They are often eaten dried.",
            "Phoenix dactylifera",
            "Excellent source of energy, fiber, and various vitamins and minerals.",
        ),
        Fruit(
            "Elderberry",
            R.drawable.img_fruit_elderberry,
            "Elderberries are small dark purple berries that grow in clusters.",
            "Sambucus",
            "Known for immune-boosting properties and high antioxidant content.",
        ),
        Fruit(
            "Fig",
            R.drawable.img_fruit_fig,
            "Figs are unique fruits with a soft, sweet flesh and many small seeds.",
            "Ficus carica",
            "Good source of dietary fiber and minerals like potassium and calcium.",
        ),
        Fruit(
            "Grape",
            R.drawable.img_fruit_grape,
            "Grapes grow in clusters and can be eaten fresh or used for making wine.",
            "Vitis vinifera",
            "Contains powerful antioxidants like resveratrol.",
        ),
        Fruit(
            "Honeydew",
            R.drawable.img_fruit_honeydew,
            "Honeydew is a sweet, pale green melon with a smooth rind.",
            "Cucumis melo",
            "Hydrating and high in Vitamin C and potassium.",
        ),
        Fruit(
            "Kiwi",
            R.drawable.img_fruit_kiwi,
            "Kiwi is a fuzzy brown fruit with vibrant green flesh and tiny black seeds.",
            "Actinidia deliciosa",
            "Extremely high in Vitamin C and good for digestion.",
        ),
        Fruit(
            "Lemon",
            R.drawable.img_fruit_lemon,
            "Lemons are bright yellow citrus fruits known for their sour taste.",
            "Citrus limon",
            "Great source of Vitamin C and supports heart health.",
        ),
        Fruit(
            "Mango",
            R.drawable.img_fruit_mango,
            "Mangoes are tropical fruits with sweet, juicy yellow-orange flesh.",
            "Mangifera indica",
            "Rich in Vitamin A, Vitamin C, and folate.",
        ),
        Fruit(
            "Nectarine",
            R.drawable.img_fruit_nectarine,
            "Nectarines are similar to peaches but have smooth skin.",
            "Prunus persica var. nucipersica",
            "Good source of vitamins and minerals for healthy skin.",
        ),
        Fruit(
            "Orange",
            R.drawable.img_fruit_orange,
            "Oranges are popular citrus fruits known for their high Vitamin C content.",
            "Citrus x sinensis",
            "Boosts immune system and provides healthy fiber.",
        ),
        Fruit(
            "Papaya",
            R.drawable.img_fruit_papaya,
            "Papayas are tropical fruits with orange flesh and many black seeds.",
            "Carica papaya",
            "Contains papain, an enzyme that aids digestion.",
        ),
        Fruit(
            "Quince",
            R.drawable.img_fruit_quince,
            "Quince is a hard, yellow fruit that is usually cooked before eating.",
            "Cydonia oblonga",
            "High in dietary fiber and beneficial antioxidants.",
        ),
        Fruit(
            "Raspberry",
            R.drawable.img_fruit_raspberry,
            "Raspberries are delicate red berries with a sweet-tart flavor.",
            "Rubus idaeus",
            "High in fiber and anti-inflammatory properties.",
        ),
        Fruit(
            "Strawberry",
            R.drawable.img_fruit_strawberry,
            "Strawberries are popular red berries with seeds on the outside.",
            "Fragaria x ananassa",
            "Excellent source of Vitamin C and manganese.",
        )
    )

    val users = listOf(
        User(
            username = "johndoe",
            email = "john@example.com",
            password = "password123",
            isAdmin = false
        ),
        User(
            username = "admin",
            email = "admin@fruitapp.com",
            password = "admin123",
            isAdmin = true
        )
    )
}
