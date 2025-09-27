// import { Component } from '@angular/core';
// import { CommonModule } from '@angular/common';
// import { FormsModule } from '@angular/forms';
// import { EditNurseDialogComponent } from '../update-nurse-modal/update-nurse-modal.component';

// interface Nurse {
//   id: string;
//   name: string;
//   cpf: string;
//   email: string;
//   tel: string;
//   token: string;
//   status: 'active' | 'inactive';
//   lastUse: string;
//   registration: string;
// }

// @Component({
//   selector: 'app-list-nurse',
//   standalone: true,
//   imports: [CommonModule, FormsModule, EditNurseDialogComponent],
//   templateUrl: './list-nurse-component.component.html',
//   styleUrl: './list-nurse-component.component.scss',
// })
// export class ListNurseComponent {
//   nurseSearchTerm = '';
//   showTokens: { [key: string]: boolean } = {};

//   nurses: Nurse[] = [
//     {
//       id: '1',
//       name: 'Ana Paula',
//       cpf: '12345678900',
//       email: 'ana.paula@hospital.com',
//       tel: '11999999999',
//       token: 'ABC123',
//       status: 'active',
//       lastUse: '2023-09-01',
//       registration: 'ENF001',
//     },
//   ];

//   get filteredNurses(): Nurse[] {
//     const term = this.nurseSearchTerm.toLowerCase();
//     return this.nurses.filter(
//       (n) =>
//         n.name.toLowerCase().includes(term) ||
//         n.registration.toLowerCase().includes(term)
//     );
//   }

//   get activeNurseCount(): number {
//     return this.nurses.filter((n) => n.status === 'active').length;
//   }

//   toggleToken(id: string): void {
//     this.showTokens[id] = !this.showTokens[id];
//   }

//   copyToClipboard(token: string): void {
//     navigator.clipboard.writeText(token);
//   }

//   handleToggleStatus(id: string): void {
//     const nurse = this.nurses.find((n) => n.id === id);
//     if (nurse) {
//       nurse.status = nurse.status === 'active' ? 'inactive' : 'active';
//     }
//   }

//   confirmAction(type: string, nurseId: string): void {
//     if (type === 'regenerate') {
//       const nurse = this.nurses.find((n) => n.id === nurseId);
//       if (nurse) {
//         nurse.token = this.generateToken();
//       }
//     }
//   }

//   private generateToken(): string {
//     return Math.random().toString(36).substring(2, 8).toUpperCase();
//   }

//   editingNurse: Nurse | null = null;

//   handleEditNurse(nurse: Nurse): void {
//     this.editingNurse = { ...nurse }; // cria uma cópia para edição
//   }

//   handleUpdateNurse(updated: Nurse): void {
//     const index = this.nurses.findIndex((n) => n.id === updated.id);
//     if (index !== -1) {
//       this.nurses[index] = updated;
//       this.editingNurse = null;
//     }
//   }
// }

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EditNurseDialogComponent } from '../update-nurse-modal/update-nurse-modal.component';
import { Nurse } from '../../model/Nurse';
import { NurseService } from '../../controller/nurse.service';

@Component({
  selector: 'app-list-nurse',
  standalone: true,
  imports: [CommonModule, FormsModule, EditNurseDialogComponent],
  templateUrl: './list-nurse-component.component.html',
  styleUrl: './list-nurse-component.component.scss',
})
export class ListNurseComponent implements OnInit {
  nurseSearchTerm = '';
  showTokens: { [key: string]: boolean } = {};
  nurses: Nurse[] = [];
  editingNurse: Nurse | null = null;

  constructor(private nurseService: NurseService) {}

  async ngOnInit(): Promise<void> {
    await this.loadNurses();
    console.log(this.nurses);
  }

  async loadNurses(): Promise<void> {
    try {
      this.nurses = await this.nurseService.getNurses(
        10,
        0,
        this.nurseSearchTerm
      );
    } catch (err) {
      console.error('Erro ao carregar enfermeiras:', err);
    }
  }

  get filteredNurses(): Nurse[] {
    const term = this.nurseSearchTerm.toLowerCase();
    return this.nurses.filter(
      (n) =>
        n.name.toLowerCase().includes(term) ||
        n.cpf.toLowerCase().includes(term) ||
        n.token.toLowerCase().includes(term)
    );
  }

  get activeNurseCount(): number {
    return this.nurses.filter((n) => n.status === 'active').length;
  }

  toggleToken(cpf: string): void {
    this.showTokens[cpf] = !this.showTokens[cpf];
  }

  copyToClipboard(token: string): void {
    navigator.clipboard.writeText(token);
  }

  handleEditNurse(nurse: Nurse): void {
    this.editingNurse = { ...nurse };
  }

  handleUpdateNurse(updated: Nurse): void {
    const index = this.nurses.findIndex((n) => n.cpf === updated.cpf);
    if (index !== -1) {
      this.nurses[index] = updated;
      this.editingNurse = null;
    }
  }
  confirmAction(action: 'regenerate', cpf: string): void {
    const nurse = this.nurses.find((n) => n.cpf === cpf);
    if (!nurse) return;

    if (
      confirm(
        `Tem certeza que deseja regenerar o token da enfermeira ${nurse.name}?`
      )
    ) {
      this.regenerateToken(cpf);
    }
  }

  private regenerateToken(cpf: string): void {
    console.log('Regenerando token para', cpf);
    // 👉 aqui você pode depois integrar com NurseService para chamar o backend
    // ex: this.nurseService.regenerateToken(cpf).then(...)
  }
}
