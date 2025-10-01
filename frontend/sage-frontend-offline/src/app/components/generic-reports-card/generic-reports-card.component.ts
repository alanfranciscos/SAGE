import { Component, Input, OnInit } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { NgFor } from '@angular/common';
import { ReportsService } from '../../controller/reports/reports.service';

interface Report {
  label: string;
  value: number;
  color: string;
}

@Component({
  selector: 'app-generic-reports-card',
  standalone: true,
  imports: [MatCardModule, NgFor],
  templateUrl: './generic-reports-card.component.html',
  styleUrls: ['./generic-reports-card.component.scss'],
})
export class GenericReportsCardComponent implements OnInit {
  @Input() title: string = 'Relatório';

  reports: Report[] = [];

  constructor(private reportsService: ReportsService) {}

  async ngOnInit() {
    try {
      if (this.title === 'Chamados por Dia da Semana') {
        const data = await this.reportsService.getCallsByWeekday();

        // Mapa para traduzir dias
        const weekdaysMap: Record<string, string> = {
          Monday: 'Segunda',
          Tuesday: 'Terça',
          Wednesday: 'Quarta',
          Thursday: 'Quinta',
          Friday: 'Sexta',
          Saturday: 'Sábado',
          Sunday: 'Domingo',
        };

        this.reports = Object.entries(data).map(([day, value]) => ({
          label: weekdaysMap[day] || day, // traduzir dia
          value,
          color: 'blue', // você pode variar cores se quiser
        }));
      } else if (this.title === 'Chamados por Hora do Dia') {
        const data = await this.reportsService.getCallsHourlyByDay();
        this.reports = Object.entries(data).map(([hour, value]) => ({
          label: `${hour}h`,
          value,
          color: 'emerald',
        }));
      }
    } catch (error) {
      console.error('Erro ao carregar dados do gráfico:', error);
    }
  }
}
