// import { Component, EventEmitter, Input, Output } from '@angular/core';
// import { ModalComponent } from '../../../components/modal/modal.component';
// import { ResidentService } from '../../../controller/resident/resident.service';
// import { ResidentDetailsResponseDto } from '../../../model/Resident';
// import { ButtonComponent } from "../../../components/button/button.component";
// import { Router } from '@angular/router';

// @Component({
//   selector: 'app-detail',
//   standalone: true,
//   imports: [ModalComponent, ButtonComponent],
//   templateUrl: './detail.component.html',
//   styleUrl: './detail.component.scss',
// })
// export class DetailComponent {
//   @Input() residentId!: string | null;
//   @Input() showModal: boolean = false;

//   @Output() closeModal = new EventEmitter<void>();

//   residentDetails: ResidentDetailsResponseDto | null = null;

//   constructor(private residentControllerService: ResidentService, private router: Router) {}

// async getResidentInformations(): Promise<void> {
//   if (!this.residentId) {
//     console.error('Resident ID is required to fetch details');
//     return;
//   }

//   const details = await this.residentControllerService.getResidentById(this.residentId);

//   if (details?.birthDate) {
//     const date = new Date(details.birthDate);
//     details.birthDate = date.toISOString().split('T')[0];
//   }

//   this.residentDetails = details;
// }

//   async ngOnChanges() {
//     if (this.showModal) {
//       await this.getResidentInformations();
//     }
//   }

//   onCloseModal() {
//     this.showModal = false;
//     this.closeModal.emit();
//   }
//   onUpdateResident(resident: ResidentDetailsResponseDto) {
//     if (!this.residentId) {
//       console.error('Resident ID is required to update details');
//       return;
//     }

//     this.router.navigate(['residents/update/', this.residentId]);
//   }
// }
