import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MainComponent } from '../../layout/main/main.component';
import { HeaderCardReportComponent } from '../../components/header-card-report/header-card-report.component';
import { ReportsService } from '../../controller/reports/reports.service';
import { GenericReportsCardComponent } from '../../components/generic-reports-card/generic-reports-card.component';
import { GenericReportsRankingCardComponent } from '../../components/generic-reports-ranking-card/generic-reports-ranking-card.component';

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [
    MainComponent,
    HeaderCardReportComponent,
    CommonModule,
    GenericReportsCardComponent,
    GenericReportsRankingCardComponent,
  ],
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.scss'],
})
export class ReportsComponent implements OnInit {
  topCaregivers: any[] = [];
  topResidents: any[] = [];

  topCaregiversForTemplate: any[] = [];
  topResidentsForTemplate: any[] = [];

  cards = [
    {
      title: 'Total de Chamados',
      value: '...',
      delta: '+12% vs mês anterior',
      icon: 'notifications',
      iconBgColor: 'bg-blue-100',
      iconColor: 'text-blue-600',
      valueClass: 'value-default',
    },
    {
      title: 'Tempo Médio de Resposta',
      value: '...',
      delta: '',
      icon: 'timer',
      iconBgColor: 'bg-yellow-100',
      iconColor: 'text-yellow-600',
      valueClass: 'value-default',
    },
    {
      title: 'Tempo Médio de Resolução',
      value: '...',
      delta: '',
      icon: 'schedule',
      iconBgColor: 'bg-purple-100',
      iconColor: 'text-purple-600',
      valueClass: 'value-default',
    },
    {
      title: 'Taxa de Chamados Críticos',
      value: '...',
      delta: '',
      icon: 'priority_high',
      iconBgColor: 'bg-red-100',
      iconColor: 'text-red-600',
      valueClass: 'value-default',
    },
  ];

  constructor(private reportsService: ReportsService) {}
  formatTimeString(time: string): string {
    if (!time) return '---';

    // Divide a string entre "days" e o restante
    const parts = time.split(' days ');
    if (parts.length === 2) {
      const days = parts[0];
      const timePart = parts[1].split('.')[0]; // remove milissegundos
      const hhmm = timePart.slice(0, 5); // pega só HH:MM
      return `${days} dias ${hhmm}`;
    } else {
      // se não tem "days", pega HH:MM
      return time.split('.')[0].slice(0, 5);
    }
  }

  async ngOnInit() {
    try {
      // Cards
      this.cards[0].value = (
        await this.reportsService.getTotalAssists()
      ).toString();
      this.cards[1].value = await this.reportsService.getAverageResponseTime();
      this.cards[2].value =
        await this.reportsService.getAverageResolutionTime();
      const criticalRate = await this.reportsService.getCriticalAssistsRate();
      this.cards[3].value = `${criticalRate}%`;
      this.cards[3].valueClass = criticalRate > 0 ? 'value-red' : 'value-green';

      // Rankings
      this.topCaregivers =
        await this.reportsService.getTopFiveCaregiverPerformance();
      this.topResidents = await this.reportsService.getTopFiveCallResidents();

      // Transformar para o formato do ranking
      this.topCaregiversForTemplate = this.topCaregivers.map((c) => ({
        name: c.name,
        calls: c.quantity,
        avgResponse: c.meanResponse,
      }));

      this.topResidentsForTemplate = this.topResidents.map((r) => ({
        name: r.name,
        calls: r.quantity,
        avgResponse: r.meanResponse,
      }));
    } catch (error) {
      console.error('Erro ao carregar dados do relatório:', error);
    }
  }
}
