import { Router } from '@angular/router';
import { Component } from '@angular/core';
import { RegisterComponent } from '../../layout/register/register.component';
import { InputComponent } from '../../components/input/input.component';
import { SelectInputComponent } from '../../components/select-input/select-input.component';
import { ImageInputComponent } from '../../components/image-input/image-input.component';
import {
  CreateResidentRequestDto,
  ResidentListResponseDto,
} from '../../model/Resident';
import { ResidentService } from '../../controller/resident/resident.service';

export enum ResidentInputField {
  FULL_NAME = 'fullName',
  CPF = 'cpf',
  SEX = 'sex',
  BIRTH_DATE = 'birthDate',
  EMERGENCY_NAME = 'emergencyName',
  EMERGENCY_PHONE = 'emergencyPhone',
  RELATIONSHIP = 'relationship',
  RESIDENTIAL_UNIT = 'residentialUnit',
  CONTROL_NUMBER = 'controlNumber',
}

@Component({
  selector: 'app-register-resident',
  standalone: true,
  imports: [
    RegisterComponent,
    InputComponent,
    SelectInputComponent,
    ImageInputComponent,
  ],
  templateUrl: './register-resident.component.html',
  styleUrl: './register-resident.component.scss',
})
export class RegisterResidentComponent {
  steps = ['Identificação', 'Contato de emergência', 'Residência'];
  currentStep: number = 0;
  ResidentInputField = ResidentInputField;
  residentListResponseDto: CreateResidentRequestDto = {
    fullName: '',
    cpf: '',
    sex: '',
    birthDate: '',
    residentialUnit: '',
    controlNumber: 0,
    
  };
  cpfInvalido?: boolean;

  constructor(
    private residentControllerService: ResidentService,
    private router: Router,

  ) {}

  validateStep(): boolean {
    switch (this.currentStep) {
      case 0:
        return (
          this.residentListResponseDto.fullName !== '' &&
          this.residentListResponseDto.cpf !== '' &&
          this.residentListResponseDto.sex !== '' &&
          this.residentListResponseDto.birthDate !== ''
        );
      case 1:
        return true;
      case 2:
        return (
          this.residentListResponseDto.residentialUnit !== '' &&
          this.residentListResponseDto.controlNumber > 0
        );
      default:
        return false;
    }
  }

private validarCPF(cpf: string): boolean {
  cpf = cpf.replace(/[^\d]+/g, '');

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
    this.router.navigate(['/residents']);
  }

  private formatFields(): CreateResidentRequestDto {
    return {
      ...this.residentListResponseDto,
      fullName: this.residentListResponseDto.fullName.trim(),
      birthDate: new Date(
        this.residentListResponseDto.birthDate.trim()
      ).toISOString(),
    };
  }

  async onFinish(): Promise<void> {
    this.residentListResponseDto = this.formatFields();
    try {
      await this.residentControllerService.createResident(
        this.residentListResponseDto
      );
      this.router.navigate(['/residents']);
    } catch (error) {
      console.error('Error creating resident:', error);
      alert('Falha ao criar residente. Por favor, tente novamente.');
    }
  }

onInputChange(field: ResidentInputField, event: any): void {
  (this.residentListResponseDto as any)[field] = event;

  if (field === ResidentInputField.CPF) {
    this.cpfInvalido = !this.validarCPF(event);
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
      this.residentListResponseDto.imageData = file;
    }
  }
}
