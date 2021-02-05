package com.bajie.money.model.loacal

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bajie.money.model.dao.CategoryDao
import com.bajie.money.model.dao.MovieDao
import com.bajie.money.model.data.Category
import com.bajie.money.model.data.MovieSubject

/**

 * bajie on 2021/1/14 17:15

 */
@Database(entities = [MovieSubject::class, Category::class], version=1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDao;
    abstract fun categoryDao(): CategoryDao;

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null;
        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(
                        context
                    ).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "money.db").build();
    }
}