import { Component, EventEmitter, Input, Output } from '@angular/core';

import { Router } from '@angular/router';
import { ModalComponent } from '../modal/modal.component';
import { ButtonComponent } from '../button/button.component';
import { ResidentDetailsResponseDto } from '../../model/Resident';
import { ResidentService } from '../../controller/resident/resident.service';

@Component({
  selector: 'app-dashboard-resident-details-modal',
  standalone: true,
  imports: [ModalComponent, ButtonComponent],
  templateUrl: './dashboard-resident-details-modal.component.html',
  styleUrl: './dashboard-resident-details-modal.component.scss',
})
export class DashboardResidentDetailsModalComponent {
  @Input() residentId!: string | null;
  @Input() showModal: boolean = false;

  @Output() closeModal = new EventEmitter<void>();

  residentDetails: ResidentDetailsResponseDto | null = null;

  constructor(
    private residentControllerService: ResidentService,
    private router: Router
  ) {}

  async getResidentInformations(): Promise<void> {
    if (!this.residentId) {
      console.error('Resident ID is required to fetch details');
      return;
    }

    const details = await this.residentControllerService.getResidentById(
      this.residentId
    );

    if (details?.birthDate) {
      const date = new Date(details.birthDate);
      const day = String(date.getDate()).padStart(2, '0');
      const month = String(date.getMonth() + 1).padStart(2, '0'); // meses começam do 0
      const year = date.getFullYear();
      details.birthDate = `${day}/${month}/${year}`; // formata para dd/MM/yyyy
    }

    if (details?.sex === 'M') {
      details.sex = 'Masculino';
    } else if (details?.sex === 'F') {
      details.sex = 'Feminino';
    }

    this.residentDetails = details;
  }

  async ngOnChanges() {
    if (this.showModal) {
      await this.getResidentInformations();
    }
  }

  onCloseModal() {
    this.showModal = false;
    this.closeModal.emit();
  }

  onUpdateResident(resident: ResidentDetailsResponseDto) {
    if (!this.residentId) {
      console.error('Resident ID is required to update details');
      return;
    }

    this.router.navigate(['residents/update/', this.residentId]);
  }
}
