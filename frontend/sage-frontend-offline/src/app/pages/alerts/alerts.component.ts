import { Component, OnInit } from '@angular/core';
import { MainComponent } from '../../layout/main/main.component';
import { SearchInputComponent } from '../../components/search-input/search-input.component';
import { ButtonComponent } from '../../components/button/button.component';
import { CommonModule } from '@angular/common';
import { ResidentService } from '../../controller/resident/resident.service';
import { AlertResidentCardComponent } from '../../components/alert-resident-card/alert-resident-card.component';

@Component({
  selector: 'app-alerts',
  standalone: true,
  imports: [
    MainComponent,
    SearchInputComponent,
    CommonModule,
    AlertResidentCardComponent,
  ],
  templateUrl: './alerts.component.html',
  styleUrl: './alerts.component.scss',
})
export class AlertsComponent implements OnInit {
  constructor(private residentService: ResidentService) {}
  async ngOnInit(): Promise<void> {
    this.totalActiveCalls =
      await this.residentService.getTotalActiveResidentsCalls();
  }
  totalActiveCalls: number = 0;
  private iconMap: Record<string, string> = {
    dashboard: 'fa-solid fa-chart-line',
    alerts: 'fa-solid fa-bell',
    reports: 'fa-solid fa-file-lines',
    settings: 'fa-solid fa-gear',
  };
  async onSearch(searchTerm: string) {
    console.log('Search term:', searchTerm);
  }

  getIconClass(item: string): string {
    return this.iconMap[item] || 'fa-solid fa-circle-question';
  }
}
