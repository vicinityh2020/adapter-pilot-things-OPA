package com.pilot.things.vicinity.adapterpilotthingsopa.data.vicinity.schema;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StringSchema extends Schema {

    private static final String _type = "string";

    @Builder
    public StringSchema() {
        super(_type);

    }
}
