import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EditNurseDialogComponent } from '../update-nurse-modal/update-nurse-modal.component';
import { Nurse } from '../../model/Nurse';
import { NurseService } from '../../controller/nurse.service';
import { ToastrService } from 'ngx-toastr';

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

  constructor(
    private nurseService: NurseService,
    private toastr: ToastrService
  ) {}

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
  async handleUpdateNurse(updated: Nurse): Promise<void> {
    if (!updated.id) {
      console.error('ID da enfermeira não encontrado!');
      return;
    }

    try {
      const payload = {
        fullName: updated.name,
        cpf: updated.cpf,
        email: updated.email,
        phone: updated.tel,
        position: 'employee',
      };

      const saved = await this.nurseService.updateNurse(updated.id, payload);

      // Atualiza a lista local
      const index = this.nurses.findIndex((n) => n.id === saved.id);
      if (index !== -1) {
        this.nurses[index] = { ...this.nurses[index], ...payload };
      }

      this.editingNurse = null;
      this.toastr.success('Enfermeira atualizada com sucesso!', 'Sucesso');
    } catch (err) {
      console.error('Erro ao atualizar enfermeira:', err);
      this.toastr.error(
        'Falha ao atualizar enfermeira. Tente novamente.',
        'Erro'
      );
    }
    window.location.reload();
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

  // async toggleActive(nurse: Nurse) {
  //   if (!nurse.id) return;

  //   const newStatus = nurse.status === 'active' ? false : true;

  //   try {
  //     await this.nurseService.setActiveStatus(nurse.id, newStatus);
  //     nurse.status = newStatus ? 'active' : 'inactive'; // Atualiza localmente
  //     this.toastr.success(
  //       `Enfermeira ${nurse.name} agora está ${
  //         newStatus ? 'ativa' : 'inativa'
  //       }`,
  //       'Sucesso'
  //     );
  //   } catch (err) {
  //     console.error('Erro ao alterar status da enfermeira:', err);
  //     this.toastr.error(
  //       'Falha ao alterar status da enfermeira. Tente novamente.',
  //       'Erro'
  //     );
  //   }
  // }
  async toggleActive(nurse: Nurse) {
    if (!nurse.id) return;

    const newStatus = nurse.status === 'active' ? false : true;

    try {
      await this.nurseService.setActiveStatus(nurse.id, newStatus);
      nurse.status = newStatus ? 'active' : 'inactive'; // atualiza localmente
      this.toastr.success(
        `Enfermeira ${nurse.name} agora está ${
          newStatus ? 'ativa' : 'inativa'
        }`,
        'Sucesso'
      );
    } catch (err) {
      console.error('Erro ao alterar status da enfermeira:', err);
      this.toastr.error(
        'Falha ao alterar status da enfermeira. Tente novamente.',
        'Erro'
      );
    }
  }
}
