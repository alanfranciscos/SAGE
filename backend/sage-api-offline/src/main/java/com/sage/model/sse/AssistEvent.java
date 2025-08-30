package com.sage.model.sse;

import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssistEvent {

    private String operation;

    public static AssistEvent parseJson(JSONObject jsonObject) {
        String operation = jsonObject.getString("operation");
        AssistEvent assitEvent = new AssistEvent(operation);

        return assitEvent;
    }

}
