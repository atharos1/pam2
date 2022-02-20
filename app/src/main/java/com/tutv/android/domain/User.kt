package com.tutv.android.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class User {
    @PrimaryKey
    var id = 0
    var mail: String? = null
    var isAdmin = false
    var avatar: String? = null
    var isBanned = false
    var userName: String? = null
}