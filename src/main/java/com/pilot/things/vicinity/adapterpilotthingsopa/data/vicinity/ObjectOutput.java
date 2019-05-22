package com.pilot.things.vicinity.adapterpilotthingsopa.data.vicinity;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObjectOutput extends Output {

    private static final String _type = "object";

    @Builder(builderMethodName = "ObjectOutputBuilder")
    public ObjectOutput(List<Field> field,String description){
        super(_type,field,description);
    }
}
