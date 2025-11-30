import {
  Component,
  EventEmitter,
  Input,
  Output,
  OnInit,
  OnDestroy,
  inject,
  Inject,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ResidentDetailsResponseDto } from '../../model/Resident';
import { CommonModule, DOCUMENT } from '@angular/common';
import { StartAssistDialogComponent } from '../start-assist-dialog/start-assist-dialog.component';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { AssistService } from '../../controller/assist/assist.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { AccessibilityService } from '../../controller/accessibility/accessibility.service';
import { Router } from '@angular/router';

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
export class AlertResidentDetailCardComponent implements OnInit, OnDestroy {
  @Input() alertDetail!: ResidentAlertDetail;
  @Output() updated = new EventEmitter<ResidentAlertDetail>();

  newObservation: string = '';

  daltonicMode = false;
  private accessibilitySub?: Subscription;
  private accessibilityService = inject(AccessibilityService);
  private dialog = inject(MatDialog);
  private assistService = inject(AssistService);
  private toastr = inject(ToastrService);
  private router = inject(Router);

  constructor(@Inject(DOCUMENT) private document: Document) { }

  ngOnInit() {
    this.accessibilitySub = this.accessibilityService.daltonicMode$.subscribe(
      (enabled) => (this.daltonicMode = enabled)
    );
  }

  ngOnDestroy() {
    this.accessibilitySub?.unsubscribe();
  }

  // === Cores adaptáveis ao modo daltônico ===
  getLevelColor(): string {
    if (this.daltonicMode) {
      switch (this.alertDetail.severity) {
        case 'medio':
          return '#FFD700'; // amarelo
        case 'critico':
          return '#800080'; // roxo
        default:
          return '#4DA6FF'; // azul
      }
    } else {
      switch (this.alertDetail.severity) {
        case 'medio':
          return '#ff9800';
        case 'critico':
          return '#f44336';
        default:
          return '#4caf50';
      }
    }
  }

  getStatusColor(): string {
    if (this.daltonicMode) {
      switch (this.alertDetail.status) {
        case 'pendente':
          return '#FF6B6B'; // vermelho suave
        case 'em_atendimento':
          return '#4DA6FF'; // azul
        case 'atendido':
          return '#800080'; // roxo
        default:
          return '#ccc';
      }
    } else {
      switch (this.alertDetail.status) {
        case 'pendente':
          return '#f44336';
        case 'em_atendimento':
          return '#ff9800';
        case 'atendido':
          return '#4caf50';
        default:
          return '#ccc';
      }
    }
  }

  atenderChamado() {
    if (this.newObservation.trim()) {
      this.alertDetail.status = 'atendido';
      this.alertDetail.observations = this.newObservation;
      this.newObservation = '';
      this.updated.emit(this.alertDetail);
    }
  }

  calcularIdade(birthDate: string | undefined | null): number {
    if (!birthDate) return 0;
    const nascimento = new Date(birthDate);
    if (isNaN(nascimento.getTime())) return 0;
    const hoje = new Date();
    let idade = hoje.getFullYear() - nascimento.getFullYear();
    const m = hoje.getMonth() - nascimento.getMonth();
    if (m < 0 || (m === 0 && hoje.getDate() < nascimento.getDate())) idade--;
    return idade;
  }

  handleAtendimento() {
    if (this.alertDetail.status === 'pendente') {
      this.openStartAssistDialog();
    } else if (this.alertDetail.status === 'em_atendimento') {
      if (!this.alertDetail.caregiverToken) {
        this.openStartAssistDialog(true);
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
                this.toastr.error('Falha ao iniciar atendimento.', 'Erro');
              },
            });
        } else {
          this.alertDetail.caregiverToken = caregiverToken;
          this.finalizeAssist();
        }
      }
    });
  }

  // Componente já deve ter o 'router' injetado (this.router)
  // Componente já deve ter o 'document' injetado (this.document)

  finalizeAssist() {
    if (!this.alertDetail.caregiverToken) {
      this.toastr.error('Token do cuidador não informado!', 'Erro');
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
          this.toastr.success('Atendimento finalizado com sucesso!', 'Sucesso');
          this.updated.emit(this.alertDetail);

          // 1. Inicia a navegação para a rota Home ('/')
          this.router.navigate(['/'])
            // 2. Usa .then() para esperar a navegação ser CONCLUÍDA
            .then(() => {
              // ✨ O reload é executado aqui, GARANTINDO que você já está na tela Home (DashboardComponent)
              this.document.location.reload();
            })
            .catch(err => {
              console.error('Falha na navegação antes do reload:', err);
            });
        },
        error: (err) => {
          this.toastr.error('Falha ao finalizar atendimento.', 'Erro');
        },
      });
  }
}
