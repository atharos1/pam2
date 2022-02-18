package com.tutv.android.datasource.dto

import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

class ResourceViewedDTO {
    private var viewedByUser = false

    constructor() {}
    constructor(userViewed: Boolean) {
        viewedByUser = userViewed
    }

    fun isViewedByUser(): Boolean {
        return viewedByUser
    }

    fun setViewedByUser(viewedByUser: Boolean) {
        this.viewedByUser = viewedByUser
    }
}