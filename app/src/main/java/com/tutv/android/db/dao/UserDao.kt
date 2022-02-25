package com.tutv.android.db.dao

import androidx.room.*
import com.tutv.android.domain.*

@Dao
interface UserDao {
    @get:Query("SELECT * FROM user LIMIT 1")
    val currentUser: User?

    @Insert
    fun insert(user: User)

    @Delete
    fun delete(user: User)
}