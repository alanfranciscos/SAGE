import { Component, OnInit } from '@angular/core';
import { MainComponent } from "../../layout/main/main.component";
import { ResidentService } from '../../controller/resident/resident.service';
import { Router } from '@angular/router';
import { PanelSettingsComponent } from '../../components/panel-settings/panel-settings.component';

import { NgFor } from '@angular/common';
import { SummaryCardComponent } from '../../components/summary-card/summary-card.component';
import { DashboardResidentCardComponent } from '../../components/dashboard-resident-card/dashboard-resident-card.component';
import { DashboardResidentDetailsModalComponent } from '../../components/dashboard-resident-details-modal/dashboard-resident-details-modal.component';
import { SearchInputComponent } from '../../components/search-input/search-input.component';
import { ButtonComponent } from '../../components/button/button.component';
import { Resident } from '../../model/Resident';


@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [
    MainComponent,
    PanelSettingsComponent,
    MainComponent,
    ],
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.scss'
})
export class SettingsComponent {
  totalResidents: number = 0;
  constructor(
    private residentService: ResidentService,
    private router: Router
  ) {}
    async ngOnInit(): Promise<void> {
    try {
      this.totalResidents =
        await this.residentService.getTotalResidentsNumber();

    } catch (error) {}
  }

}
