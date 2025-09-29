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
import { ResidentService } from '../../controller/resident/resident.service';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatNativeDateModule } from '@angular/material/core';
import { CommonModule } from '@angular/common';
import { Control } from '../../model/Control';
import { ControlService } from '../../controller/control/control.service';
import { ToastrService } from 'ngx-toastr';
import { CaregiverLeaderService } from '../../controller/caregiver-leader/caregiver-leader.service';

export enum CaregiverLeaderInputField {
  FULL_NAME = 'fullName',
  PHONE = 'phone',
  EMAIL = 'email',
  CPF = 'cpf',
  PASSWORD = 'password',
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
    caregiver_password: ' ',
  };

  cpfInvalido?: boolean;
  telefoneInvalido: boolean = false;

  constructor(
    private caregiverLeaderService: CaregiverLeaderService,
    private router: Router,
    private toastr: ToastrService
  ) {}
  async ngOnInit(): Promise<void> {
    // this.availableControls = await this.controlService.getAvailableControls();
    // console.log('Controles disponíveis:', this.availableControls);
  }

  validateStep(): boolean {
    switch (this.currentStep) {
      case 0:
        return (
          this.caregiverLeaderListResponseDto.fullName !== '' &&
          this.caregiverLeaderListResponseDto.cpf !== '' &&
          this.caregiverLeaderListResponseDto.phone !== ''
        );
      case 1:
        return (
          this.caregiverLeaderListResponseDto.email !== '' &&
          this.caregiverLeaderListResponseDto.caregiver_password !== ''
        );
      default:
        return false;
    }
  }

  private validarCPF(cpf: string): boolean {
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
    if (event < this.currentStep) {
      this.currentStep = event;
      return;
    }
    if (this.validateStep()) {
      this.currentStep = event;
    }
  }

  onCancel(): void {
    this.router.navigate(['/']);
  }

  private formatFields(): CreateCaregiverLeaderRequestDto {
    return {
      ...this.caregiverLeaderListResponseDto,
      fullName: this.caregiverLeaderListResponseDto.fullName.trim(),
      cpf: this.caregiverLeaderListResponseDto.cpf.replace(/\D/g, ''),
      phone: this.caregiverLeaderListResponseDto.phone.replace(
        /\D/g,
        ''
      ),
    };
  }

  async onFinish(): Promise<void> {
    this.caregiverLeaderListResponseDto = this.formatFields();
    try {
      await this.caregiverLeaderService.createCaregiverLeader(
        this.caregiverLeaderListResponseDto
      );

      this.toastr.success('Envermeiro(a) Líder criado com sucesso!', 'Sucesso');
      this.router.navigate(['/']);
    } catch (error) {
      console.error('Error creating caregiver leader:', error);
      this.toastr.error('Falha ao criar enfermeiro(a) lider. Tente novamente.', 'Erro');
    }
  }

  onInputChange(field: CaregiverLeaderInputField, event: any): void {
    (this.caregiverLeaderListResponseDto as any)[field] = event;

    if (field === CaregiverLeaderInputField.CPF) {
      const cpfNumerico = event.replace(/\D/g, '');
      this.cpfInvalido =
        cpfNumerico.length === 11 ? !this.validarCPF(cpfNumerico) : false;
    }


    if (field === CaregiverLeaderInputField.CPF) {
      const cpfNumerico = event.replace(/\D/g, '');
      this.caregiverLeaderListResponseDto.cpf = cpfNumerico;
      this.cpfInvalido =
        cpfNumerico.length === 11 ? !this.validarCPF(cpfNumerico) : false;
    }

    this.validateStep();
  }
  private imageFileToBase64(file: File): Promise<string> {
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
      const file = base64String;
      this.caregiverLeaderListResponseDto.imageData = file;
    }
  }
}
