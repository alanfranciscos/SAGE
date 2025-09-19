import { NgFor } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ResidentService } from '../../controller/resident/resident.service';
import { MainComponent } from '../../layout/main/main.component';
import { SummaryCardComponent } from '../../components/summary-card/summary-card.component';
import { DashboardResidentCardComponent } from '../../components/dashboard-resident-card/dashboard-resident-card.component';
import { DashboardResidentDetailsModalComponent } from '../../components/dashboard-resident-details-modal/dashboard-resident-details-modal.component';

type ResidentStatus = 'normal' | 'critical' | 'warning';
interface Resident {
  id: string;
  residentImage: string;
  residentName: string;
  houseNumber: string;
  lastCallDateTime: string;
  status: ResidentStatus;
  lastCallTime: string;
  totalCallsLast24Hours: string;
}
@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    NgFor,
    MainComponent,
    SummaryCardComponent,
    DashboardResidentCardComponent,
    DashboardResidentDetailsModalComponent,
  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  totalResidents: number = 0;
  totalResolvedToday: number = 0;
  totalActiveCalls: number = 0;
  meanTime: number = 0;
  selectedResidentId: string | null = null;
  showModal = false;
  // residents: Resident[] = [];
  residents: Resident[] = [
    {
      id: '1',
      residentImage: 'https://via.placeholder.com/100', // imagem genérica
      residentName: 'Maria Silva',
      houseNumber: '101',
      lastCallDateTime: '2025-09-18T14:30:00',
      status: 'normal',
      lastCallTime: '14:30',
      totalCallsLast24Hours: '2',
    },
    {
      id: '2',
      residentImage: 'https://via.placeholder.com/100',
      residentName: 'João Pereira',
      houseNumber: '102',
      lastCallDateTime: '2025-09-18T13:15:00',
      status: 'critical',
      lastCallTime: '13:15',
      totalCallsLast24Hours: '5',
    },
  ];

  constructor(private residentService: ResidentService) {}
  async ngOnInit(): Promise<void> {
    try {
      this.totalResidents =
        await this.residentService.getTotalResidentsNumber();
      this.totalResolvedToday =
        await this.residentService.getTotalResolvedToday();
      this.totalActiveCalls =
        await this.residentService.getTotalActiveResidentsCalls();
      this.meanTime = await this.residentService.getMeanTime();
    } catch (error) {}
    console.log(this.totalResidents);
  }

  onOpenDetails(residentId: string) {
    this.selectedResidentId =
      this.residents.find((resident) => resident.id === residentId)?.id || null;
    this.showModal = true;
  }

  onCloseModal() {
    this.showModal = false;
    this.selectedResidentId = null;
  }
}
