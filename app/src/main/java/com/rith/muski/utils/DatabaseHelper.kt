package com.rith.muski.utils

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.File

class DatabaseHelper private constructor(context: Context) : SQLiteOpenHelper(
    context,
    getDatabasePath(context),
    null,
    DB_VERSION
) {

    companion object {
        private const val DB_NAME = "waterapp.db"
        private const val DB_VERSION = 1

        private fun getDatabasePath(context: Context): String {
            val folder = File(context.getExternalFilesDir(null), "WaterAppData")
            if (!folder.exists()) {
                folder.mkdirs()
            }
            Log.d("DB_PATH",File(folder, DB_NAME).absolutePath)
            return File(folder, DB_NAME).absolutePath
        }

        // Singleton instance
        @Volatile
        private var instance: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper {
            return instance ?: synchronized(this) {
                instance ?: DatabaseHelper(context.applicationContext).also { instance = it }
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS user (
                u_id INTEGER PRIMARY KEY,
                name TEXT NOT NULL,
                email TEXT NOT NULL,
                phone TEXT NOT NULL,
                address TEXT NOT NULL
            );
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS water (
                w_id INTEGER PRIMARY KEY,
                w_type TEXT NOT NULL,
                w_rate REAL NOT NULL,
                w_quantity TEXT NOT NULL
            );
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS orders (
                o_id INTEGER PRIMARY KEY,
                u_id INTEGER,
                o_total REAL NOT NULL,
                o_payment TEXT NOT NULL,
                o_date TEXT NOT NULL
            );
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS order_items (
                o_id INTEGER,
                w_id INTEGER
            );
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS payment (
                p_id INTEGER PRIMARY KEY,
                p_date TEXT NOT NULL,
                o_id INTEGER,
                p_mode TEXT NOT NULL,
                p_paid_amount REAL NOT NULL,
                p_due_amount REAL NOT NULL
            );
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS cost (
                c_id INTEGER PRIMARY KEY,
                w_id INTEGER,
                c_amount REAL NOT NULL
            );
        """.trimIndent())
    }
    fun isWaterExists(name: String, size: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT COUNT(*) FROM water WHERE w_type = ? AND w_quantity = ?",
            arrayOf(name, size)
        )
        var exists = false
        if (cursor.moveToFirst()) {
            exists = cursor.getInt(0) > 0
        }
        cursor.close()
        return exists
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // TODO: Implement when upgrading schema
    }

    fun executeQuery(query: String) {
        writableDatabase.execSQL(query)
    }

    fun fetchQuery(query: String): Cursor {
        return readableDatabase.rawQuery(query, null)
    }
}