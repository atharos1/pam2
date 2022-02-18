package com.tutv.android.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.tutv.android.domain.User
import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

@Dao
interface UserDao {
    @Query("SELECT * FROM user LIMIT 1")
    open fun getCurrentUser(): User?

    @Insert
    open fun insert(user: User?)

    @Delete
    open fun delete(user: User?)
}