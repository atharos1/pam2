package com.tutv.android.db.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tutv.android.domain.Series;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class SeriesListToStringConverter {
    static Gson gson = new Gson();

    @TypeConverter
    public static List<Series> stringToSeriesList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Series>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String seriesListToString(List<Series> seriesList) {
        return gson.toJson(seriesList);
    }
}
