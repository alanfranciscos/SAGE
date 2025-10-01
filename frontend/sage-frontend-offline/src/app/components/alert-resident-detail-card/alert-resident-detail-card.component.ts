import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ResidentDetailsResponseDto } from '../../model/Resident';
import { CommonModule } from '@angular/common';
import { StartAssistDialogComponent } from '../start-assist-dialog/start-assist-dialog.component';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { AssistService } from '../../controller/assist/assist.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { ToastrModule, ToastrService } from 'ngx-toastr';

export interface ResidentAlertDetail extends ResidentDetailsResponseDto {
  severity: 'medio' | 'critico';
  status: 'pendente' | 'em_atendimento' | 'atendido';
  observations?: string;
  time: string; // tempo decorrido
  caregiverToken?: string;
}

@Component({
  selector: 'app-alert-resident-detail-card',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
  ],
  templateUrl: './alert-resident-detail-card.component.html',
  styleUrls: ['./alert-resident-detail-card.component.scss'],
})
export class AlertResidentDetailCardComponent {
  @Input() alertDetail!: ResidentAlertDetail;
  @Output() updated = new EventEmitter<ResidentAlertDetail>();

  newObservation: string = '';
  constructor(
    private dialog: MatDialog,
    private assistService: AssistService,
    private toastr: ToastrService
  ) {}

  atenderChamado() {
    if (this.newObservation.trim()) {
      this.alertDetail.status = 'atendido';
      this.alertDetail.observations = this.newObservation;
      this.newObservation = '';
      this.updated.emit(this.alertDetail);
    }
  }

  calcularIdade(birthDate: string | undefined | null): number {
    if (!birthDate) return 0; // caso não tenha data
    const nascimento = new Date(birthDate);
    if (isNaN(nascimento.getTime())) return 0; // data inválida
    const hoje = new Date();
    let idade = hoje.getFullYear() - nascimento.getFullYear();
    const m = hoje.getMonth() - nascimento.getMonth();
    if (m < 0 || (m === 0 && hoje.getDate() < nascimento.getDate())) {
      idade--;
    }
    return idade;
  }

  handleAtendimento() {
    if (this.alertDetail.status === 'pendente') {
      this.openStartAssistDialog(); // inicia atendimento
    } else if (this.alertDetail.status === 'em_atendimento') {
      // abrir modal se ainda não tiver token
      if (!this.alertDetail.caregiverToken) {
        this.openStartAssistDialog(true); // passa flag para finalizar
      } else {
        this.finalizeAssist();
      }
    }
  }

  openStartAssistDialog(isFinish: boolean = false) {
    const dialogRef = this.dialog.open(StartAssistDialogComponent, {
      width: '400px',
      data: { assistId: this.alertDetail.id },
    });

    dialogRef.afterClosed().subscribe((caregiverToken: string | undefined) => {
      if (caregiverToken) {
        if (!isFinish) {
          // start assist
          this.assistService
            .startAssist(this.alertDetail.id, caregiverToken)
            .subscribe({
              next: () => {
                this.alertDetail.status = 'em_atendimento';
                this.alertDetail.caregiverToken = caregiverToken;
                this.toastr.success(
                  'Atendimento iniciado com sucesso!',
                  'Sucesso'
                );
                this.updated.emit(this.alertDetail);
              },
              error: (err) => {
                console.error('Erro ao iniciar atendimento:', err);
                this.toastr.error('Falha ao iniciar atendimento.', 'Erro');
              },
            });
        } else {
          // finish assist
          this.alertDetail.caregiverToken = caregiverToken;
          this.finalizeAssist();
        }
      }
    });
  }

  finalizeAssist() {
    if (!this.alertDetail.caregiverToken) {
      console.error('Token do cuidador não informado!');
      return;
    }

    this.assistService
      .finishAssist(
        this.alertDetail.id,
        this.alertDetail.caregiverToken,
        this.newObservation
      )
      .subscribe({
        next: () => {
          this.alertDetail.status = 'atendido';
          this.alertDetail.observations = this.newObservation;
          this.newObservation = '';
          this.toastr.success('Atendimento iniciado com sucesso!', 'Sucesso');
          this.updated.emit(this.alertDetail);
          window.setTimeout(() => {
            window.location.reload();
          }, 2000);
        },
        error: (err) => {
          console.error('Erro ao iniciar atendimento:', err);
          this.toastr.error('Falha ao iniciar atendimento.', 'Erro');
        },
      });
  }
}
