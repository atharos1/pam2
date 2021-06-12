package com.tutv.android.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.tutv.android.domain.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user LIMIT 1")
    User getCurrentUser();

    @Insert
    void insert(User user);

    @Delete
    void delete(User user);
}
