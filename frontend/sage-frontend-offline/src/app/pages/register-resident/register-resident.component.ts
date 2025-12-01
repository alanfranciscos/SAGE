import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { RegisterComponent } from '../../layout/register/register.component';
import { InputComponent } from '../../components/input/input.component';
import { SelectInputComponent } from '../../components/select-input/select-input.component';
import { ImageInputComponent } from '../../components/image-input/image-input.component';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatNativeDateModule } from '@angular/material/core';
import { CommonModule } from '@angular/common';
import {
  CreateResidentRequestDto,
  ResidentListResponseDto,
} from '../../model/Resident';
import { ResidentService } from '../../controller/resident/resident.service';
import { ControlService } from '../../controller/control/control.service';
import { ToastrService } from 'ngx-toastr';

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
    MatDatepickerModule,
    MatFormFieldModule,
    MatInputModule,
    MatNativeDateModule,
    CommonModule,
  ],
  templateUrl: './register-resident.component.html',
  styleUrls: ['./register-resident.component.scss'],
})
export class RegisterResidentComponent implements OnInit {
  steps = ['Identificação', 'Contato de emergência', 'Residência'];
  currentStep: number = 0;
  ResidentInputField = ResidentInputField;

  availableControls: number[] = [];
  controlOptions: { value: string; label: string; disabled?: boolean }[] = [];

  today = new Date().toISOString().split('T')[0];

  residentListResponseDto: CreateResidentRequestDto = {
    fullName: '',
    cpf: '',
    sex: '',
    birthDate: '',
    residentialUnit: '',
    controlNumber: 0,
    emergencyName: '',
    emergencyPhone: '',
    relationship: '',
    imageData: '',
  };

  cpfInvalido?: boolean;
  telefoneInvalido: boolean = false;
  isLoading: boolean = false;

  constructor(
    private residentControllerService: ResidentService,
    private router: Router,
    private controlService: ControlService,
    private toastr: ToastrService
  ) {}

  async ngOnInit(): Promise<void> {
    this.availableControls = await this.controlService.getAvailableControls();
    console.log('Controles disponíveis:', this.availableControls);

    // Monta o array de opções para o app-select-input
    this.controlOptions = [
      { value: '', label: 'Selecione um controle', disabled: true },
      ...this.availableControls.map((control) => ({
        value: control.toString(),
        label: `Controle #${control}`,
      })),
    ];
  }

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
          this.residentListResponseDto.controlNumber > 0 &&
          this.residentListResponseDto.controlNumber <= 100
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

  private formatFields(): CreateResidentRequestDto {
    return {
      ...this.residentListResponseDto,
      fullName: this.residentListResponseDto.fullName.trim(),
      cpf: this.residentListResponseDto.cpf.replace(/\D/g, ''),
      emergencyPhone: this.residentListResponseDto.emergencyPhone.replace(
        /\D/g,
        ''
      ),
      birthDate: new Date(
        this.residentListResponseDto.birthDate.trim()
      ).toISOString(),
    };
  }

  async onFinish(): Promise<void> {
    try {
      this.isLoading = true;

      const payload: CreateResidentRequestDto = this.formatFields();

      const createdResidentId: string =
        await this.residentControllerService.createResident(payload);
      console.log('Residente criado com ID:', createdResidentId);

      if (payload.imageData && payload.imageData instanceof File) {
        await this.residentControllerService.updateResidentImage(
          createdResidentId,
          payload.imageData
        );
        console.log(
          'Imagem enviada com sucesso para o residente:',
          createdResidentId
        );
      }

      this.toastr.success('Residente criado com sucesso!', 'Sucesso');
      this.residentListResponseDto = this.getEmptyResident();
      this.router.navigate(['/residents']);
    } catch (error) {
      console.error('Erro ao criar residente ou enviar imagem:', error);
      this.toastr.error('Erro ao criar residente ou enviar imagem.', 'Erro');
    } finally {
      this.isLoading = false;
    }
  }

  private getEmptyResident(): CreateResidentRequestDto {
    return {
      fullName: '',
      cpf: '',
      sex: '',
      birthDate: '',
      residentialUnit: '',
      controlNumber: 0,
      emergencyName: '',
      emergencyPhone: '',
      relationship: '',
    };
  }

  onInputChange(field: ResidentInputField, event: any): void {
    (this.residentListResponseDto as any)[field] = event;

    if (field === ResidentInputField.CPF) {
      const cpfNumerico = event.replace(/\D/g, '');
      this.cpfInvalido =
        cpfNumerico.length === 11 ? !this.validarCPF(cpfNumerico) : false;
      this.residentListResponseDto.cpf = cpfNumerico;
    }

    if (field === ResidentInputField.CONTROL_NUMBER) {
      const value = Number(event);
      if (isNaN(value) || value < 0 || value > 100) {
        alert('O número de controle deve estar entre 0 e 100.');
        this.residentListResponseDto.controlNumber = 0;
        return;
      }
      this.residentListResponseDto.controlNumber = value;
    }

    if (field === ResidentInputField.BIRTH_DATE) {
      const selectedDate = new Date(event);
      const today = new Date();
      selectedDate.setHours(0, 0, 0, 0);
      today.setHours(0, 0, 0, 0);
      if (selectedDate > today) {
        alert('A data de nascimento não pode ser no futuro.');
        this.residentListResponseDto.birthDate = '';
        return;
      }
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

  async onImageChange(event: File | null): Promise<void> {
    if (event) {
      this.residentListResponseDto.imageData = event; // guarda o File puro
    }
  }
}
