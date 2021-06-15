package com.tutv.android.datasource.dto;

public class SeriesFollowedResponseDTO {

    private int followers;
    private Boolean loggedInUserFollows;

    public SeriesFollowedResponseDTO(int followers, Boolean loggedInUserFollows) {
        this.followers = followers;
        this.loggedInUserFollows = loggedInUserFollows;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public Boolean getLoggedInUserFollows() {
        return loggedInUserFollows;
    }

    public void setLoggedInUserFollows(Boolean loggedInUserFollows) {
        this.loggedInUserFollows = loggedInUserFollows;
    }
}
