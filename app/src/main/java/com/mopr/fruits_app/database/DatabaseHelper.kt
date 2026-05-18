package com.mopr.fruits_app.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.mopr.fruits_app.models.Fruit
import com.mopr.fruits_app.models.User

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "fruits_app.db"
        private const val DATABASE_VERSION = 5

        // User table
        private const val TABLE_USERS = "users"
        private const val KEY_USER_ID = "id"
        private const val KEY_USER_NAME = "username"
        private const val KEY_USER_EMAIL = "email"
        private const val KEY_USER_PASSWORD = "password"
        private const val KEY_USER_IS_ADMIN = "is_admin"

        // Fruit table
        private const val TABLE_FRUITS = "fruits"
        private const val KEY_FRUIT_ID = "id"
        private const val KEY_FRUIT_NAME = "name"
        private const val KEY_FRUIT_IMAGE = "image_res"
        private const val KEY_FRUIT_DESC = "description"
        private const val KEY_FRUIT_SCIENTIFIC = "scientific_name"
        private const val KEY_FRUIT_BENEFITS = "health_benefits"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUserTable = ("CREATE TABLE " + TABLE_USERS + "("
                + KEY_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USER_NAME + " TEXT,"
                + KEY_USER_EMAIL + " TEXT,"
                + KEY_USER_PASSWORD + " TEXT,"
                + KEY_USER_IS_ADMIN + " INTEGER" + ")")
        db.execSQL(createUserTable)

        val createFruitTable = ("CREATE TABLE " + TABLE_FRUITS + "("
                + KEY_FRUIT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_FRUIT_NAME + " TEXT,"
                + KEY_FRUIT_IMAGE + " INTEGER,"
                + KEY_FRUIT_DESC + " TEXT,"
                + KEY_FRUIT_SCIENTIFIC + " TEXT,"
                + KEY_FRUIT_BENEFITS + " TEXT" + ")")
        db.execSQL(createFruitTable)

        seedDatabase(db)
    }

    private fun seedDatabase(db: SQLiteDatabase) {
        // Seed Users
        SeedData.users.forEach { user ->
            val values = ContentValues().apply {
                put(KEY_USER_NAME, user.username)
                put(KEY_USER_EMAIL, user.email)
                put(KEY_USER_PASSWORD, user.password)
                put(KEY_USER_IS_ADMIN, if (user.isAdmin) 1 else 0)
            }
            db.insert(TABLE_USERS, null, values)
        }

        // Seed Fruits
        SeedData.fruits.forEach { fruit ->
            val values = ContentValues().apply {
                put(KEY_FRUIT_NAME, fruit.name)
                put(KEY_FRUIT_IMAGE, fruit.imageRes)
                put(KEY_FRUIT_DESC, fruit.description)
                put(KEY_FRUIT_SCIENTIFIC, fruit.scientificName)
                put(KEY_FRUIT_BENEFITS, fruit.healthBenefits)
            }
            db.insert(TABLE_FRUITS, null, values)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FRUITS")
        onCreate(db)
    }

    // --- User Operations ---

    fun addUser(user: User): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_USER_NAME, user.username)
            put(KEY_USER_EMAIL, user.email)
            put(KEY_USER_PASSWORD, user.password)
            put(KEY_USER_IS_ADMIN, if (user.isAdmin) 1 else 0)
        }
        val id = db.insert(TABLE_USERS, null, values)
        db.close()
        return id
    }

    fun getUser(username: String, password: String): User? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            null,
            "$KEY_USER_NAME=? AND $KEY_USER_PASSWORD=?",
            arrayOf(username, password),
            null, null, null
        )

        var user: User? = null
        if (cursor.moveToFirst()) {
            user = User(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_USER_ID)),
                username = cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_NAME)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_EMAIL)),
                password = cursor.getString(cursor.getColumnIndexOrThrow(KEY_USER_PASSWORD)),
                isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_USER_IS_ADMIN)) == 1
            )
        }
        cursor.close()
        db.close()
        return user
    }

    fun getUserCount(): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USERS", null)
        val count = cursor.count
        cursor.close()
        return count
    }

    // --- Fruit Operations ---

    fun addFruit(fruit: Fruit): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_FRUIT_NAME, fruit.name)
            put(KEY_FRUIT_IMAGE, fruit.imageRes)
            put(KEY_FRUIT_DESC, fruit.description)
            put(KEY_FRUIT_SCIENTIFIC, fruit.scientificName)
            put(KEY_FRUIT_BENEFITS, fruit.healthBenefits)
        }
        val id = db.insert(TABLE_FRUITS, null, values)
        db.close()
        return id
    }

    fun getFruitCount(): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_FRUITS", null)
        val count = cursor.count
        cursor.close()
        return count
    }

    fun getAllFruits(): List<Fruit> {
        val fruitList = mutableListOf<Fruit>()
        val selectQuery = "SELECT * FROM $TABLE_FRUITS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val fruit = Fruit(
                    name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FRUIT_NAME)),
                    imageRes = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_FRUIT_IMAGE)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FRUIT_DESC)),
                    scientificName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FRUIT_SCIENTIFIC)),
                    healthBenefits = cursor.getString(cursor.getColumnIndexOrThrow(KEY_FRUIT_BENEFITS))
                )
                fruitList.add(fruit)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return fruitList
    }
}
