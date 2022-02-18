package com.tutv.android.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class User {
    @PrimaryKey var id = 0
    var mail: String? = null
    var admin = false
    var avatar: String? = null
    var banned = false
    var userName: String? = null
}