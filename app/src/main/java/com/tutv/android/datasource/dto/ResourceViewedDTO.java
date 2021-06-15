package com.tutv.android.datasource.dto;

public class ResourceViewedDTO {

    private boolean viewedByUser;

    public ResourceViewedDTO() {}

    public ResourceViewedDTO(boolean userViewed) {
        this.viewedByUser = userViewed;
    }

    public boolean isViewedByUser() {
        return viewedByUser;
    }

    public void setViewedByUser(boolean viewedByUser) {
        this.viewedByUser = viewedByUser;
    }

}
