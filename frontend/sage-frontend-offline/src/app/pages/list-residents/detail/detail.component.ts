import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ModalComponent } from '../../../components/modal/modal.component';

@Component({
  selector: 'app-detail',
  standalone: true,
  imports: [ModalComponent],
  templateUrl: './detail.component.html',
  styleUrl: './detail.component.scss',
})
export class DetailComponent {
  @Input() residentId: string | null = null;
  @Input() showModal: boolean = false;

  @Output() closeModal = new EventEmitter<void>();

  onCloseModal() {
    this.showModal = false;
    this.closeModal.emit();
  }
}
