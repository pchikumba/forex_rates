package com.wiremit.forex_rates.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class ERApiResponse {

    private String result;
    private String provider;
    private String documentation;
    @JsonProperty("terms_of_use")
    private String termsOfUse;

    @JsonProperty("time_last_update_unix")
    private long timeLastUpdateUnix;
    @JsonProperty("time_last_update_utc")
    private String timeLastUpdateUtc;

    @JsonProperty("time_next_update_unix")
    private long timeNextUpdateUnix;
    @JsonProperty("time_next_update_utc")
    private String timeNextUpdateUtc;

    @JsonProperty("time_eol_unix")
    private long timeEolUnix;

    @JsonProperty("base_code")
    private String baseCode;

    private Map<String, Double> rates;
}