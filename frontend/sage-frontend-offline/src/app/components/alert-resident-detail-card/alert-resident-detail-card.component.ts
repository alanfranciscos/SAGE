import { Component, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ResidentDetailsResponseDto } from '../../model/Resident';
import { CommonModule } from '@angular/common';

export interface ResidentAlertDetail extends ResidentDetailsResponseDto {
  severity: 'medio' | 'critico';
  status: 'pendente' | 'em_atendimento' | 'atendido';
  observations?: string;
  time: string; // tempo decorrido
}

@Component({
  selector: 'app-alert-resident-detail-card',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './alert-resident-detail-card.component.html',
  styleUrls: ['./alert-resident-detail-card.component.scss'],
})
export class AlertResidentDetailCardComponent {
  @Input() alertDetail!: ResidentAlertDetail;

  newObservation: string = '';

  atenderChamado() {
    if (this.newObservation.trim()) {
      this.alertDetail.status = 'atendido';
      this.alertDetail.observations = this.newObservation;
      this.newObservation = '';
    }
  }
  calcularIdade(birthDate: string): number {
    const nascimento = new Date(birthDate);
    const hoje = new Date();
    let idade = hoje.getFullYear() - nascimento.getFullYear();
    const m = hoje.getMonth() - nascimento.getMonth();
    if (m < 0 || (m === 0 && hoje.getDate() < nascimento.getDate())) {
      idade--;
    }
    return idade;
  }
}
