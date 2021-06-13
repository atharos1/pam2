package com.tutv.android.datasource.dto;

public class ResourceFollowedDTO {
    private Boolean loggedInUserFollows;

    public ResourceFollowedDTO(Boolean loggedInUserFollows) {
        this.loggedInUserFollows = loggedInUserFollows;
    }

    public Boolean getLoggedInUserFollows() {
        return loggedInUserFollows;
    }

    public void setLoggedInUserFollows(Boolean loggedInUserFollows) {
        this.loggedInUserFollows = loggedInUserFollows;
    }
}
