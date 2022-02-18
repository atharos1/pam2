package com.tutv.android.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tutv.android.db.dao.SeriesDao
import com.tutv.android.db.dao.UserDao
import com.tutv.android.domain.*
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

@Database(entities = [User::class, Series::class, Season::class, Episode::class, SeriesListAndSeriesMap::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao?
    abstract fun seriesDao(): SeriesDao?

    companion object {
        private val NAME: String? = "app_db"
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context?): AppDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase::class.java, NAME)
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return instance
        }
    }
}