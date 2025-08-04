import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ModalComponent } from '../../../components/modal/modal.component';
import { ResidentService } from '../../../controller/resident/resident.service';
import { ResidentDetailsResponseDto } from '../../../model/Resident';

@Component({
  selector: 'app-detail',
  standalone: true,
  imports: [ModalComponent],
  templateUrl: './detail.component.html',
  styleUrl: './detail.component.scss',
})
export class DetailComponent {
  @Input() residentId!: string | null;
  @Input() showModal: boolean = false;

  @Output() closeModal = new EventEmitter<void>();

  residentDetails: ResidentDetailsResponseDto | null = null;

  constructor(private residentControllerService: ResidentService) {}

  async getResidentInformations(): Promise<void> {
    if (!this.residentId) {
      console.error('Resident ID is required to fetch details');
      return;
    }

    this.residentDetails = await this.residentControllerService.getResidentById(
      this.residentId
    );
  }

  //get resident details when the showModal is true
  async ngOnChanges() {
    if (this.showModal) {
      await this.getResidentInformations();
    }
  }

  onCloseModal() {
    this.showModal = false;
    this.closeModal.emit();
  }
}
