import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EditNurseDialogComponent } from '../update-nurse-modal/update-nurse-modal.component';

interface Nurse {
  id: string;
  name: string;
  cpf: string;
  email: string;
  tel: string;
  token: string;
  status: 'active' | 'inactive';
  lastUse: string;
  registration: string;
}

@Component({
  selector: 'app-list-nurse',
  standalone: true,
  imports: [CommonModule, FormsModule, EditNurseDialogComponent],
  templateUrl: './list-nurse-component.component.html',
  styleUrl: './list-nurse-component.component.scss',
})
export class ListNurseComponent {
  nurseSearchTerm = '';
  showTokens: { [key: string]: boolean } = {};

  nurses: Nurse[] = [
    {
      id: '1',
      name: 'Ana Paula',
      cpf: '12345678900',
      email: 'ana.paula@hospital.com',
      tel: '11999999999',
      token: 'ABC123',
      status: 'active',
      lastUse: '2023-09-01',
      registration: 'ENF001',
    },
  ];

  get filteredNurses(): Nurse[] {
    const term = this.nurseSearchTerm.toLowerCase();
    return this.nurses.filter(
      (n) =>
        n.name.toLowerCase().includes(term) ||
        n.registration.toLowerCase().includes(term)
    );
  }

  get activeNurseCount(): number {
    return this.nurses.filter((n) => n.status === 'active').length;
  }

  toggleToken(id: string): void {
    this.showTokens[id] = !this.showTokens[id];
  }

  copyToClipboard(token: string): void {
    navigator.clipboard.writeText(token);
  }

  handleToggleStatus(id: string): void {
    const nurse = this.nurses.find((n) => n.id === id);
    if (nurse) {
      nurse.status = nurse.status === 'active' ? 'inactive' : 'active';
    }
  }

  confirmAction(type: string, nurseId: string): void {
    if (type === 'regenerate') {
      const nurse = this.nurses.find((n) => n.id === nurseId);
      if (nurse) {
        nurse.token = this.generateToken();
      }
    }
  }

  private generateToken(): string {
    return Math.random().toString(36).substring(2, 8).toUpperCase();
  }

  editingNurse: Nurse | null = null;

  handleEditNurse(nurse: Nurse): void {
    this.editingNurse = { ...nurse }; // cria uma cópia para edição
  }

  handleUpdateNurse(updated: Nurse): void {
    const index = this.nurses.findIndex((n) => n.id === updated.id);
    if (index !== -1) {
      this.nurses[index] = updated;
      this.editingNurse = null;
    }
  }
}
