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
  meanTime: string = '';

  selectedResidentId: string | null = null;
  showModal = false;
  // residents: Resident[] = [];

  residents: Resident[] = Array.from({ length: 12 }, (_, i) => {
    const id = (i + 1).toString();
    const residentName = `Residente ${i + 1}`;
    const houseNumber = (100 + i).toString();
    const statusOptions: ResidentStatus[] = ['normal', 'critical', 'warning'];
    const status = statusOptions[i % statusOptions.length];
    const hour = String(8 + (i % 12)).padStart(2, '0');
    const minute = String((i * 7) % 60).padStart(2, '0');
    const lastCallTime = `${hour}:${minute}`;
    const lastCallDateTime = `2025-09-18T${lastCallTime}:00`;
    const totalCallsLast24Hours = Math.floor(Math.random() * 6).toString();

    return {
      id,
      residentImage: 'https://via.placeholder.com/100',
      residentName,
      houseNumber,
      lastCallDateTime,
      status,
      lastCallTime,
      totalCallsLast24Hours,
    };
  });

  constructor(private residentService: ResidentService) {}
  async ngOnInit(): Promise<void> {
    try {
      this.totalResidents =
        await this.residentService.getTotalResidentsNumber();
      console.log('totalResidents', this.totalResidents);
      this.totalResolvedToday =
        await this.residentService.getTotalResolvedToday();
      console.log('totalResolvedToday', this.totalResolvedToday);

      this.meanTime = await this.residentService.getMeanTime();
      console.log('meanTime', this.meanTime);
      this.totalActiveCalls =
        await this.residentService.getTotalActiveResidentsCalls();
      console.log('totalActiveCalls', this.totalActiveCalls);
    } catch (error) {}
    this.residents = this.residents.sort((a, b) => {
      const priority: Record<ResidentStatus, number> = {
        critical: 1,
        warning: 2,
        normal: 3,
      };
      return priority[a.status] - priority[b.status];
    });
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
