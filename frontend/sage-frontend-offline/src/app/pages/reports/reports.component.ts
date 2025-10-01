import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MainComponent } from '../../layout/main/main.component';
import { HeaderCardReportComponent } from '../../components/header-card-report/header-card-report.component';
import { ReportsService } from '../../controller/reports/reports.service';
import { GenericReportsCardComponent } from '../../components/generic-reports-card/generic-reports-card.component';
import { GenericReportsRankingCardComponent } from '../../components/generic-reports-ranking-card/generic-reports-ranking-card.component';
import { DateFilterComponent } from '../../components/search/search-reports.component';

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [
    MainComponent,
    HeaderCardReportComponent,
    CommonModule,
    GenericReportsCardComponent,
    GenericReportsRankingCardComponent,
    DateFilterComponent,
  ],
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.scss'],
})
export class ReportsComponent implements OnInit {
  // Rankings
  topCaregivers: any[] = [];
  topResidents: any[] = [];
  topCaregiversForTemplate: any[] = [];
  topResidentsForTemplate: any[] = [];

  // Tradução dos dias da semana
  weekdaysMap: Record<string, string> = {
    Monday: 'Segunda',
    Tuesday: 'Terça',
    Wednesday: 'Quarta',
    Thursday: 'Quinta',
    Friday: 'Sexta',
    Saturday: 'Sábado',
    Sunday: 'Domingo',
  };

  // Cards
  cards = [
    {
      title: 'Total de Chamados',
      value: '...',
      // delta: '+12% vs mês anterior',
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
      iconBgColor: 'bg-purple-100',
      iconColor: 'text-purple-600',
      icon: 'schedule',
      valueClass: 'value-default',
    },
    {
      title: 'Taxa de Chamados Críticos',
      value: '...',
      delta: '',
      iconBgColor: 'bg-red-100',
      iconColor: 'text-red-600',
      icon: 'priority_high',
      valueClass: 'value-default',
    },
  ];

  constructor(private reportsService: ReportsService) {}

  ngOnInit(): void {
    this.loadReportData();
  }

  /** ================== CARREGAMENTO DE DADOS ================== **/
  private async loadReportData() {
    try {
      // Cards
      this.cards[0].value = (
        await this.reportsService.getTotalAssists()
      ).toString();

      const avgResponse = await this.reportsService.getAverageResponseTime();
      const avgResolution =
        await this.reportsService.getAverageResolutionTime();

      this.cards[1].value = this.formatElapsedTime(avgResponse);
      this.cards[2].value = this.formatElapsedTime(avgResolution);

      const criticalRate = await this.reportsService.getCriticalAssistsRate();
      this.cards[3].value = `${criticalRate}%`;
      this.cards[3].valueClass = criticalRate > 0 ? 'value-red' : 'value-green';

      // Rankings
      this.topCaregivers =
        await this.reportsService.getTopFiveCaregiverPerformance();
      this.topResidents = await this.reportsService.getTopFiveCallResidents();

      this.topCaregiversForTemplate = this.topCaregivers.map((c) => ({
        name: c.name,
        calls: c.quantity,
        avgResponse: this.formatElapsedTime(c.meanResponse),
      }));

      this.topResidentsForTemplate = this.topResidents.map((r) => ({
        name: r.name,
        calls: r.quantity,
        avgResponse: this.formatElapsedTime(r.meanResponse),
      }));
    } catch (error) {
      console.error('Erro ao carregar dados do relatório:', error);
    }
  }

  /** ================== FORMATAÇÃO DE TEMPO ================== **/
  formatElapsedTime(time: string): string {
    if (!time) return '---';

    const match = time.match(/(\d+) days (\d+):(\d+):(\d+)/);
    if (!match) return time.split('.')[0];

    const days = parseInt(match[1], 10);
    const hours = parseInt(match[2], 10);
    const minutes = parseInt(match[3], 10);

    let result = '';
    if (days > 0) result += `${days} dias `;
    if (hours > 0) result += `${hours}h `;
    if (minutes > 0) result += `${minutes}m`;

    return result.trim();
  }

  /** ================== TRADUZIR DIAS DA SEMANA ================== **/
  translateWeekdays(data: Record<string, number>): {
    labels: string[];
    values: number[];
  } {
    const labels: string[] = [];
    const values: number[] = [];

    for (const key in data) {
      labels.push(this.weekdaysMap[key] || key);
      values.push(data[key]);
    }

    return { labels, values };
  }
  async onDateFilterChange(filter: { startDate: string; endDate: string }) {
    try {
      const { startDate, endDate } = filter;

      // Cards
      this.cards[0].value = (
        await this.reportsService.getTotalAssists(startDate, endDate)
      ).toString();

      const avgResponse = await this.reportsService.getAverageResponseTime(
        startDate,
        endDate
      );
      const avgResolution = await this.reportsService.getAverageResolutionTime(
        startDate,
        endDate
      );

      this.cards[1].value = this.formatElapsedTime(avgResponse);
      this.cards[2].value = this.formatElapsedTime(avgResolution);

      const criticalRate = await this.reportsService.getCriticalAssistsRate(); // Se quiser, pode criar versão com filtro
      this.cards[3].value = `${criticalRate}%`;
      this.cards[3].valueClass = criticalRate > 0 ? 'value-red' : 'value-green';

      // Rankings
      this.topCaregivers =
        await this.reportsService.getTopFiveCaregiverPerformance(); // Pode filtrar se backend permitir
      this.topResidents = await this.reportsService.getTopFiveCallResidents();

      this.topCaregiversForTemplate = this.topCaregivers.map((c) => ({
        name: c.name,
        calls: c.quantity,
        avgResponse: this.formatElapsedTime(c.meanResponse),
      }));

      this.topResidentsForTemplate = this.topResidents.map((r) => ({
        name: r.name,
        calls: r.quantity,
        avgResponse: this.formatElapsedTime(r.meanResponse),
      }));
    } catch (error) {
      console.error('Erro ao aplicar filtro de datas:', error);
    }
  }
}
