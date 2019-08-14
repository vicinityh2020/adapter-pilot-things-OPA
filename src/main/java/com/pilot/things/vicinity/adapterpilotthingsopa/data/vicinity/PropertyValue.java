package com.pilot.things.vicinity.adapterpilotthingsopa.data.vicinity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyValue {

    @JsonProperty (value = "property-value")
    Object value;

    String name;

    @JsonProperty (value = "value-read-time")
    Long timestamp;
}
