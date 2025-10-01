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

        // Dias da semana na ordem correta
        const weekdaysOrder = [
          'Monday',
          'Tuesday',
          'Wednesday',
          'Thursday',
          'Friday',
          'Saturday',
          'Sunday',
        ];
        const weekdaysMap: Record<string, string> = {
          Monday: 'Segunda',
          Tuesday: 'Terça',
          Wednesday: 'Quarta',
          Thursday: 'Quinta',
          Friday: 'Sexta',
          Saturday: 'Sábado',
          Sunday: 'Domingo',
        };

        this.reports = weekdaysOrder
          .map((day) => ({
            label: weekdaysMap[day],
            value: data[day] || 0,
            color: 'blue',
          }))
          .sort((a, b) => b.value - a.value); // do maior para o menor
      } else if (this.title === 'Chamados por Hora do Dia') {
        const data = await this.reportsService.getCallsHourlyByDay();
        // data = { "0": 5, "1": 2, "2": 4, "3": 3, ... }

        const grouped: Record<string, number> = {};

        // Agrupar de 3 em 3 horas
        for (let i = 0; i < 24; i += 3) {
          const rangeLabel = `${i}-${i + 2}h`;
          grouped[rangeLabel] =
            (data[i] || 0) + (data[i + 1] || 0) + (data[i + 2] || 0);
        }

        // Ordenar do maior para o menor
        const sortedEntries = Object.entries(grouped).sort(
          (a, b) => b[1] - a[1]
        );

        this.reports = sortedEntries.map(([range, value]) => ({
          label: range,
          value,
          color: 'emerald',
        }));
      }
    } catch (error) {
      console.error('Erro ao carregar dados do gráfico:', error);
    }
  }
  get maxValue(): number {
    return this.reports.length
      ? Math.max(...this.reports.map((r) => r.value))
      : 1;
  }

  getPercentage(value: number): number {
    // Proteção contra divisão por zero
    return this.maxValue ? (value / this.maxValue) * 100 : 0;
  }
}
