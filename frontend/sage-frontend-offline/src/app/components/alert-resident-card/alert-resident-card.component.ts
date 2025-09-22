import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

export interface Patient {
  imageUrl: string;
  name: string;
  room: string;
  time: string;
  level: 'normal' | 'medio' | 'critico';
  status: 'pendente' | 'em_atendimento' | 'atendido';
  observation?: string;
}

@Component({
  selector: 'app-alert-resident-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './alert-resident-card.component.html',
  styleUrls: ['./alert-resident-card.component.scss'],
})
export class AlertResidentCardComponent {
  @Input() patient?: Patient;

  /** Paciente mockado para visualização se nenhum input for passado */
  defaultPatient: Patient = {
    imageUrl: 'https://randomuser.me/api/portraits/men/32.jpg',
    name: 'João da Silva',
    room: '12',
    time: 'há 3m9s',
    level: 'medio',
    status: 'pendente',
  };

  /** Outro exemplo de paciente atendido (com observação) */
  pacienteAtendido: Patient = {
    imageUrl: 'https://randomuser.me/api/portraits/women/44.jpg',
    name: 'Maria Souza',
    room: '21',
    time: 'há 10m',
    level: 'critico',
    status: 'atendido',
    observation: 'Paciente foi medicado e está em repouso.',
  };

  /** Getter usado no template */
  get p(): Patient {
    return this.patient ?? this.defaultPatient;
  }
}
