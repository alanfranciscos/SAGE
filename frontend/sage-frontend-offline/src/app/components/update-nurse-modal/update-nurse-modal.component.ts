import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Nurse } from '../../model/Nurse';

@Component({
  selector: 'app-edit-nurse-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './update-nurse-modal.component.html',
  styleUrls: ['./update-nurse-modal.component.scss'],
})
export class EditNurseDialogComponent {
  @Input() editingNurse: Nurse | null = null;
  @Output() update = new EventEmitter<Nurse>();
  @Output() cancel = new EventEmitter<void>();

  handleUpdateNurse(): void {
    if (this.editingNurse) {
      // devolve os dados editados para o pai
      this.update.emit({ ...this.editingNurse });
    }
  }

  cancelEdit(): void {
    this.cancel.emit();
  }
}
