package com.sage.controller.v1.resident;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/resident")
public class ResidentController {

    @PostMapping
    public String createResident(@RequestBody String resident) {
        return "TODO: Implement resident creation logic";
    }

    @PatchMapping
    public String updateResident(@RequestBody String resident) {
        return "TODO: Implement resident update logic";
    }

    @GetMapping
    public String listResidents() {
        return "TODO: Implement resident listing logic";
    }

    @GetMapping("/details")
    public String getResidentDetails() {
        return "TODO: Implement resident details logic";
    }

}
