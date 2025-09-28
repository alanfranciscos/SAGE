import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonComponent } from '../button/button.component';
import { InputComponent } from '../input/input.component';
import { CommonModule } from '@angular/common';
import { RegisterNurseDto } from '../../model/Nurse';
import { NurseService } from '../../controller/nurse.service';

@Component({
  selector: 'app-register-nurse-component',
  standalone: true,
  imports: [FormsModule, ButtonComponent, InputComponent, CommonModule],
  templateUrl: './register-nurse-component.component.html',
  styleUrl: './register-nurse-component.component.scss',
})
export class RegisterNurseComponentComponent {
  nurse: RegisterNurseDto = {
    fullName: '',
    cpf: '',
    email: '',
    phone: '',
    position: 'employee',
  };

  cpfInvalido: boolean = false;
  telefoneInvalido: boolean = false;

  constructor(private nurseService: NurseService) {}

  private validarCPF(cpf: string): boolean {
    cpf = cpf.replace(/\D/g, '');
    if (cpf.length !== 11 || /^(\d)\1+$/.test(cpf)) return false;

    let soma = 0;
    for (let i = 0; i < 9; i++) soma += parseInt(cpf.charAt(i)) * (10 - i);
    let resto = (soma * 10) % 11;
    if (resto === 10 || resto === 11) resto = 0;
    if (resto !== parseInt(cpf.charAt(9))) return false;

    soma = 0;
    for (let i = 0; i < 10; i++) soma += parseInt(cpf.charAt(i)) * (11 - i);
    resto = (soma * 10) % 11;
    if (resto === 10 || resto === 11) resto = 0;
    if (resto !== parseInt(cpf.charAt(10))) return false;

    return true;
  }

  private validarTelefone(telefone: string): boolean {
    const telNumerico = telefone.replace(/\D/g, '');
    return telNumerico.length === 10 || telNumerico.length === 11;
  }

  // Normaliza os campos antes de enviar
  private formatFields(): RegisterNurseDto {
    return {
      ...this.nurse,
      fullName: this.nurse.fullName.trim(),
      cpf: this.nurse.cpf.replace(/\D/g, ''),
      phone: this.nurse.phone!.replace(/\D/g, ''),
    };
  }

  async onRegisterNurse() {
    this.nurse = this.formatFields();
    try {
      await this.nurseService.registerNurse(this.nurse);
      alert('Cuidador cadastrada com sucesso!');

      // Recarrega a página
      window.location.reload();
    } catch (error) {
      console.error(error);
      alert('Erro ao cadastrar cuidador. Veja o console.');
    }
  }

  onInputChange(field: keyof RegisterNurseDto, event: any) {
    (this.nurse as any)[field] = event;

    if (field === 'cpf') {
      const cpfNumerico = event.replace(/\D/g, '');
      this.cpfInvalido =
        cpfNumerico.length === 11 ? !this.validarCPF(cpfNumerico) : false;
    }

    if (field === 'phone') {
      this.telefoneInvalido = !this.validarTelefone(event);
    }
  }
}
