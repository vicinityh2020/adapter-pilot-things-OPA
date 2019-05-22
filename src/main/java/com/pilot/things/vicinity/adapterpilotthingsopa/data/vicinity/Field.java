package com.pilot.things.vicinity.adapterpilotthingsopa.data.vicinity;

import com.pilot.things.vicinity.adapterpilotthingsopa.data.vicinity.schema.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Field {
    String name;
    Schema schema;
}
