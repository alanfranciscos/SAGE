package com.sage.controller.v1.caregiver;

import com.sage.services.caregiver.CaregiverServiceImpl;

public class CaregiverController {

    private final CaregiverServiceImpl caregiverService;

    public CaregiverController(
            CaregiverServiceImpl caregiverService
    ) {
        this.caregiverService = caregiverService;
    }

    
}
