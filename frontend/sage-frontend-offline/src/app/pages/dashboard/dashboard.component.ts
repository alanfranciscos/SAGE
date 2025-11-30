import { CommonModule, NgFor } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ResidentService } from '../../controller/resident/resident.service';
import { MainComponent } from '../../layout/main/main.component';
import { SummaryCardComponent } from '../../components/summary-card/summary-card.component';
import { DashboardResidentCardComponent } from '../../components/dashboard-resident-card/dashboard-resident-card.component';
import { DashboardResidentDetailsModalComponent } from '../../components/dashboard-resident-details-modal/dashboard-resident-details-modal.component';
import { SearchInputComponent } from '../../components/search-input/search-input.component';
import { ButtonComponent } from '../../components/button/button.component';
import { Resident } from '../../model/Resident';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs/internal/Subscription';
import { SseService } from '../../controller/sse/sse.service';

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
    CommonModule,
  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  totalResidents: number = 0;
  totalResolvedToday: number = 0;
  totalActiveCalls: number = 0;
  meanTime: string = '';
  private subscription?: Subscription;

  selectedResidentId: string | null = null;
  showModal = false;
  page = 0;
  pageSize = 10;
  loading = false;
  allLoaded = false;
  searchTerm = '';
  residents: Resident[] = [];

  constructor(
    private residentService: ResidentService,
    private router: Router,
    private sseService: SseService
  ) {}

  ngOnInit() {
    // Assinatura reativa
    this.residentService.totalActiveCalls$.subscribe((total) => {
      this.totalActiveCalls = total;
    });

    this.loadDashboardData();

    this.sseService.messages$.subscribe(async (msg) => {
      if (!msg) return;

      console.log('Dashboard recebeu evento SSE:', msg);

      if (msg.type === 'assignment-change') {
        try {
          // Atualiza residentes e ordena
          const residents = await this.residentService.getResidents(
            10,
            0,
            undefined,
            true // somente ativos
          );
          this.residents = this.sortResidents(residents);

          // Atualiza os counters
          this.totalResidents =
            await this.residentService.getTotalResidentsNumber();
          this.totalResolvedToday =
            await this.residentService.getTotalResolvedToday();
          this.meanTime = await this.residentService.getMeanTime();

          await this.residentService.getTotalActiveResidentsCalls();
        } catch (error) {
          console.error('Erro ao atualizar dashboard via SSE:', error);
        }
      }
    });
  }

  async loadDashboardData() {
    try {
      this.totalResidents =
        await this.residentService.getTotalResidentsNumber();
      this.totalResolvedToday =
        await this.residentService.getTotalResolvedToday();
      this.meanTime = await this.residentService.getMeanTime();
      await this.residentService.getTotalActiveResidentsCalls();

      // ⬅ primeira página
      this.residents = await this.residentService.getResidents(
        this.pageSize,
        0,
        undefined,
        true
      );

      // configura para segunda página
      this.page = 1;
      this.allLoaded = false;
      this.loading = false;
    } catch (err) {
      console.error(err);
    }
  }

  async loadResidentsPage() {
    if (this.loading || this.allLoaded) return;

    this.loading = true;

    try {
      const skip = this.page * this.pageSize;

      const newResidents = await this.residentService.getResidents(
        this.pageSize,
        skip,
        this.searchTerm,
        true // active = true
      );

      // acabou
      if (newResidents.length === 0) {
        this.allLoaded = true;
        return;
      }

      // adiciona sem duplicar
      this.residents = [...this.residents, ...newResidents];

      this.page++; // ⬅ incrementa somente agora

      console.log('REQUEST:', {
        limit: this.pageSize,
        skip,
        search: this.searchTerm,
        active: true,
      });

      console.log('RESPONSE:', newResidents);
    } finally {
      this.loading = false;
    }
    console.log('PAGE:', this.page);
  }

  private sortResidents(residents: Resident[]): Resident[] {
    const severityOrder: Record<string, number> = {
      emergency: 1, // vermelho
      warning: 2, // amarelo
      normal: 3, // branco / sem chamado
    };

    return residents.sort((a, b) => {
      const orderA = severityOrder[a.severityLevel ?? 'normal'];
      const orderB = severityOrder[b.severityLevel ?? 'normal'];
      return orderA - orderB;
    });
  }

  getStatus(
    severity: 'emergency' | 'warning' | null
  ): 'normal' | 'warning' | 'critical' {
    if (severity === 'emergency') return 'critical';
    if (severity === 'warning') return 'warning';
    return 'normal';
  }

  getLastCallText(lastEndAt: string | null): string {
    if (!lastEndAt) return 'nenhum';

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
  onSearch(term: string) {
    // this.searchTerm = term.trim().toLowerCase();
    // this.loadResidentsSearch();
  }

  async loadResidentsSearch() {
    try {
      const residents = await this.residentService.getResidents(
        10,
        0,
        this.searchTerm,
        true // sempre residentes ativos
      );

      this.residents = this.sortResidents(residents);
    } catch (err) {
      console.error('Erro ao buscar residentes:', err);
    }
  }

  onOpenDetails(residentId: string) {
    this.selectedResidentId =
      this.residents.find((resident) => resident.id === residentId)?.id || null;
    this.showModal = true;
  }
  get filteredResidents(): Resident[] {
    if (!this.searchTerm) return this.residents;

    return this.residents.filter(
      (r) =>
        r.fullName.toLowerCase().includes(this.searchTerm) ||
        r.residentialUnit.toString().includes(this.searchTerm) ||
        (r.id && r.id.toLowerCase().includes(this.searchTerm))
    );
  }

  onCloseModal() {
    this.showModal = false;
    this.selectedResidentId = null;
  }

  onRegisterResident() {
    this.router.navigate(['residents/register']);
  }
  // onScroll(event: any) {
  //   const element = event.target;
  //   const threshold = 150; // quando faltar 150px para o fim
  //   if (
  //     element.scrollHeight - element.scrollTop - element.clientHeight <
  //     threshold
  //   ) {
  //     this.loadResidentsPage();
  //   }
  // }

  onScroll(event: any) {
    const element = event.target;

    const reachedBottom =
      element.scrollHeight - element.scrollTop <= element.clientHeight + 50;

    if (reachedBottom) {
      this.loadResidentsPage();
    }
  }

  formatElapsedTime(elapsed: string): string {
    if (!elapsed) return '-';

    // Ex.: "24 days 03:21:23.453585" ou "3 days 12:05:00"
    const parts = elapsed.split(' ');
    let days = 0;
    let timePart = '00:00';

    if (parts.length === 4 && parts[1] === 'days') {
      days = parseInt(parts[0], 10);
      timePart = parts[2]; // horas:minutos:segundos
    } else if (parts.length === 3 && parts[1] === 'days') {
      days = parseInt(parts[0], 10);
      timePart = parts[2];
    } else if (parts.length === 1) {
      timePart = parts[0];
    }

    const [hours, minutes] = timePart.split(':');

    return `${days} dia${days !== 1 ? 's' : ''} ${hours}h${minutes}`;
  }
}
