package com.tutv.android.datasource.dto;

public class SeriesFollowedResponseDTO {

    private int followers;
    private boolean loggedInUserFollows;

    public SeriesFollowedResponseDTO() {}

    public SeriesFollowedResponseDTO(int followers, boolean loggedInUserFollows) {
        this.followers = followers;
        this.loggedInUserFollows = loggedInUserFollows;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public boolean getLoggedInUserFollows() {
        return loggedInUserFollows;
    }

    public void setLoggedInUserFollows(Boolean loggedInUserFollows) {
        this.loggedInUserFollows = loggedInUserFollows;
    }
}
