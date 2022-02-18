package com.tutv.android.ui.login

interface LoginView {
    open fun showToast(message: String?)
    open fun setMailError(message: String?)
    open fun setPasswordError(message: String?)
    open fun setLoadingStatus(status: Boolean)
    open fun dismiss()
}