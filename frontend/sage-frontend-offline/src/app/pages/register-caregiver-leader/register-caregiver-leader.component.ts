import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { RegisterComponent } from '../../layout/register/register.component';
import { InputComponent } from '../../components/input/input.component';
import { SelectInputComponent } from '../../components/select-input/select-input.component';
import { ImageInputComponent } from '../../components/image-input/image-input.component';
import {
  CreateCaregiverLeaderRequestDto,
  CaregiverLeaderHeaderResponseDto,
} from '../../model/CaregiverLeader';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatNativeDateModule } from '@angular/material/core';
import { CommonModule } from '@angular/common';
import { Control } from '../../model/Control';
import { ToastrService } from 'ngx-toastr';
import { CaregiverLeaderService } from '../../controller/caregiver-leader/caregiver-leader.service';

export enum CaregiverLeaderInputField {
  FULL_NAME = 'fullName',
  PHONE = 'phone',
  EMAIL = 'email',
  CPF = 'cpf',
  PASSWORD = 'caregiver_password', 
  CONFIRM_PASSWORD = 'confirmPassword', // Novo ENUM para o campo de confirmação
}

@Component({
  selector: 'app-register-caregiver-leader',
  standalone: true,
  imports: [
    RegisterComponent,
    InputComponent,
    SelectInputComponent,
    ImageInputComponent,
    MatDatepickerModule,
    MatFormFieldModule,
    MatInputModule,
    MatNativeDateModule,
    CommonModule,
  ],
  templateUrl: './register-caregiver-leader.component.html',
  styleUrl: './register-caregiver-leader.component.scss',
})
export class RegisterCaregiverLeaderComponent implements OnInit {
  steps = ['Identificação', 'Autenticação'];
  currentStep: number = 0;
  CaregiverLeaderInputField = CaregiverLeaderInputField;
  availableControls: number[] = [];
  caregiverLeaderListResponseDto: CreateCaregiverLeaderRequestDto = {
    fullName: '',
    phone: '',
    email: '',
    cpf: '',
    caregiver_password: '', 
  };
  
  // NOVA PROPRIEDADE
  confirmPassword: '' = '';

  cpfInvalido?: boolean;
  telefoneInvalido: boolean = false;
  emailInvalido: boolean = false;
  senhaInvalida: boolean = false; 
  senhaNaoConfere: boolean = false; // Novo estado para senhas que não coincidem

  private readonly EMAIL_REGEX = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  private readonly PASSWORD_COMPLEXITY_REGEX = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9@$!%*?&])[A-Za-z0-9@$!%*?&]{8,}$/;

  constructor(
    private caregiverLeaderService: CaregiverLeaderService,
    private router: Router,
    private toastr: ToastrService,
  ) {}

  async ngOnInit(): Promise<void> {}

  /**
   * Valida os campos da etapa atual.
   */
  validateStep(): boolean {
    switch (this.currentStep) {
      case 0:
        // Lógica da Etapa 0... (Sem alterações nesta seção)
        const { fullName, cpf, phone } = this.caregiverLeaderListResponseDto;
        const cpfNumerico = this.formatCPF(cpf);
        const phoneNumerico = this.formatPhone(phone);
        const camposPreenchidos = fullName.trim() !== '' && cpfNumerico !== '' && phoneNumerico !== '';
        if (!camposPreenchidos) return false;

        const cpfValido = !this.cpfInvalido;
        const telefoneValido = phoneNumerico.length >= 10 && phoneNumerico.length <= 11;
        this.telefoneInvalido = !telefoneValido;

        return cpfValido && telefoneValido;

      case 1:
        const { email, caregiver_password } = this.caregiverLeaderListResponseDto;

        // 1. Campos não vazios
        const camposPreenchidosAuth = email.trim() !== '' && caregiver_password.trim() !== '' && this.confirmPassword.trim() !== '';
        if (!camposPreenchidosAuth) return false;

        // 2. Validação de Email
        const emailValido = this.EMAIL_REGEX.test(email);
        this.emailInvalido = !emailValido;
        
        // 3. Validação de Complexidade de Senha
        const senhaForte = this.PASSWORD_COMPLEXITY_REGEX.test(caregiver_password);
        this.senhaInvalida = !senhaForte;
        
        // 4. Validação de Confirmação de Senha (NOVA LÓGICA)
        const senhasCoincidem = caregiver_password === this.confirmPassword;
        this.senhaNaoConfere = !senhasCoincidem;

        return emailValido && senhaForte && senhasCoincidem;

      default:
        return false;
    }
  }

  private validarCPF(cpf: string): boolean {
    // Lógica do CPF... (Sem alterações)
    cpf = cpf.replace(/\D/g, '');
    if (cpf.length !== 11 || /^(\d)\1+$/.test(cpf)) return false;
    let soma = 0;
    for (let i = 0; i < 9; i++) {
      soma += parseInt(cpf.charAt(i)) * (10 - i);
    }
    let resto = (soma * 10) % 11;
    if (resto === 10 || resto === 11) resto = 0;
    if (resto !== parseInt(cpf.charAt(9))) return false;
    soma = 0;
    for (let i = 0; i < 10; i++) {
      soma += parseInt(cpf.charAt(i)) * (11 - i);
    }
    resto = (soma * 10) % 11;
    if (resto === 10 || resto === 11) resto = 0;
    if (resto !== parseInt(cpf.charAt(10))) return false;
    return true;
  }

  updateStep(event: number): void {
    // Lógica do passo... (Sem alterações)
    if (event < this.currentStep) {
      this.currentStep = event;
      return;
    }
    if (this.validateStep()) { 
      this.currentStep = event;
    } else {
      this.toastr.warning('Preencha e valide todos os campos obrigatórios.', 'Atenção');
    }
  }

  onCancel(): void {
    this.router.navigate(['/']);
  }

  private formatCPF(cpf: string): string {
    return cpf.replace(/\D/g, '');
  }

  private formatPhone(phone: string): string {
    return phone.replace(/\D/g, '');
  }

  private formatFields(): CreateCaregiverLeaderRequestDto {
    return {
      ...this.caregiverLeaderListResponseDto,
      fullName: this.caregiverLeaderListResponseDto.fullName.trim(),
      cpf: this.formatCPF(this.caregiverLeaderListResponseDto.cpf),
      phone: this.formatPhone(this.caregiverLeaderListResponseDto.phone),
      email: this.caregiverLeaderListResponseDto.email.trim(),
    };
  }

  async onFinish(): Promise<void> {
    if (!this.validateStep()) {
      this.toastr.warning('Não é possível finalizar. Verifique os campos.', 'Atenção');
      return; 
    }
    
    const dadosParaEnvio = this.formatFields();
    
    // NOVO: Imprime os dados no console antes de enviar
    console.log('Dados do Enfermeiro(a) Líder prontos para envio:', dadosParaEnvio);

    try {
      await this.caregiverLeaderService.createCaregiverLeader(dadosParaEnvio);

      this.toastr.success('Enfermeiro(a) Líder criado(a) com sucesso!', 'Sucesso');
      this.router.navigate(['/']);
    } catch (error: any) {
      console.error('Error creating caregiver leader:', error);
      
      const errorMsg = error.error?.message || 'Falha ao criar enfermeiro(a) líder. Tente novamente.';
      
      if (error.status === 409 && error.error?.field === 'email') {
          this.toastr.error('O e-mail informado já está cadastrado.', 'Erro de Duplicidade');
      } else if (error.status === 409 && error.error?.field === 'cpf') {
          this.toastr.error('O CPF informado já está cadastrado.', 'Erro de Duplicidade');
      } else {
          this.toastr.error(errorMsg, 'Erro Geral');
      }
    }
  }

  onInputChange(field: CaregiverLeaderInputField, event: any): void {
    // 1. Atualiza o valor. Verifica se o campo é a confirmação de senha.
    if (field === CaregiverLeaderInputField.CONFIRM_PASSWORD) {
        this.confirmPassword = event;
    } else {
        (this.caregiverLeaderListResponseDto as any)[field] = event;
    }

    // 2. Lógica de Validação Específica por Campo
    if (field === CaregiverLeaderInputField.CPF) {
      const cpfNumerico = this.formatCPF(event);
      this.caregiverLeaderListResponseDto.cpf = cpfNumerico;
      this.cpfInvalido = cpfNumerico.length === 11 ? !this.validarCPF(cpfNumerico) : false;
    }
    
    if (field === CaregiverLeaderInputField.PHONE) {
      const phoneNumerico = this.formatPhone(event);
      this.caregiverLeaderListResponseDto.phone = phoneNumerico;
    }
    
    // Revalida a etapa para atualizar o estado da validação de senha/confirmação
    if (field === CaregiverLeaderInputField.PASSWORD || field === CaregiverLeaderInputField.CONFIRM_PASSWORD) {
        this.validateStep();
    }
  }
  
  private imageFileToBase64(file: File): Promise<string> {
    // Lógica de Base64... (Sem alterações)
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => resolve(reader.result as string);
      reader.onerror = (error) => reject(error);
      reader.readAsDataURL(file);
    });
  }

  async onImageChange(event: any): Promise<void> {
    if (event instanceof File) {
      const base64String = await this.imageFileToBase64(event);
      this.caregiverLeaderListResponseDto.imageData = base64String;
    }
  }
}