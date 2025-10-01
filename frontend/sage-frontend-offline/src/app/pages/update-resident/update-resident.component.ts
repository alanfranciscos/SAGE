import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { InputComponent } from '../../components/input/input.component';
import { SelectInputComponent } from '../../components/select-input/select-input.component';
import { ImageInputComponent } from '../../components/image-input/image-input.component';
import { ResidentService } from '../../controller/resident/resident.service';
import {
  CreateResidentRequestDto,
  ResidentDetailsResponseDto,
} from '../../model/Resident';
import { updateComponent } from '../../layout/update/update.component';
import { CommonModule } from '@angular/common';
import { RegisterComponent } from '../../layout/register/register.component';
import { Toast, ToastrModule, ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-update-resident',
  standalone: true,
  imports: [
    InputComponent,
    SelectInputComponent,
    ImageInputComponent,
    CommonModule,
    RegisterComponent,
  ],
  templateUrl: './update-resident.component.html',
  styleUrl: './update-resident.component.scss',
})
export class UpdateResidentComponent implements OnInit {
  title = 'Atualizar Residente';
  steps = ['Identificação', 'Contato de emergência', 'Residência'];
  currentStep = 0;
  residentId!: string;

  residentData: CreateResidentRequestDto = {
    fullName: '',
    cpf: '',
    sex: '',
    birthDate: '',
    emergencyName: '',
    emergencyPhone: '',
    residentialUnit: '',
    controlNumber: 0,
    imageData: '',
    relationship: '',
  };

  constructor(
    private residentService: ResidentService,
    private router: Router,
    private route: ActivatedRoute,
    private toast: ToastrService
  ) {}

  async ngOnInit(): Promise<void> {
    this.residentId = this.route.snapshot.paramMap.get('id')!;
    await this.loadResident();
  }

  private async loadResident() {
    try {
      const resident: ResidentDetailsResponseDto =
        await this.residentService.getResidentById(this.residentId);

      this.residentData = {
        fullName: resident.fullName,
        cpf: resident.cpf,
        sex: resident.sex,
        birthDate: resident.birthDate ? resident.birthDate.split('T')[0] : '',
        emergencyName: resident.emergencyFullName,
        emergencyPhone: resident.emergencyPhone,
        residentialUnit: resident.residentialUnit,
        relationship: resident.emergencyRelationship,
        controlNumber: resident.controlId,
        imageData: resident.imageData ?? '',
      };
    } catch (err) {
      console.error(err);
      alert('Erro ao carregar os dados do residente.');
      this.router.navigate(['/residents']);
    }
  }

  updateStep(step: number) {
    if (step < this.currentStep || this.validateStep()) {
      this.currentStep = step;
    }
  }

  validateStep(): boolean {
    switch (this.currentStep) {
      case 0:
        return (
          this.residentData.fullName.trim() !== '' &&
          this.residentData.cpf.trim() !== '' &&
          this.residentData.sex.trim() !== '' &&
          this.residentData.birthDate.trim() !== ''
        );
      case 1:
        return true;
      case 2:
        return (
          this.residentData.residentialUnit.trim() !== '' &&
          this.residentData.controlNumber > 0
        );
      default:
        return false;
    }
  }

  private formatFields(): CreateResidentRequestDto {
    return {
      ...this.residentData,
      fullName: this.residentData.fullName.trim(),
      cpf: this.residentData.cpf.replace(/\D/g, ''), // só números
      emergencyPhone: this.residentData.emergencyPhone.replace(/\D/g, ''), // só números
      birthDate: new Date(this.residentData.birthDate).toISOString(),
      controlNumber: Number(this.residentData.controlNumber), // garante que é number
    };
  }

  async onFinish() {
    try {
      const formattedData = this.formatFields();
      await this.residentService.updateResident(this.residentId, formattedData);
      this.toast.success('Residente atualizado com sucesso.');
      this.router.navigate(['/']);
    } catch (err) {
      console.error(err);
      this.toast.error('Erro ao atualizar o residente.');
    }
    // console.log('Finalizar atualização:', this.residentData);
  }

  onCancel() {
    this.router.navigate(['/']);
  }

  // onImageSelected(file: File | null) {
  //   if (!file) {
  //     this.residentData.imageData = '';
  //     return;
  //   }

  //   const reader = new FileReader();
  //   reader.onload = () => {
  //     this.residentData.imageData = reader.result as string; // base64
  //   };
  //   reader.readAsDataURL(file);
  // }
  onImageSelected(file: File | null) {
    if (!file) {
      this.residentData.imageData = '';
      return;
    }

    const reader = new FileReader();
    reader.onload = async () => {
      this.residentData.imageData = reader.result as string;

      try {
        // envia a imagem para o backend assim que o usuário seleciona
        await this.residentService.updateResidentImage(this.residentId, file);
      } catch (err) {}
    };
    reader.readAsDataURL(file);
  }
}
