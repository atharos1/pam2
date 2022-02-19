package com.tutv.android.ui.login

interface LoginView {
    fun showToast(message: String?)
    fun setMailError(message: String?)
    fun setPasswordError(message: String?)
    fun setLoadingStatus(status: Boolean)
    fun dismiss()
}