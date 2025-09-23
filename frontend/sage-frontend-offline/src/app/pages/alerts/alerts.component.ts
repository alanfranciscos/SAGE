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

interface Alert {
  id: string;
  name: string;
  room: string;
  time: string;
  severity: 'Crítico' | 'Alto' | 'Médio';
  status: 'Pendente' | 'Atendido';
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
    MatTabsModule,
    AlertResidentCardComponent,
  ],
})
export class AlertsComponent implements OnInit {
  constructor(private residentService: ResidentService) {}

  async ngOnInit(): Promise<void> {
    this.totalActiveCalls =
      await this.residentService.getTotalActiveResidentsCalls();
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
  alerts: Alert[] = [
    {
      id: '1',
      name: 'Ana Costa',
      room: 'Quarto 150',
      time: 'há 2m2s',
      severity: 'Crítico',
      status: 'Pendente',
      image: 'ana.jpg',
      observations: 'Paciente atendido pelo Dr. Paulo',
    },
    {
      id: '2',
      name: 'Maria Silva',
      room: 'Quarto 101',
      time: 'há 5m2s',
      severity: 'Alto',
      status: 'Pendente',
      image: 'maria.jpg',
    },
    {
      id: '3',
      name: 'João Santos',
      room: 'Quarto 205',
      time: 'há 10m',
      severity: 'Médio',
      status: 'Pendente',
      image: 'joao.jpg',
    },
    {
      id: '3',
      name: 'João Santos',
      room: 'Quarto 205',
      time: 'há 10m',
      severity: 'Médio',
      status: 'Atendido',
      image: 'joao.jpg',
      observations: 'Paciente atendido pelo Dr. Paulo',
    },
    {
      id: '4',
      name: 'Maria Oliveira',
      room: 'Quarto 202',
      time: 'há 15m',
      severity: 'Crítico',
      status: 'Atendido',
      image: 'maria.jpg',
      observations: 'Paciente atendido pelo Dr. Paulo',
    },
  ];

  selectedAlert?: Alert; // guarda o card clicado

  selectAlert(alert: Alert) {
    this.selectedAlert = alert; // agora a classe active funciona
    this.selectedAlertDetail = {
      id: alert.id,
      fullName: alert.name,
      residentialUnit: alert.room,
      imageData: alert.image,
      birthDate: '1970-01-01',
      level: alert.severity.toLowerCase() as 'normal' | 'medio' | 'critico',
      status: alert.status.toLowerCase() as
        | 'pendente'
        | 'em_atendimento'
        | 'atendido',
      time: alert.time,
      observations: '',
      cpf: '000.000.000-00',
      sex: 'M',
      controlNumber: 0,
    };
  }

  async onSearch(searchTerm: string) {
    console.log('Search term:', searchTerm);
  }

  getIconClass(item: string): string {
    return this.iconMap[item] || 'fa-solid fa-circle-question';
  }
  get activeAlerts() {
    return this.alerts.filter(
      (a) =>
        a.status.toLowerCase() === 'pendente' ||
        a.status.toLowerCase() === 'em_atendimento'
    );
  }

  get finishedAlerts() {
    return this.alerts.filter(
      (a) =>
        a.status.toLowerCase() === 'finalizado' ||
        a.status.toLowerCase() === 'atendido'
    );
  }
  mapLevel(severity: string): 'normal' | 'medio' | 'critico' {
    switch (severity.toLowerCase()) {
      case 'crítico':
        return 'critico';
      case 'alto':
        return 'medio';
      case 'médio':
        return 'medio';
      default:
        return 'normal';
    }
  }

  mapStatus(status: string): 'pendente' | 'em_atendimento' | 'atendido' {
    switch (status.toLowerCase()) {
      case 'pendente':
        return 'pendente';
      case 'atendido':
        return 'atendido';
      default:
        return 'em_atendimento';
    }
  }
}
