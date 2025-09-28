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
