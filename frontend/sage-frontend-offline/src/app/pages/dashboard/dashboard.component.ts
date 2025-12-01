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
import { Subscription } from 'rxjs';
import { SseService } from '../../controller/sse/sse.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    NgFor,
    CommonModule,
    MainComponent,
    SummaryCardComponent,
    DashboardResidentCardComponent,
    DashboardResidentDetailsModalComponent,
    SearchInputComponent,
    ButtonComponent,
  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  totalResidents = 0;
  totalResolvedToday = 0;
  totalActiveCalls = 0;
  meanTime = '';

  private subscription?: Subscription;

  selectedResidentId: string | null = null;
  showModal = false;
  showOnlyActive = true; // inicialmente, apenas residentes ativos
  activeResidentsCount = 0;
  inactiveResidentsCount = 0;

  page = 0; // primeira página = 0
  pageSize = 10;
  loading = false;
  allLoaded = false;

  searchTerm = '';

  residents: Resident[] = []; // lista completa carregada
  displayedResidents: Resident[] = []; // lista exibida no HTML

  constructor(
    private residentService: ResidentService,
    private router: Router,
    private sseService: SseService
  ) {}

  ngOnInit() {
    this.initCounters();
    this.loadDashboardData();
    this.initSSE();
  }

  /** Inicializa contadores reativos */
  private initCounters() {
    this.residentService.totalActiveCalls$.subscribe((total) => {
      this.totalActiveCalls = total;
    });
  }

  /** Inicializa SSE para atualizar dashboard em tempo real */
  private initSSE() {
    this.sseService.messages$.subscribe(async (msg) => {
      if (!msg) return;

      if (msg.type === 'assignment-change') {
        try {
          await this.reloadFirstPage();
        } catch (err) {
          console.error('Erro ao atualizar dashboard via SSE:', err);
        }
      }
    });
  }

  /** Recarrega primeira página e atualiza contadores */
  private async reloadFirstPage() {
    this.page = 0;
    this.residents = [];
    this.displayedResidents = [];
    this.allLoaded = false;
    await this.loadDashboardData();
  }

  /** Carrega dados iniciais do dashboard */
  async loadDashboardData() {
    this.loading = true;
    try {
      // === 1️⃣ Carrega primeira página de residentes com filtro ===
      const firstPage = await this.residentService.getResidents(
        this.pageSize,
        0,
        this.searchTerm || undefined,
        this.showOnlyActive ? true : undefined
      );
      this.residents = this.sortResidents(firstPage);
      this.displayedResidents = [...this.residents];

      // === 2️⃣ Pega totais do backend ===
      this.totalResidents =
        await this.residentService.getTotalResidentsNumber();
      this.totalResolvedToday =
        await this.residentService.getTotalResolvedToday();
      this.meanTime = await this.residentService.getMeanTime();

      // === 3️⃣ Pega total de residentes ativos ===
      const totalActive =
        await this.residentService.getTotalActiveResidentsCalls();

      if (this.showOnlyActive) {
        // mostra apenas ativos carregados na página
        this.activeResidentsCount = this.residents.length;
        this.inactiveResidentsCount = 0;
      } else {
        // mostra ativo / inativo corretamente
        this.activeResidentsCount = totalActive;
        this.inactiveResidentsCount = this.totalResidents - totalActive;
      }

      // === 4️⃣ Atualiza paginação ===
      this.page = 1;
      this.allLoaded = firstPage.length < this.pageSize;
    } catch (err) {
      console.error('Erro loadDashboardData:', err);
    } finally {
      this.loading = false;
    }
  }

  /** Carrega próxima página ao clicar "Carregar mais" */
  async loadResidentsPage() {
    if (this.loading || this.allLoaded) return;

    this.loading = true;
    try {
      const skip = this.page * this.pageSize;
      const newResidents = await this.residentService.getResidents(
        this.pageSize,
        skip,
        this.searchTerm || undefined,
        this.showOnlyActive ? true : undefined
      );

      if (!newResidents || newResidents.length === 0) {
        this.allLoaded = true;
        return;
      }

      this.residents = [...this.residents, ...newResidents];
      this.displayedResidents = [...this.residents];
      this.page++;
      if (newResidents.length < this.pageSize) this.allLoaded = true;
    } catch (err) {
      console.error('Erro ao carregar página:', err);
    } finally {
      this.loading = false;
    }
  }

  /** Ordena residentes por prioridade (emergency > warning > normal) */
  private sortResidents(residents: Resident[]): Resident[] {
    const severityOrder: Record<string, number> = {
      emergency: 1,
      warning: 2,
      normal: 3,
    };
    return [...residents].sort((a, b) => {
      const orderA = severityOrder[a.severityLevel ?? 'normal'] ?? 99;
      const orderB = severityOrder[b.severityLevel ?? 'normal'] ?? 99;
      return orderA - orderB;
    });
  }

  /** Mapeia severityLevel para status do card */
  getStatus(
    severity: 'emergency' | 'warning' | null
  ): 'normal' | 'warning' | 'critical' {
    if (severity === 'emergency') return 'critical';
    if (severity === 'warning') return 'warning';
    return 'normal';
  }

  /** Busca de residentes */
  onSearch(term: string) {
    this.searchTerm = (term || '').trim();
    this.page = 0;
    this.residents = [];
    this.displayedResidents = [];
    this.allLoaded = false;
    this.loadResidentsSearch();
  }

  async loadResidentsSearch() {
    await this.loadDashboardData();
  }

  /** Abre modal de detalhes */
  onOpenDetails(residentId: string) {
    this.selectedResidentId =
      this.residents.find((r) => r.id === residentId)?.id || null;
    this.showModal = true;
  }

  /** Fecha modal */
  onCloseModal() {
    this.showModal = false;
    this.selectedResidentId = null;
  }

  /** Navega para registro de residente */
  onRegisterResident() {
    this.router.navigate(['residents/register']);
  }

  /** Alterna entre mostrar apenas ativos ou todos */
  toggleShowActive() {
    this.showOnlyActive = !this.showOnlyActive;
    this.reloadFirstPage();
  }

  /** Formata tempo decorrido (ex.: "2 dias 05h30") */
  formatElapsedTime(elapsed: string): string {
    if (!elapsed) return '-';
    const parts = elapsed.split(' ');
    let days = 0;
    let timePart = '00:00';
    if (parts.length === 4 && parts[1] === 'days') {
      days = parseInt(parts[0], 10);
      timePart = parts[2];
    } else if (parts.length === 3 && parts[1] === 'days') {
      days = parseInt(parts[0], 10);
      timePart = parts[2];
    } else if (parts.length === 1) {
      timePart = parts[0];
    }
    const [hours = '00', minutes = '00'] = timePart.split(':');
    return `${days} dia${days !== 1 ? 's' : ''} ${hours}h${minutes}`;
  }
}
