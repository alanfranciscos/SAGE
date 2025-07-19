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
  ],
  templateUrl: './list-residents.component.html',
  styleUrl: './list-residents.component.scss',
})
export class ListResidentsComponent {
  residents: ResidentListResponseDto | null = null;
  currentPage: number = 1;
  hasAlert: boolean = false;
  alertType: 'warning' | 'several' = 'warning';
  loadingResidents: boolean = true;
  errorResidents: boolean = false;

  selectedResidentId: string | null = null;
  showDetail: boolean = false;

  constructor(
    private residentControllerService: ResidentService,
    private router: Router
  ) {}

  async fetchResidents() {
    this.loadingResidents = true;
    try {
      this.residents = await this.residentControllerService.getResidents();
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
    this.fetchResidents();
  }

  onSearch($event: string) {}

  onPageChange(page: number) {
    this.currentPage = page;
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
}
