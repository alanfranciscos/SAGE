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

  formatElapsedTime(time: string): string {
    const match = time.match(/(\d+) days (\d+):(\d+):(\d+)/);
    if (!match) return time;

    const days = parseInt(match[1]);
    const hours = parseInt(match[2]);
    const minutes = parseInt(match[3]);

    let result = '';
    if (days > 0) result += `${days}d `;
    if (hours > 0) result += `${hours}h `;
    if (minutes > 0) result += `${minutes}m`;

    return result.trim();
  }
}
