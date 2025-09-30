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
  age: number;
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
  // private async loadActiveAlerts() {
  //   try {
  //     const response = await this.assistService
  //       .getPendingAssists(10, 0)
  //       .toPromise();
  //     this.activeAlerts = response.data
  //       .filter(
  //         (a: any) => a.status === 'pending' || a.status === 'in_attendance'
  //       )
  //       .map((a: any) => this.mapApiToAlert(a));
  //   } catch (err) {
  //     console.error('Erro ao carregar chamados ativos:', err);
  //   }
  // }
  private async loadActiveAlerts() {
    try {
      const response: any = await this.assistService
        .getPendingAssists(10, 0)
        .toPromise();

      const alerts: Alert[] = [];

      for (const a of response.data) {
        if (a.status === 'pending' || a.status === 'in_attendance') {
          // Busca detalhes do assist para pegar idade
          const assistDetails: any = await this.assistService
            .getPendingAssistById(a.assistId)
            .toPromise();

          alerts.push({
            id: assistDetails.assistId,
            name: assistDetails.fullName,
            room: assistDetails.residentialUnit,
            time: this.formatDateTime(assistDetails.elapsedTime),
            severity: this.mapLevelFromApi(assistDetails.severityLevel),
            status: this.mapStatusFromApi(assistDetails.status),
            image: 'default.jpg',
            observations: assistDetails.observations ?? '',
            age: assistDetails.age,
          });
        }
      }

      this.activeAlerts = alerts; // atualiza a view corretamente
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
        name: a.patientName,
        room: a.patientUnit,
        time: this.formatDateTime(a.elapsedTime),
        severity: this.mapLevelFromApi(a.severityLevel),
        status: 'atendido',
        image: 'default.jpg',
        observations: a.description ?? '',
      }));

      console.log('Histórico carregado:', this.finishedAlerts);
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
      time: this.formatDateTime(a.elapsedTime),
      severity: this.mapLevelFromApi(a.severityLevel),
      status: this.mapStatusFromApi(a.status),
      image: 'default.jpg',
      observations: a.observations ?? '',
      age: a.age,
    };
  }
  private formatTime(time: string): string {
    const match = time.match(/(\d+) days (\d+):(\d+):(\d+)/);
    if (!match) return time;

    const days = parseInt(match[1], 10);
    const hours = parseInt(match[2], 10);
    const minutes = parseInt(match[3], 10);

    let result = '';
    if (days > 0) result += `${days}d `;
    if (hours > 0) result += `${hours}h `;
    if (minutes > 0) result += `${minutes}m`;

    return result.trim();
  }

  private formatDateTime(time: string): string {
    const match = time.match(/(\d+) days (\d+):(\d+):(\d+)/);
    if (!match) return time;

    const days = parseInt(match[1], 10);
    const hours = parseInt(match[2], 10);
    const minutes = parseInt(match[3], 10);

    let result = '';
    if (days > 0) result += `${days}d `;
    if (hours > 0) result += `${hours}h `;
    if (minutes > 0) result += `${minutes}m`;

    return result.trim();
  }

  private mapAlertToDetail(alert: Alert): ResidentAlertDetail {
    return {
      id: alert.id,
      fullName: alert.name,
      residentialUnit: alert.room,
      time: this.formatTime(alert.time),
      severity: alert.severity,
      status: alert.status,
      observations: alert.observations ?? '',
      imageData: alert.image,
      cpf: 'string',
      sex: 'string',
      birthDate: 'string',
      age: alert.age,
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
      time: this.formatTime(a.elapsedTime),
      severity: this.mapLevelFromApi(a.severityLevel),
      status,
      observations: a.details ?? '',
      imageData: image,
      cpf: 'string',
      sex: 'string',
      birthDate: '', // não precisa usar
      age: a.age,
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
  onAlertUpdated(updatedAlert: ResidentAlertDetail) {
    // atualiza na lista de ativos
    const idxActive = this.activeAlerts.findIndex(
      (a) => a.id === updatedAlert.id
    );
    if (idxActive > -1) {
      this.activeAlerts[idxActive] = {
        ...this.activeAlerts[idxActive],
        ...updatedAlert,
      };
      this.activeAlerts = [...this.activeAlerts]; // força change detection
    }

    // atualiza na lista de finalizados (histórico)
    const idxFinished = this.finishedAlerts.findIndex(
      (a) => a.id === updatedAlert.id
    );
    if (idxFinished > -1) {
      this.finishedAlerts[idxFinished] = {
        ...this.finishedAlerts[idxFinished],
        ...updatedAlert,
      };
      this.finishedAlerts = [...this.finishedAlerts]; // força change detection
    }
  }

  async onSearch(searchTerm: string) {
    console.log('Search term:', searchTerm);
  }
}
