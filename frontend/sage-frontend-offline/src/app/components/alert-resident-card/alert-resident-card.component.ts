import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ResidentHeaderResponseDto } from '../../model/Resident';

@Component({
  selector: 'app-alert-resident-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './alert-resident-card.component.html',
  styleUrls: ['./alert-resident-card.component.scss'],
})
export class AlertResidentCardComponent {
  @Input() resident!: ResidentHeaderResponseDto;

  // extras calculados no front (não vêm do backend)
  @Input() time: string = '';
  @Input() level: 'normal' | 'medio' | 'critico' = 'normal';
  @Input() status: 'pendente' | 'em_atendimento' | 'atendido' = 'pendente';
  @Input() observation?: string;
  defaultPatient = {
    name: 'João da Silva',
    room: '101',
    time: '5 min',
    imageUrl: 'assets/images/paciente1.png',
    level: 'normal',
    status: 'pendente',
  };

  pacienteAtendido = {
    name: 'Maria Oliveira',
    room: '202',
    time: '15 min',
    imageUrl: 'assets/images/paciente2.png',
    level: 'critico',
    status: 'atendido',
    observation: 'Paciente atendido pelo Dr. Paulo',
  };
}
