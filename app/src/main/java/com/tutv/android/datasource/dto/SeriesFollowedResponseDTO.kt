package com.tutv.android.datasource.dto

import org.mockito.ArgumentMatchers
import org.mockito.InOrder
import org.mockito.Mockito

class SeriesFollowedResponseDTO {
    private var followers = 0
    private var loggedInUserFollows = false

    constructor() {}
    constructor(followers: Int, loggedInUserFollows: Boolean) {
        this.followers = followers
        this.loggedInUserFollows = loggedInUserFollows
    }

    fun getFollowers(): Int {
        return followers
    }

    fun setFollowers(followers: Int) {
        this.followers = followers
    }

    fun getLoggedInUserFollows(): Boolean {
        return loggedInUserFollows
    }

    fun setLoggedInUserFollows(loggedInUserFollows: Boolean?) {
        this.loggedInUserFollows = loggedInUserFollows
    }
}