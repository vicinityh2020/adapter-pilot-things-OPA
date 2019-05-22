package com.pilot.things.vicinity.adapterpilotthingsopa.data.vicinity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Output {
    String type;

    List<Field> field;

    String description;
}
