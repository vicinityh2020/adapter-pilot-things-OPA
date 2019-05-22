package com.pilot.things.vicinity.adapterpilotthingsopa.data.vicinity;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@JsonNaming(PropertyNamingStrategy.KebabCaseStrategy.class)
@Getter
@Setter
@Builder
public class Adapter {

    String adapterId;
    List<Thing> thingDescriptions;

}