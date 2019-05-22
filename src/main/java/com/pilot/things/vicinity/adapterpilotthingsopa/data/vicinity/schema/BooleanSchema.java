package com.pilot.things.vicinity.adapterpilotthingsopa.data.vicinity.schema;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BooleanSchema extends Schema {
    private static final String _type = "string";

    public BooleanSchema(){
        super(_type);
    }
}
