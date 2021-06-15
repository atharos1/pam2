package com.tutv.android.ui.search;

import com.tutv.android.domain.Genre;
import com.tutv.android.domain.Network;

import java.util.List;

public interface SearchView {
    void setSearchQuery(String searchQuery, Integer genre, Integer network);
    void setFilters(List<Genre> genres, List<Network> networks);
    void showFilterError();
}
