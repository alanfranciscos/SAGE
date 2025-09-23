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

  @Input() name: string = '';
  @Input() room: string = '';
  @Input() time: string = '';
  @Input() imageUrl: string = '';
  @Input() observation?: string;
  @Input() level: 'normal' | 'medio' | 'critico' = 'normal';
  @Input() status: 'pendente' | 'em_atendimento' | 'atendido' = 'pendente';
}
