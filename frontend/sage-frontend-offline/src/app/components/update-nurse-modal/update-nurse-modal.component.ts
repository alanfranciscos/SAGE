import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Nurse } from '../../model/Nurse'; // importa a interface oficial

@Component({
  selector: 'app-edit-nurse-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './update-nurse-modal.component.html',
  styleUrl: './update-nurse-modal.component.scss',
})
export class EditNurseDialogComponent {
  @Input() editingNurse: Nurse | null = null;
  @Output() update = new EventEmitter<Nurse>();
  @Output() cancel = new EventEmitter<void>();

  handleUpdateNurse() {
    if (this.editingNurse) {
      this.update.emit(this.editingNurse);
    }
  }

  cancelEdit() {
    this.cancel.emit();
  }
}
