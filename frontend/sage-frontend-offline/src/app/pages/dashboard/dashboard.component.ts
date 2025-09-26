// import { NgFor } from '@angular/common';
// import { Component, OnInit } from '@angular/core';
// import { ResidentService } from '../../controller/resident/resident.service';
// import { MainComponent } from '../../layout/main/main.component';
// import { SummaryCardComponent } from '../../components/summary-card/summary-card.component';
// import { DashboardResidentCardComponent } from '../../components/dashboard-resident-card/dashboard-resident-card.component';
// import { DashboardResidentDetailsModalComponent } from '../../components/dashboard-resident-details-modal/dashboard-resident-details-modal.component';
// import { SearchInputComponent } from '../../components/search-input/search-input.component';
// import { ButtonComponent } from '../../components/button/button.component';
// import { Resident } from '../../model/Resident';
// import { Router } from '@angular/router';
// import { Subscription } from 'rxjs/internal/Subscription';
// import { SseService } from '../../controller/sse/sse.service';

// @Component({
//   selector: 'app-dashboard',
//   standalone: true,
//   imports: [
//     NgFor,
//     MainComponent,
//     SummaryCardComponent,
//     DashboardResidentCardComponent,
//     DashboardResidentDetailsModalComponent,
//     SearchInputComponent,
//     ButtonComponent,
//   ],
//   templateUrl: './dashboard.component.html',
//   styleUrls: ['./dashboard.component.scss'],
// })
// export class DashboardComponent implements OnInit {
//   totalResidents: number = 0;
//   totalResolvedToday: number = 0;
//   totalActiveCalls: number = 0;
//   meanTime: string = '';
//   private subscription?: Subscription;

//   selectedResidentId: string | null = null;
//   showModal = false;
//   residents: Resident[] = [];

//   constructor(
//     private residentService: ResidentService,
//     private router: Router,
//     private sseService: SseService
//   ) {}
//   // async ngOnInit(): Promise<void> {
//   //   this.subscription = this.sseService.messages$.subscribe((msg) => {
//   //     if (!msg) return;
//   //   });
//   //   try {
//   //     this.totalResidents =
//   //       await this.residentService.getTotalResidentsNumber();
//   //     // console.log('totalResidents', this.totalResidents);
//   //     this.totalResolvedToday =
//   //       await this.residentService.getTotalResolvedToday();
//   //     // console.log('totalResolvedToday', this.totalResolvedToday);

//   //     this.meanTime = await this.residentService.getMeanTime();
//   //     // console.log('meanTime', this.meanTime);
//   //     this.totalActiveCalls =
//   //       await this.residentService.getTotalActiveResidentsCalls();
//   //     this.residents = await this.residentService.getResidents(10, 0);
//   //     console.log('residents', this.residents);

//   //     // console.log('totalActiveCalls', this.totalActiveCalls);
//   //   } catch (error) {}
//   // }
//   async ngOnInit(): Promise<void> {
//     this.subscription = this.sseService.messages$.subscribe(async (msg) => {
//       if (!msg) return;

//       console.log('Dashboard recebeu evento SSE:', msg);

//       // Exemplo: só reage a eventos do tipo "assignment-change"
//       if (msg.type === 'assignment-change') {
//         try {
//           // Recarrega residentes atualizados
//           this.residents = await this.residentService.getResidents(10, 0);

//           // Também atualiza os counters de cima
//           this.totalResidents =
//             await this.residentService.getTotalResidentsNumber();
//           this.totalResolvedToday =
//             await this.residentService.getTotalResolvedToday();
//           this.meanTime = await this.residentService.getMeanTime();
//           this.totalActiveCalls =
//             await this.residentService.getTotalActiveResidentsCalls();
//         } catch (error) {
//           console.error('Erro ao atualizar dashboard via SSE:', error);
//         }
//       }
//     });

//     // carregar a primeira vez
//     try {
//       this.totalResidents =
//         await this.residentService.getTotalResidentsNumber();
//       this.totalResolvedToday =
//         await this.residentService.getTotalResolvedToday();
//       this.meanTime = await this.residentService.getMeanTime();
//       this.totalActiveCalls =
//         await this.residentService.getTotalActiveResidentsCalls();
//       this.residents = await this.residentService.getResidents(10, 0);
//     } catch (error) {
//       console.error('Erro ao carregar dashboard:', error);
//     }
//   }
//   ngOnDestroy(): void {
//     this.subscription?.unsubscribe();
//   }

//   getStatus(
//     severity: 'emergency' | 'warning' | null
//   ): 'normal' | 'warning' | 'critical' {
//     if (severity === 'emergency') return 'critical';
//     if (severity === 'warning') return 'warning';
//     return 'normal';
//   }
//   getLastCallText(lastEndAt: string | null): string {
//     if (!lastEndAt) return 'sem chamados';

//     const now = new Date();
//     const lastCall = new Date(lastEndAt);
//     const diffMs = now.getTime() - lastCall.getTime();
//     const diffDays = Math.floor(diffMs / (1000 * 60 * 60 * 24));

//     const years = Math.floor(diffDays / 365);
//     const remainingDaysAfterYears = diffDays % 365;
//     const months = Math.floor(remainingDaysAfterYears / 30);
//     const days = remainingDaysAfterYears % 30;

//     let result = 'há ';
//     if (years > 0) result += `${years} ano${years > 1 ? 's' : ''} `;
//     if (months > 0) result += `${months} mes${months > 1 ? 'es' : ''} `;
//     if (days > 0) result += `${days} dia${days > 1 ? 's' : ''}`;

//     return result.trim();
//   }
//   onOpenDetails(residentId: string) {
//     this.selectedResidentId =
//       this.residents.find((resident) => resident.id === residentId)?.id || null;
//     this.showModal = true;
//   }

//   onCloseModal() {
//     this.showModal = false;
//     this.selectedResidentId = null;
//   }

//   onRegisterResident() {
//     this.router.navigate(['residents/register']);
//   }
// }
import { NgFor } from '@angular/common';
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
  residents: Resident[] = [];

  constructor(
    private residentService: ResidentService,
    private router: Router,
    private sseService: SseService
  ) {}

  // async ngOnInit(): Promise<void> {
  //   this.subscription = this.sseService.messages$.subscribe(async (msg) => {
  //     if (!msg) return;

  //     console.log('Dashboard recebeu evento SSE:', msg);

  //     if (msg.type === 'assignment-change') {
  //       try {
  //         const residents = await this.residentService.getResidents(10, 0);
  //         this.residents = this.sortResidents(residents);

  //         this.totalResidents =
  //           await this.residentService.getTotalResidentsNumber();
  //         this.totalResolvedToday =
  //           await this.residentService.getTotalResolvedToday();
  //         this.meanTime = await this.residentService.getMeanTime();
  //         this.totalActiveCalls =
  //           await this.residentService.getTotalActiveResidentsCalls();
  //       } catch (error) {
  //         console.error('Erro ao atualizar dashboard via SSE:', error);
  //       }
  //     }
  //   });

  //   try {
  //     this.totalResidents =
  //       await this.residentService.getTotalResidentsNumber();
  //     this.totalResolvedToday =
  //       await this.residentService.getTotalResolvedToday();
  //     this.meanTime = await this.residentService.getMeanTime();
  //     this.totalActiveCalls =
  //       await this.residentService.getTotalActiveResidentsCalls();

  //     const residents = await this.residentService.getResidents(10, 0);
  //     this.residents = this.sortResidents(residents);
  //   } catch (error) {
  //     console.error('Erro ao carregar dashboard:', error);
  //   }
  // }
  ngOnInit() {
    // Assinatura reativa
    this.residentService.totalActiveCalls$.subscribe((total) => {
      this.totalActiveCalls = total;
    });

    // Carrega os valores iniciais
    this.loadDashboardData();

    // Assinatura SSE para atualização em tempo real
    this.sseService.messages$.subscribe(async (msg) => {
      if (!msg) return;

      console.log('Dashboard recebeu evento SSE:', msg);

      if (msg.type === 'assignment-change') {
        try {
          // Atualiza residentes e ordena
          const residents = await this.residentService.getResidents(10, 0);
          this.residents = this.sortResidents(residents);

          // Atualiza os counters
          this.totalResidents =
            await this.residentService.getTotalResidentsNumber();
          this.totalResolvedToday =
            await this.residentService.getTotalResolvedToday();
          this.meanTime = await this.residentService.getMeanTime();

          // Atualiza total de chamados (BehaviorSubject dispara e atualiza sidebar automaticamente)
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
      await this.residentService.getTotalActiveResidentsCalls(); // atualiza BehaviorSubject
      this.residents = await this.residentService.getResidents(10, 0);
    } catch (err) {
      console.error(err);
    }
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
    if (!lastEndAt) return 'sem chamados';

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

  onOpenDetails(residentId: string) {
    this.selectedResidentId =
      this.residents.find((resident) => resident.id === residentId)?.id || null;
    this.showModal = true;
  }

  onCloseModal() {
    this.showModal = false;
    this.selectedResidentId = null;
  }

  onRegisterResident() {
    this.router.navigate(['residents/register']);
  }
}
