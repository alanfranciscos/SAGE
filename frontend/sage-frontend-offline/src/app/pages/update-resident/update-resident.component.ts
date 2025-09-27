// import { Component, OnInit } from '@angular/core';
// import { Router, ActivatedRoute } from '@angular/router';
// import { InputComponent } from '../../components/input/input.component';
// import { SelectInputComponent } from '../../components/select-input/select-input.component';
// import { ImageInputComponent } from '../../components/image-input/image-input.component';
// import { ResidentService } from '../../controller/resident/resident.service';
// import {
//   CreateResidentRequestDto,
//   ResidentDetailsResponseDto,
// } from '../../model/Resident';
// import { updateComponent } from '../../layout/update/update.component';

@Component({
  selector: 'app-update-resident',
  standalone: true,
  imports: [
    updateComponent,
    InputComponent,
    SelectInputComponent,
    ImageInputComponent,
  ],
  templateUrl: './update-resident.component.html',
  styleUrl: './update-resident.component.scss',
})
export class UpdateResidentComponent implements OnInit {
  title = 'Atualizar Paciente';
  steps = ['Identificação', 'Contato de emergência', 'Residência'];
  currentStep = 0;
  validStep = false;
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

//   }

  async ngOnInit(): Promise<void> {
    this.residentId = this.route.snapshot.paramMap.get('id')!;
    await this.loadResident();
  }

//       this.residentData = {
//         fullName: resident.fullName,
//         cpf: resident.cpf,
//         sex: resident.sex,
//         birthDate: resident.birthDate
//           ? resident.birthDate.split('T')[0]
//           : '',
//         emergencyName: resident.emergencyName ?? '',
//         emergencyPhone: resident.emergencyPhone ?? '',
//         residentialUnit: resident.residentialUnit ?? '',
//         relationship: resident.relationship ?? '',
//         controlNumber: resident.controlNumber ?? '',
//         imageData: resident.imageData ?? '',
//       };
//     } catch (err) {
//       console.error(err);
//       alert('Erro ao carregar os dados do paciente.');
//       this.router.navigate(['/residents']);
//     }
//   }

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
      alert('Erro ao carregar os dados do paciente.');
      this.router.navigate(['/residents']);
    }
  }

//   validateStep(): boolean {
//     switch (this.currentStep) {
//       case 0:
//         return (
//           this.residentData.fullName.trim() !== '' &&
//           this.residentData.cpf.trim() !== '' &&
//           this.residentData.sex.trim() !== '' &&
//           this.residentData.birthDate.trim() !== ''
//         );
//       case 1:
//         return true;
//       case 2:
//         return (
//           this.residentData.residentialUnit.trim() !== '' &&
//           this.residentData.controlNumber > 0
//         );
//       default:
//         return false;
//     }
//   }

//   async onFinish() {
//     try {
//       await this.residentService.updateResident(
//         this.residentId,
//         this.residentData
//       );
//       alert('Paciente atualizado com sucesso!');
//       this.router.navigate(['/residents']);
//     } catch (err) {
//       console.error(err);
//       alert('Erro ao atualizar paciente.');
//     }
//   }

//   onCancel() {
//     this.router.navigate(['/residents']);
//   }
//   onImageSelected(file: File | null) {
//   if (!file) {
//     this.residentData.imageData = '';
//     return;
//   }

  onCancel() {
    this.router.navigate(['/residents']);
  }
  onImageSelected(file: File | null) {
    if (!file) {
      this.residentData.imageData = '';
      return;
    }

    const reader = new FileReader();
    reader.onload = () => {
      this.residentData.imageData = reader.result as string; // base64
    };
    reader.readAsDataURL(file);
  }
}
