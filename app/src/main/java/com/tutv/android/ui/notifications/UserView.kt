package com.tutv.android.ui.notifications

interface UserView {
    fun showUserAvatar(avatarUrl: String?)
    fun showUserName(userName: String?)
    fun showError(error: String?)
}