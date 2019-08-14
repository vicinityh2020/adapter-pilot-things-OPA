package com.pilot.things.vicinity.adapterpilotthingsopa.data.vicinity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReadLink {
    private static final String _href = "/objects/{oid}/properties/{pid}";
    String href;
    Output output;

    @Builder
    public ReadLink(Output output){
        this.href = _href;
        this.output = output;
    }
}
