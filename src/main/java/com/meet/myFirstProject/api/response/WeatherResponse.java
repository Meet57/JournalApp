package com.meet.myFirstProject.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WeatherResponse
{
    @JsonProperty("current")
    private Current current;

    @Getter
    @Setter
    public static class Current
    {
        @JsonProperty("observation_time")
        private String observationTime;

        @JsonProperty("temperature")
        private int temperature;

        @JsonProperty("weather_code")
        private int weatherCode;

        @JsonProperty("weather_icons")
        private List<String> weatherIcons;

        @JsonProperty("weather_descriptions")
        private List<String> weatherDescriptions;

        @JsonProperty("wind_speed")
        private int windSpeed;

        @JsonProperty("wind_degree")
        private int windDegree;

        @JsonProperty("wind_dir")
        private String windDir;

        @JsonProperty("pressure")
        private int pressure;

        @JsonProperty("precip")
        private int precip;

        @JsonProperty("humidity")
        private int humidity;

        @JsonProperty("cloudcover")
        private int cloudCover;

        @JsonProperty("feelslike")
        private int feelsLike;

        @JsonProperty("uv_index")
        private int uvIndex;

        @JsonProperty("visibility")
        private int visibility;

        @JsonProperty("is_day")
        private String isDay;
    }
}
