package com.sage.model.sse.event;

import org.json.JSONObject;

import com.sage.model.CaregiverAssignmentResident;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AssignmentChangeEvent {

    private String operation;
    private String table;
    private CaregiverAssignmentResident data;

    public AssignmentChangeEvent parseJson(JSONObject jsonObject) {
        this.operation = jsonObject.getString("operation");
        this.table = jsonObject.getString("table");

        this.data = new CaregiverAssignmentResident();
        this.data.parseJson(jsonObject.getJSONObject("data"));

        return this;
    }
}
