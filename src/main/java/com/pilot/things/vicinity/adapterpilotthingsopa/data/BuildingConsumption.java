package com.pilot.things.vicinity.adapterpilotthingsopa.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties (ignoreUnknown = true)
@Getter
@Setter
public class BuildingConsumption {
    @JsonProperty (value = "chrono")
    String chrono;

    @JsonProperty (value = "name")
    String name;

    @JsonProperty (value = "value")
    String value;

    @JsonProperty (value = "quality")
    String quality;

    @JsonProperty (value = "description")
    String description;
}
