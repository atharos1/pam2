package com.tutv.android.ui.notifications

import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

interface UserView {
    open fun showUserAvatar(avatarUrl: String?)
    open fun showUserName(userName: String?)
    open fun showError(error: String?)
}