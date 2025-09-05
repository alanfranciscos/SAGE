import { Component } from '@angular/core';
import { MainComponent } from '../../layout/main/main.component';
import { ResidentComponent } from './resident/resident.component';
import { SearchInputComponent } from '../../components/search-input/search-input.component';
import { AlertComponent } from '../../components/alert/alert.component';
import { ButtonComponent } from '../../components/button/button.component';
import { ResidentService } from '../../controller/resident/resident.service';
import { ResidentListResponseDto } from '../../model/Resident';
import { Router } from '@angular/router';
import { DetailComponent } from './detail/detail.component';
import { PaginationComponent } from '../../components/pagination/pagination.component';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { SseService } from '../../controller/sse/sse.service';
@Component({
  selector: 'app-list-residents',
  standalone: true,
  imports: [
    MainComponent,
    ResidentComponent,
    SearchInputComponent,
    AlertComponent,
    ButtonComponent,
    DetailComponent,
    PaginationComponent,
  ],
  templateUrl: './list-residents.component.html',
  styleUrl: './list-residents.component.scss',
})
export class ListResidentsComponent {
  residents: ResidentListResponseDto | null = null;

  hasAlert: boolean = false;
  alertType: 'warning' | 'several' = 'warning';
  loadingResidents: boolean = true;
  errorResidents: boolean = false;

  selectedResidentId: string | null = null;
  showDetail: boolean = false;
  openAlertModal: boolean = false;

  readonly pageSize = 10; // ou qualquer número que faça sentido pra você
  currentPage: number = 1;
  searchTerm: string = '';

  private subscription?: Subscription;

  constructor(
    private residentControllerService: ResidentService,
    private router: Router,
    private toastr: ToastrService,
    private sseService: SseService
  ) {}

  async fetchResidents() {
    const skip = (this.currentPage - 1) * this.pageSize;
    this.loadingResidents = true;
    try {
      this.residents = await this.residentControllerService.getResidents(
        this.pageSize,
        skip,
        this.searchTerm
      );
      this.errorResidents = false;
    } catch (error) {
      console.error('Error fetching residents:', error);
      this.errorResidents = true;
    }
    this.loadingResidents = false;

    if (this.residents) {
      this.hasAlert =
        this.residents.severalResidents.length > 0 ||
        this.residents.warningResidents.length > 0;
    }

    if (this.hasAlert && this.residents) {
      this.alertType = 'warning';
      if (this.residents.severalResidents.length > 0) {
        this.alertType = 'several';
      }
    }
  }

  async ngOnInit() {
    this.subscription = this.sseService.messages$.subscribe((msg) => {
      if (!msg) return;

      // debug:
      console.log('Sidebar recebeu:', msg);

      // exemplo: só reage a eventos "assignment-change"
      if (msg.type === 'assignment-change') {
        console.log('Residentes atualizados:', msg.data);

        this.currentPage = 1;
        this.searchTerm = '';
        this.fetchResidents();
      }
    });

    // carregar a primeira vez
    this.fetchResidents();
  }

  onSearch(term: string) {
    this.searchTerm = term;
    this.currentPage = 1; // Reinicia na primeira página
    this.fetchResidents();
    this.toastr.warning('Fulano de tal', 'Alarme acionado');
  }
  onPageChange(page: number) {
    this.currentPage = page;
    this.fetchResidents();
  }

  getTotalItems(): number {
    return this.residents ? this.residents.totalResidents : 0;
  }

  onRegisterResident() {
    this.router.navigate(['/residents/register']);
  }

  onShowDetails(residentId: string) {
    this.selectedResidentId = residentId;
    this.showDetail = true;
  }
  onOpenAlertModal(residentId: string) {
    this.selectedResidentId = residentId;
    this.openAlertModal = true;
  }
}
