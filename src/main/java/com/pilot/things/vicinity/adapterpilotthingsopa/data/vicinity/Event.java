package com.pilot.things.vicinity.adapterpilotthingsopa.data.vicinity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Event {

    String eid;
    String monitors;
    Output output;
}
