import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTabsModule } from '@angular/material/tabs';

import { MainComponent } from '../../layout/main/main.component';
import { SearchInputComponent } from '../../components/search-input/search-input.component';
import { AlertResidentCardComponent } from '../../components/alert-resident-card/alert-resident-card.component';
import { AlertResidentDetailCardComponent } from '../../components/alert-resident-detail-card/alert-resident-detail-card.component';
import { ResidentAlertDetail } from '../../components/alert-resident-detail-card/alert-resident-detail-card.component';

import { ResidentService } from '../../controller/resident/resident.service';
import { AssistService } from '../../controller/assist/assist.service';

interface Alert {
  id: string;
  name: string;
  room: string;
  time: string;
  severity: 'medio' | 'critico';
  status: 'pendente' | 'em_atendimento' | 'atendido';
  image: string;
  observations?: string;
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
    AlertResidentCardComponent,
    MatTabsModule,
  ],
})
export class AlertsComponent implements OnInit {
  activeAlerts: Alert[] = [];
  finishedAlerts: Alert[] = [];

  selectedAlertDetail?: ResidentAlertDetail;
  selectedAlert?: Alert;

  totalActiveCalls: number = 0;
  selectedTabIndex: number = 0;

  private iconMap: Record<string, string> = {
    dashboard: 'fa-solid fa-chart-line',
    alerts: 'fa-solid fa-bell',
    reports: 'fa-solid fa-file-lines',
    settings: 'fa-solid fa-gear',
  };

  constructor(
    private residentService: ResidentService,
    private assistService: AssistService
  ) {}

  async ngOnInit(): Promise<void> {
    this.totalActiveCalls =
      await this.residentService.getTotalActiveResidentsCalls();
    await this.loadActiveAlerts();
    await this.loadFinishedAlerts();
  }

  // ================== Carregar Ativos ==================
  private async loadActiveAlerts() {
    try {
      const response = await this.assistService
        .getPendingAssists(10, 0)
        .toPromise();
      this.activeAlerts = response.data
        .filter(
          (a: any) => a.status === 'pending' || a.status === 'in_attendance'
        )
        .map((a: any) => this.mapApiToAlert(a));
    } catch (err) {
      console.error('Erro ao carregar chamados ativos:', err);
    }
  }

  // ================== Carregar Histórico ==================
  // private async loadFinishedAlerts() {
  //   try {
  //     const response = await this.assistService
  //       .getFinishedAssists(10, 0)
  //       .toPromise();
  //     this.finishedAlerts = response.data.map((a: any) => ({
  //       ...this.mapApiToAlert(a),
  //       status: 'atendido',
  //       observations: a.details ?? '',
  //     }));
  //   } catch (err) {
  //     console.error('Erro ao carregar histórico de alertas:', err);
  //   }
  // }
  private async loadFinishedAlerts() {
    try {
      const response: any = await this.assistService
        .getFinishedAssists(10, 0)
        .toPromise();

      this.finishedAlerts = response.data.map((a: any) => ({
        id: a.assistId,
        name: a.patientName, // atenção: mudou de fullName → patientName
        room: a.patientUnit, // fullName → patientUnit
        time: a.elapsedTime,
        severity: this.mapLevelFromApi(a.severityLevel),
        status: 'atendido', // histórico → sempre 'atendido'
        image: 'default.jpg',
        observations: a.description ?? '', // description
      }));

      console.log('Histórico carregado:', this.finishedAlerts); // para debugar
    } catch (err) {
      console.error('Erro ao carregar histórico de alertas:', err);
    }
  }

  // ================== Selecionar Alerta ==================
  async selectAlert(alert: Alert, isHistory: boolean) {
    this.selectedAlert = alert;

    if (isHistory) {
      try {
        const response: any = await this.assistService
          .getFinishedAssistById(alert.id)
          .toPromise();
        this.selectedAlertDetail = this.mapApiToAlertDetail(
          response,
          alert.image,
          'atendido'
        );
      } catch (err) {
        console.error('Erro ao carregar detalhes do histórico:', err);
      }
    } else {
      this.selectedAlertDetail = this.mapAlertToDetail(alert);
    }
  }

  // ================== Mapeamento ==================
  private mapApiToAlert(a: any): Alert {
    return {
      id: a.assistId,
      name: a.fullName,
      room: a.residentialUnit,
      time: a.elapsedTime,
      severity: this.mapLevelFromApi(a.severityLevel),
      status: this.mapStatusFromApi(a.status),
      image: 'default.jpg',
      observations: a.observations ?? '',
    };
  }

  private mapAlertToDetail(alert: Alert): ResidentAlertDetail {
    return {
      id: alert.id,
      fullName: alert.name,
      residentialUnit: alert.room,
      time: alert.time,
      severity: alert.severity,
      status: alert.status,
      observations: alert.observations ?? '',
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

  private mapApiToAlertDetail(
    a: any,
    image: string,
    status: 'pendente' | 'em_atendimento' | 'atendido'
  ): ResidentAlertDetail {
    return {
      id: a.assistId,
      fullName: a.fullName,
      residentialUnit: a.residentialUnit,
      time: a.elapsedTime,
      severity: this.mapLevelFromApi(a.severityLevel),
      status,
      observations: a.details ?? '',
      imageData: image,
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

  // ================== Utils ==================
  getIconClass(item: string): string {
    return this.iconMap[item] || 'fa-solid fa-circle-question';
  }

  private mapLevelFromApi(severity: string): 'medio' | 'critico' {
    return severity.toUpperCase() === 'EMERGENCY' ? 'critico' : 'medio';
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

  async onSearch(searchTerm: string) {
    console.log('Search term:', searchTerm);
  }
}
