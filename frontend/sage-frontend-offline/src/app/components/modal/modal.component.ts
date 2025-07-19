import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-modal',
  standalone: true,
  imports: [],
  templateUrl: './modal.component.html',
  styleUrl: './modal.component.scss',
})
export class ModalComponent {
  @Input() showModal: boolean = false;

  @Output() closeModal = new EventEmitter<void>();

  onCloseModal() {
    this.showModal = false;
    this.closeModal.emit();
  }
}
