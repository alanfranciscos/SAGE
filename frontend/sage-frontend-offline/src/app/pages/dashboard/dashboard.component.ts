import { NgFor } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ResidentService } from '../../controller/resident/resident.service';
import { MainComponent } from '../../layout/main/main.component';
import { SummaryCardComponent } from '../../components/summary-card/summary-card.component';
import { DashboardResidentCardComponent } from '../../components/dashboard-resident-card/dashboard-resident-card.component';
import { DashboardResidentDetailsModalComponent } from '../../components/dashboard-resident-details-modal/dashboard-resident-details-modal.component';
import { SearchInputComponent } from '../../components/search-input/search-input.component';
import { ButtonComponent } from '../../components/button/button.component';
import { Resident } from '../../model/Resident';

type ResidentStatus = 'normal' | 'critical' | 'warning';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    NgFor,
    MainComponent,
    SummaryCardComponent,
    DashboardResidentCardComponent,
    DashboardResidentDetailsModalComponent,
    SearchInputComponent,
    ButtonComponent,
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
  residents: Resident[] = [];

  constructor(private residentService: ResidentService) {}
  async ngOnInit(): Promise<void> {
    try {
      this.totalResidents =
        await this.residentService.getTotalResidentsNumber();
      // console.log('totalResidents', this.totalResidents);
      this.totalResolvedToday =
        await this.residentService.getTotalResolvedToday();
      // console.log('totalResolvedToday', this.totalResolvedToday);

      this.meanTime = await this.residentService.getMeanTime();
      // console.log('meanTime', this.meanTime);
      this.totalActiveCalls =
        await this.residentService.getTotalActiveResidentsCalls();
      this.residents = await this.residentService.getResidents(10, 0);
      console.log('residents', this.residents);

      // console.log('totalActiveCalls', this.totalActiveCalls);
    } catch (error) {}
  }

  // async getResidents() {
  //   try {
  //     this.residents = await this.residentService.getResidents(5, 0);
  //     console.log('residents', this.residents);
  //   } catch (error) {}
  // }
  getStatus(
    severity: 'emergency' | 'warning' | null
  ): 'normal' | 'warning' | 'critical' {
    if (severity === 'emergency') return 'critical';
    if (severity === 'warning') return 'warning';
    return 'normal';
  }
  getLastCallText(lastEndAt: string | null): string {
    if (!lastEndAt) return 'sem chamados';

    const now = new Date();
    const lastCall = new Date(lastEndAt);
    const diffMs = now.getTime() - lastCall.getTime();
    const diffDays = Math.floor(diffMs / (1000 * 60 * 60 * 24));

    const years = Math.floor(diffDays / 365);
    const remainingDaysAfterYears = diffDays % 365;
    const months = Math.floor(remainingDaysAfterYears / 30);
    const days = remainingDaysAfterYears % 30;

    let result = 'há ';
    if (years > 0) result += `${years} ano${years > 1 ? 's' : ''} `;
    if (months > 0) result += `${months} mes${months > 1 ? 'es' : ''} `;
    if (days > 0) result += `${days} dia${days > 1 ? 's' : ''}`;

    return result.trim();
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
