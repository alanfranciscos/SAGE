import { Component, OnInit } from '@angular/core';
import { MainComponent } from '../../layout/main/main.component';
import { SearchInputComponent } from '../../components/search-input/search-input.component';
import { ButtonComponent } from '../../components/button/button.component';
import { CommonModule } from '@angular/common';
import { ResidentService } from '../../controller/resident/resident.service';
import { AlertResidentCardComponent } from '../../components/alert-resident-card/alert-resident-card.component';
import { AlertResidentDetailCardComponent } from '../../components/alert-resident-detail-card/alert-resident-detail-card.component';
import { ResidentAlertDetail } from '../../components/alert-resident-detail-card/alert-resident-detail-card.component';
import { MatTabsModule } from '@angular/material/tabs';
import { AssistService } from '../../controller/alert/alert.service';

interface Alert {
  id: string;
  name: string;
  room: string;
  time: string;
  severity: 'medio' | 'critico';
  status: 'pendente' | 'em_atendimento';
  image: string;
}

@Component({
  selector: 'app-alerts',
  templateUrl: './alerts.component.html',
  styleUrls: ['./alerts.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    MainComponent,
    SearchInputComponent,
    AlertResidentDetailCardComponent,
    MatTabsModule,
    AlertResidentCardComponent,
  ],
})
export class AlertsComponent implements OnInit {
  activeAlerts: Alert[] = [];
  finishedAlerts: Alert[] = [];
  constructor(
    private residentService: ResidentService,
    private assistService: AssistService
  ) {}

  async ngOnInit(): Promise<void> {
    this.totalActiveCalls =
      await this.residentService.getTotalActiveResidentsCalls();
    await this.loadAlerts();
  }
  private async loadAlerts() {
    try {
      const response = await this.assistService.getPendingAssists(10, 0);

      this.activeAlerts = response.data
        .filter((a) => a.status === 'pending' || a.status === 'in_attendance')
        .map((a) => ({
          id: a.assistId,
          name: a.fullName,
          room: a.residentialUnit,
          time: a.elapsedTime,
          severity: this.mapLevelFromApi(a.severityLevel),
          status: this.mapStatusFromApi(a.status),
          image: 'default.jpg',
          observations: a.observations ?? '',
        }));

      this.finishedAlerts = response.data
        .filter((a) => a.status === 'attended' || a.status === 'completed')
        .map((a) => ({
          id: a.assistId,
          name: a.fullName,
          room: a.residentialUnit,
          time: a.elapsedTime,
          severity: this.mapLevelFromApi(a.severityLevel),
          status: this.mapStatusFromApi(a.status),
          image: 'default.jpg',
          observations: a.observations ?? '',
        }));
    } catch (err) {
      console.error('Erro ao carregar alertas:', err);
    }
  }
  totalActiveCalls: number = 0;
  selectedTabIndex: number = 0;
  private iconMap: Record<string, string> = {
    dashboard: 'fa-solid fa-chart-line',
    alerts: 'fa-solid fa-bell',
    reports: 'fa-solid fa-file-lines',
    settings: 'fa-solid fa-gear',
  };
  selectedAlertDetail?: ResidentAlertDetail;

  selectedAlert?: Alert; // guarda o card clicado

  selectAlert(alert: Alert) {
    this.selectedAlert = alert; // agora a classe active funciona
    this.selectedAlertDetail = {
      id: alert.id,
      fullName: alert.name,
      residentialUnit: alert.room,
      time: alert.time,
      severity: alert.severity,
      status: alert.status,
      imageData: alert.image,

      cpf: 'string',
      sex: 'string',
      birthDate: 'string',

      active: true,
      controlId: 0,
      createdAt: 'string',
      updatedAt: 'string',
      emergencyFullName: 'string',
      emergencyPhone: 'string',
      emergencyRelationship: 'string',
    };
  }

  async onSearch(searchTerm: string) {
    console.log('Search term:', searchTerm);
  }

  getIconClass(item: string): string {
    return this.iconMap[item] || 'fa-solid fa-circle-question';
  }

  mapLevel(severity: string): 'normal' | 'medio' | 'critico' {
    if (severity.toLowerCase() === 'emergency') return 'critico';
    if (severity.toLowerCase() === 'warning') return 'medio';
    return 'normal';
  }

  mapStatus(status: string): 'pendente' | 'em_atendimento' | 'atendido' {
    if (status.toLowerCase() === 'in_attendance') return 'em_atendimento';
    if (status.toLowerCase() === 'pending') return 'pendente';
    if (status.toLowerCase() === 'attended') return 'atendido';
    return 'pendente';
  }
  private mapStatusFromApi(status: string): 'pendente' | 'em_atendimento' {
    switch (status.toLowerCase()) {
      case 'pending':
        return 'pendente';
      case 'in_attendance':
        return 'em_atendimento';
      default:
        return 'pendente';
    }
  }
  private mapLevelFromApi(severity: string): 'medio' | 'critico' {
    return severity.toUpperCase() === 'EMERGENCY' ? 'critico' : 'medio';
  }
}
