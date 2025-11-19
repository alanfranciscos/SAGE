import { Component, Input, OnInit, OnDestroy, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ResidentHeaderResponseDto } from '../../model/Resident';
import { AccessibilityService } from '../../controller/accessibility/accessibility.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-alert-resident-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './alert-resident-card.component.html',
  styleUrls: ['./alert-resident-card.component.scss'],
})
export class AlertResidentCardComponent implements OnInit, OnDestroy {
  @Input() resident!: ResidentHeaderResponseDto;

  @Input() name: string = '';
  @Input() room: string = '';
  @Input() time: string = '';
  @Input() imageUrl: string = '';
  @Input() observation?: string;
  @Input() level: 'normal' | 'medio' | 'critico' = 'normal';
  @Input() status: 'pendente' | 'em_atendimento' | 'atendido' = 'pendente';

  daltonicMode = false;
  private accessibilitySub?: Subscription;
  private accessibilityService = inject(AccessibilityService);

  ngOnInit() {
    this.accessibilitySub = this.accessibilityService.daltonicMode$.subscribe(
      (enabled) => (this.daltonicMode = enabled)
    );
  }

  ngOnDestroy() {
    this.accessibilitySub?.unsubscribe();
  }

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

  // === cores adaptadas para modo daltônico ===
  getLevelColor(): string {
    if (this.daltonicMode) {
      switch (this.level) {
        case 'normal':
          return '#4DA6FF'; // azul
        case 'medio':
          return '#FFD700'; // amarelo
        case 'critico':
          return '#800080'; // roxo
      }
    } else {
      switch (this.level) {
        case 'normal':
          return '#4caf50';
        case 'medio':
          return '#ff9800';
        case 'critico':
          return '#f44336';
      }
    }
  }

  getStatusColor(): string {
    if (this.daltonicMode) {
      switch (this.status) {
        case 'pendente':
          return '#FF6B6B'; // vermelho suave
        case 'em_atendimento':
          return '#4DA6FF'; // azul
        case 'atendido':
          return '#800080'; // roxo
      }
    } else {
      switch (this.status) {
        case 'pendente':
          return '#f44336';
        case 'em_atendimento':
          return '#ff9800';
        case 'atendido':
          return '#0069C5';
      }
    }
  }
}
